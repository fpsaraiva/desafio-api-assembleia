package dev.fpsaraiva.api_assembleia.entity;

import dev.fpsaraiva.api_assembleia.dto.AssociadoDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;

import java.util.UUID;

@Entity(name = "associado")
public class Associado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O nome do associado é obrigatório.")
    private String nome;

    @NotNull(message = "O cpf é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 caracteres.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter apenas números.")
    private String cpf;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public static Associado toModel(AssociadoDto associadoDto) {
        Associado associado = new Associado();
        associado.setNome(associadoDto.nome());
        associado.setCpf(associadoDto.cpf());
        return associado;
    }
}
