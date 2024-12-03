package dev.fpsaraiva.api_assembleia.dto;

import dev.fpsaraiva.api_assembleia.entity.Sessao;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SessaoDto(
        UUID id,
        @NotNull(message = "O id da pauta deve ser preenchido.")
        UUID idPauta,
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
