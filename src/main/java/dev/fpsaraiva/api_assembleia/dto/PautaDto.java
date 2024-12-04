package dev.fpsaraiva.api_assembleia.dto;

import dev.fpsaraiva.api_assembleia.entity.Pauta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record PautaDto(
        @Schema(description = "ID único da pauta. Não necessário no envio da requisição.", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
        UUID id,
        @NotBlank(message = "O título da pauta é obrigatório")
        String titulo
) {
    public static PautaDto toDto(Pauta pauta) {
        return new PautaDto(
                pauta.getId(),
                pauta.getTitulo()
        );
    }
}
