package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.SessaoDto;
import dev.fpsaraiva.api_assembleia.entity.Sessao;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.exception.ErroPadronizado;
import dev.fpsaraiva.api_assembleia.service.PautaService;
import dev.fpsaraiva.api_assembleia.service.SessaoService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Sessão", description = "Operações relacionadas às sessões")
@RestController
@RequestMapping("/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private PautaService pautaService;

    private final Logger logger = LoggerFactory.getLogger(Sessao.class);

    @Operation(
            summary = "Abrir uma sessão de votação",
            description = "Abrir uma sessão de votação em uma pauta."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão aberta com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pauta não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErroPadronizado.class)))
    })
    @PostMapping
    public ResponseEntity<SessaoDto> abrirSessao(
            @Valid @RequestBody SessaoDto sessaoDto
    ) {
        if (pautaService.existsById(sessaoDto.idPauta()).isEmpty()) {
            logger.error("ERRO: Pauta com ID 'id={}' NÃO EXISTE!", sessaoDto.idPauta());
            throw new ApiErroException(HttpStatus.NOT_FOUND, "Pauta de ID '" + sessaoDto.idPauta() + "' não existe.");
        }

        SessaoDto sessao = sessaoService.abrirSessao(sessaoDto);

        logger.info("Sessão 'id={}' CRIADA com sucesso!", sessao.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(sessao);
    }

    @Operation(
            summary = "Listar sessões",
            description = "Listar todas as sessões de votação."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessões listadas com sucesso")
    })
    @GetMapping
    public Page<SessaoDto> listarSessoes(
            @Parameter(description = "Página selecionada", required = false)
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Número de resultados em uma página", required = false)
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return sessaoService.listarSessoes(pageable);
    }
}
