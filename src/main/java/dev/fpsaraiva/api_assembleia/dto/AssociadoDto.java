package dev.fpsaraiva.api_assembleia.dto;

import dev.fpsaraiva.api_assembleia.entity.Associado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AssociadoDto(
        @Schema(description = "ID único do associado. Não necessário no envio da requisição.", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
        UUID id,
        @NotBlank(message = "O nome do associado é obrigatório.")
        String nome,
        @NotNull(message = "O cpf é obrigatório.")
        @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 caracteres.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter apenas números.")
        String cpf
) {
    public static AssociadoDto toDto(Associado associado) {
        return new AssociadoDto(
                associado.getId(),
                associado.getNome(),
                associado.getCpf()
        );
    }
}
