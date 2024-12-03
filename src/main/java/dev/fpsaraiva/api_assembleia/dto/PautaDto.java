package dev.fpsaraiva.api_assembleia.dto;

import dev.fpsaraiva.api_assembleia.entity.Pauta;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record PautaDto(
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
