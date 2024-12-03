package dev.fpsaraiva.api_assembleia.dto;

import dev.fpsaraiva.api_assembleia.entity.Voto;
import dev.fpsaraiva.api_assembleia.enums.VotoEnum;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VotoDto(
        UUID id,
        @NotNull(message = "O id da pauta deve ser preenchido.")
        UUID idPauta,
        @NotNull(message = "O id do associado deve ser preenchido.")
        UUID idAssociado,
        @NotNull(message = "O voto deve ser preenchido")
        VotoEnum voto

) {
    public static VotoDto toDto(Voto voto) {
        return new VotoDto(
                voto.getId(),
                voto.getPauta().getId(),
                voto.getIdAssociado(),
                voto.getVoto()
        );
    }
}
