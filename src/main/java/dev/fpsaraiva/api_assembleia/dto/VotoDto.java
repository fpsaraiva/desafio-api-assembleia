package dev.fpsaraiva.api_assembleia.dto;

import dev.fpsaraiva.api_assembleia.entity.Voto;
import dev.fpsaraiva.api_assembleia.enums.VotoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VotoDto(
        @Schema(description = "ID único do voto. Não necessário no envio da requisição.", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
        UUID id,
        @NotNull(message = "O id da sessao deve ser preenchido.")
        UUID idSessao,
        @NotNull(message = "O id do associado deve ser preenchido.")
        UUID idAssociado,
        @NotNull(message = "O voto deve ser preenchido")
        VotoEnum voto

) {
    public static VotoDto toDto(Voto voto) {
        return new VotoDto(
                voto.getId(),
                voto.getSessao().getId(),
                voto.getIdAssociado(),
                voto.getVoto()
        );
    }
}
