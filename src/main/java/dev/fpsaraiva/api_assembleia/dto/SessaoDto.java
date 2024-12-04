package dev.fpsaraiva.api_assembleia.dto;

import dev.fpsaraiva.api_assembleia.entity.Sessao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SessaoDto(
        @Schema(description = "ID único da sessão. Não necessário no envio da requisição.", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
        UUID id,
        @NotNull(message = "O id da pauta deve ser preenchido.")
        UUID idPauta,
        @Schema(description = "Duração da sessão em minutos", example = "1")
        int duracaoEmMinutos
) {
    public static SessaoDto toDto(Sessao sessao) {
        return new SessaoDto(
                sessao.getId(),
                sessao.getPauta().getId(),
                sessao.getDuracaoEmMinutos()
        );
    }
}
