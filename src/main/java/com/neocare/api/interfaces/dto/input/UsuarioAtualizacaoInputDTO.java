package com.neocare.api.interfaces.dto.input;

import com.neocare.api.domain.enums.Sexo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UsuarioAtualizacaoInputDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotBlank(message = "Sobrenome é obrigatório")
        @Size(min = 2, max = 100, message = "Sobrenome deve ter entre 2 e 100 caracteres")
        String sobrenome,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
        String cpf,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
        String telefone,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser no passado")
        LocalDate dataNascimento,

        @NotNull(message = "Sexo é obrigatório")
        Sexo sexo,

        @NotNull(message = "Altura é obrigatória")
        @Min(value = 50, message = "Altura mínima: 50 cm")
        @Max(value = 300, message = "Altura máxima: 300 cm")
        Integer altura,

        @NotNull(message = "Peso é obrigatório")
        @DecimalMin(value = "10.0", message = "Peso mínimo: 10 kg")
        @DecimalMax(value = "500.0", message = "Peso máximo: 500 kg")
        Double peso,

        @NotNull(message = "Endereço é obrigatório")
        @Valid
        EnderecoInputDTO endereco
) {
}
