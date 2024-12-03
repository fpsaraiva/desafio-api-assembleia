package dev.fpsaraiva.api_assembleia.dto;

import java.util.UUID;

public record ResultadoVotacaoDto(
        UUID idSessao,
        int totalVotos,
        int sim,
        int nao
) {
}
