package dev.fpsaraiva.api_assembleia.entity;

import dev.fpsaraiva.api_assembleia.dto.VotoDto;
import dev.fpsaraiva.api_assembleia.enums.VotoEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity(name = "voto")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"pauta_id", "id_associado"})
})
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @NotNull(message = "O id da pauta deve ser preenchido.")
    private Pauta pauta;

    @NotNull(message = "O id do associado deve ser preenchido.")
    @Column(name = "id_associado")
    private UUID idAssociado;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O voto deve ser preenchido")
    private VotoEnum voto;

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

    public UUID getIdAssociado() {
        return idAssociado;
    }

    public void setIdAssociado(UUID idAssociado) {
        this.idAssociado = idAssociado;
    }

    public VotoEnum getVoto() {
        return voto;
    }

    public void setVoto(VotoEnum voto) {
        this.voto = voto;
    }

    public static Voto toModel(VotoDto votoDto) {
        Voto voto = new Voto();
        voto.setPauta(new Pauta(votoDto.idPauta()));
        voto.setIdAssociado(votoDto.idAssociado());
        voto.setVoto(votoDto.voto());
        return voto;
    }
}