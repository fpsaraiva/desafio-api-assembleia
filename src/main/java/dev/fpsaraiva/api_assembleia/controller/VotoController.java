package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.ResultadoVotacaoDto;
import dev.fpsaraiva.api_assembleia.dto.VotoDto;
import dev.fpsaraiva.api_assembleia.entity.Voto;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.exception.ErroPadronizado;
import dev.fpsaraiva.api_assembleia.service.SessaoService;
import dev.fpsaraiva.api_assembleia.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Voto", description = "Operações relacionadas às votações")
@RestController
@RequestMapping("/votos")
public class VotoController {

    @Autowired
    private VotoService votoService;

    @Autowired
    private SessaoService sessaoService;

    private final Logger logger = LoggerFactory.getLogger(Voto.class);

    @Operation(
            summary = "Registrar votos",
            description = "Receber votos dos associados em uma sessão."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sessão encerrada ou voto já realizado pelo associado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErroPadronizado.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sessão não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErroPadronizado.class))
            )
    })
    @PostMapping
    public ResponseEntity<VotoDto> registrarVoto(
            @Valid @RequestBody VotoDto votoDto
    ) {
        try {
            if (sessaoService.existsById(votoDto.idSessao()).isEmpty()) {
                logger.error("ERRO: Sessão com ID 'id={}' NÃO EXISTE!", votoDto.idSessao());
                throw new ApiErroException(HttpStatus.NOT_FOUND, "A Sessão de ID '" + votoDto.idSessao() + "' não existe.");
            }

            if (sessaoService.validarAbertura(votoDto.idSessao())) {
                logger.error("ERRO: Sessão com ID 'id={}' JÁ ENCERRADA!", votoDto.idSessao());
                throw new ApiErroException(HttpStatus.BAD_REQUEST, "Não foi possível votar, pois a Sessão de ID '" + votoDto.idSessao() + "' já foi encerrada para votação.");
            }

            VotoDto voto = votoService.registrarVoto(votoDto);

            logger.info("Voto 'id={}' REGISTRADO com sucesso!", voto.id());

            return ResponseEntity.status(HttpStatus.CREATED).body(voto);
        } catch (DataIntegrityViolationException e) {
            logger.error("ERRO: Associado já votou na pauta desta sessão");
            throw new ApiErroException(HttpStatus.BAD_REQUEST,
                    "Não é possível registrar este voto. O associado já votou na pauta desta sessão.");
        }
    }

    @Operation(
            summary = "Contabilizar votos",
            description = "Contabilizar todos os votos e fornecer resultado da votação."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Votos contabilizados com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sessão não encontrada",
                    content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErroPadronizado.class))
            )
    })
    @GetMapping("/{idSessao}")
    public ResponseEntity<ResultadoVotacaoDto> contabilizarVotos(
            @Parameter(description = "ID da sessão para buscar votos", required = true)
            @PathVariable(name = "idSessao") UUID idSessao
            ) {
        if (sessaoService.existsById(idSessao).isEmpty()) {
            throw new ApiErroException(HttpStatus.NOT_FOUND, "A Sessão de ID '" + idSessao + "' não existe.");
        }

        ResultadoVotacaoDto resultadoVotacaoDto = votoService.getResultadoVotacao(idSessao);
        return ResponseEntity.ok(resultadoVotacaoDto);
    }
}
