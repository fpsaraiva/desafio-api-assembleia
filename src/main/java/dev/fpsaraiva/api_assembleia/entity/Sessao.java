package dev.fpsaraiva.api_assembleia.entity;

import dev.fpsaraiva.api_assembleia.dto.SessaoDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "sessao")
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @NotNull(message = "O id da pauta deve ser preenchido.")
    private Pauta pauta;

    private int duracaoEmMinutos;

    @Column(name = "inicio_sessao")
    private LocalDateTime inicio;

    @Column(name = "fim_sessao")
    private LocalDateTime fim;

    public Sessao() {
    }

    public Sessao(UUID id) {
        this.id = id;
    }

    public Sessao(UUID id, Pauta pauta, int duracaoEmMinutos, LocalDateTime inicio, LocalDateTime fim) {
        this.id = id;
        this.pauta = pauta;
        this.duracaoEmMinutos = duracaoEmMinutos;
        this.inicio = inicio;
        this.fim = fim;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        this.pauta = pauta;
    }

    public int getDuracaoEmMinutos() {
        return duracaoEmMinutos;
    }

    public void setDuracaoEmMinutos(int duracaoEmMinutos) {
        this.duracaoEmMinutos = duracaoEmMinutos;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public static Sessao toModel(SessaoDto sessaoDto) {
        Sessao sessao = new Sessao();
        sessao.setPauta(new Pauta(sessaoDto.idPauta()));
        sessao.setDuracaoEmMinutos(validaDuracaoMinutos(sessaoDto.duracaoEmMinutos()));
        sessao.setInicio(LocalDateTime.now());
        sessao.setFim(LocalDateTime.now().plusMinutes(sessaoDto.duracaoEmMinutos()));
        return sessao;
    }

    public static int validaDuracaoMinutos(int duracaoEmMinutos) {
        return duracaoEmMinutos <= 0 ? 1 : duracaoEmMinutos;
    }
}
