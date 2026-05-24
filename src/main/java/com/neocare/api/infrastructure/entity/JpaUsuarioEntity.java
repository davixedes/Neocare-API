package com.neocare.api.infrastructure.entity;

import com.neocare.api.domain.enums.Sexo;
import com.neocare.api.domain.model.Endereco;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "usuario")
@Entity
public class JpaUsuarioEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;

    private String telefone;

    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private Integer altura;

    private Double peso;

    @Embedded
    private Endereco endereco;

    private Boolean ativo;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "credenciais_id", nullable = false)
    private JpaCredenciaisEntity credenciais;

    public JpaUsuarioEntity() {
    }

    public JpaUsuarioEntity(String nome, String sobrenome, String cpf, String email, String telefone, LocalDate dataNascimento, Sexo sexo, Integer altura, Double peso, Endereco endereco, Boolean ativo) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.altura = altura;
        this.peso = peso;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    public JpaUsuarioEntity(Long id, String nome, String sobrenome, String cpf, String email, String telefone, LocalDate dataNascimento, Sexo sexo, Integer altura, Double peso, Endereco endereco, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.altura = altura;
        this.peso = peso;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public Integer getAltura() {
        return altura;
    }

    public Double getPeso() {
        return peso;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setSexo(Sexo sexo){
        this.sexo = Objects.requireNonNullElse(sexo, Sexo.NAO_INFORMADO);
    }

    public void setAltura(Integer altura){
        this.altura = altura;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public JpaCredenciaisEntity getCredenciais() {
        return credenciais;
    }

    public void setCredenciais(JpaCredenciaisEntity credenciais) {
        this.credenciais = credenciais;
    }
}
