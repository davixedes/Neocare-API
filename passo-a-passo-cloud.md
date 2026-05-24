# 🚀 Deploy da aplicação Neocare na Azure

Este guia descreve o passo a passo para provisionar toda a infraestrutura na Azure, fazer build da aplicação e realizar o deploy utilizando **Azure Container Apps + ACR + PostgreSQL**.

---

## 📋 Pré-requisitos

- Azure CLI instalada
- Docker instalado
- Conta Azure ativa

```bash
az login
```

---

## ☁️ 1. Criar Resource Group

```bash
az group create \
  --name neocare \
  --location canadacentral
```

---

## 📦 2. Criar Azure Container Registry (ACR)

```bash
az acr create \
  --resource-group neocare \
  --name neocareapi \
  --sku Basic
```

---

## 🔐 3. Habilitar credenciais do ACR

```bash
az acr update -n neocareapi --admin-enabled true
az acr credential show --name neocareapi
```

---

## 🔑 4. Login no ACR

```bash
az acr login --name neocareapi
```

---

## 🐳 5. Build e push da imagem Docker

```bash
docker buildx build \
  --platform linux/amd64 \
  -t neocareapi.azurecr.io/neocare-api:latest \
  --push \
  -f src/main/docker/Dockerfile .
```

---

## 🌐 6. Criar ambiente do Container Apps

```bash
az containerapp env create \
  --name neocare-env \
  --resource-group neocare \
  --location canadacentral
```

---

## 🛢️ 7. Criar banco PostgreSQL

```bash
az postgres flexible-server create \
  --name neocare-database \
  --resource-group neocare \
  --location canadacentral \
  --admin-user neocare_admin \
  --admin-password 1508D@vi
```

---

## 🔓 8. Liberar acesso ao banco (Firewall)

```bash
az postgres flexible-server firewall-rule create \
  --resource-group neocare \
  --name neocare-database \
  --rule-name allow-all \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 255.255.255.255
```

---

## 🧱 9. Criar banco de dados

```sql
CREATE DATABASE neocare;
```

---

## 🔐 10. Variáveis de ambiente

```bash
export DD_API_KEY=SEU_DATADOG_API_KEY
export SPRING_DATASOURCE_PASSWORD=1508D@vi
```

---

## 🚀 11. Deploy do Container App

```bash
az containerapp create \
  --name neocare-api \
  --resource-group neocare \
  --environment neocare-env \
  --image neocareapi.azurecr.io/neocare-api:latest \
  --registry-server neocareapi.azurecr.io \
  --registry-username neocareapi \
  --registry-password <SUA_SENHA_ACR> \
  --target-port 8080 \
  --ingress external \
  --cpu 0.5 \
  --memory 1Gi \
  --env-vars \
    DD_SERVICE=neocare-api \
    DD_ENV=dev \
    DD_VERSION=1.0 \
    DD_SITE=datadoghq.com \
    DD_API_KEY=$DD_API_KEY \
    SPRING_DATASOURCE_URL="jdbc:postgresql://neocare-database.postgres.database.azure.com:5432/neocare?sslmode=require" \
    SPRING_DATASOURCE_USERNAME="neocare_admin" \
    SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD
```

---

## 🌍 12. Acessar aplicação

```bash
az containerapp show \
  --name neocare-api \
  --resource-group neocare \
  --query properties.configuration.ingress.fqdn
```

---

## ⚠️ Observações importantes

- Nunca comite senhas no repositório
- Prefira usar Azure Key Vault em produção
- Firewall aberto apenas para testes

---

## ✅ Próximos passos

- Integrar com Key Vault
- Configurar CI/CD
- Restringir acesso ao banco
