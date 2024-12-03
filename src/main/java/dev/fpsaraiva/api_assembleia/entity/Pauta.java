package dev.fpsaraiva.api_assembleia.entity;

import dev.fpsaraiva.api_assembleia.dto.PautaDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity(name = "pauta")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O título da pauta é obrigatório.")
    private String titulo;

    public Pauta() {
    }

    public Pauta(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public static Pauta toModel(PautaDto pautaDto) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(pautaDto.titulo());
        return pauta;
    }
}
