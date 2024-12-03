package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.ResultadoVotacaoDto;
import dev.fpsaraiva.api_assembleia.dto.VotoDto;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.service.SessaoService;
import dev.fpsaraiva.api_assembleia.service.VotoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/votos")
public class VotoController {

    @Autowired
    private VotoService votoService;
    @Autowired
    private SessaoService sessaoService;

    @PostMapping
    public ResponseEntity<VotoDto> registrarVoto(
            @Valid @RequestBody VotoDto votoDto
    ) {
        try {
            if (sessaoService.existsById(votoDto.idSessao()).isEmpty()) {
                throw new ApiErroException(HttpStatus.NOT_FOUND, "A Sessão de ID '" + votoDto.idSessao() + "' não existe.");
            }

            if (sessaoService.validarAbertura(votoDto.idSessao())) {
                throw new ApiErroException(HttpStatus.BAD_REQUEST, "Não foi possível votar, pois a Sessão de ID '" + votoDto.idSessao() + "' já foi encerrada para votação.");
            }

            VotoDto voto = votoService.registrarVoto(votoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(voto);
        } catch (DataIntegrityViolationException e) {
            throw new ApiErroException(HttpStatus.BAD_REQUEST,
                    "Não é possível registrar este voto. O associado já votou na pauta desta sessão.");
        }
    }

    @GetMapping("/{idSessao}")
    public ResponseEntity<ResultadoVotacaoDto> contabilizarVotos(
            @PathVariable(name = "idSessao") UUID idSessao
            ) {
        if (sessaoService.existsById(idSessao).isEmpty()) {
            throw new ApiErroException(HttpStatus.NOT_FOUND, "A Sessão de ID '" + idSessao + "' não existe.");
        }

        ResultadoVotacaoDto resultadoVotacaoDto = votoService.getResultadoVotacao(idSessao);
        return ResponseEntity.ok(resultadoVotacaoDto);
    }
}
