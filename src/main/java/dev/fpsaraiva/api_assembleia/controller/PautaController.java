package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.PautaDto;
import dev.fpsaraiva.api_assembleia.entity.Pauta;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.exception.ErroPadronizado;
import dev.fpsaraiva.api_assembleia.service.PautaService;
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

@Tag(name = "Pauta", description = "Operações relacionadas às pautas")
@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    private final Logger logger = LoggerFactory.getLogger(Pauta.class);

    @Operation(
            summary = "Cadastrar uma pauta",
            description = "Cadastrar uma nova pauta."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
            @ApiResponse(
                    responseCode = "422",
                    description = "Pauta com o mesmo título já existente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErroPadronizado.class)))
    })
    @PostMapping
    public ResponseEntity<PautaDto> criarPauta(
            @Valid @RequestBody PautaDto pautaDto
    ) {
        if (pautaService.existsByTitulo(pautaDto.titulo())) {
            logger.error("ERRO: Pauta com título 'titulo={}' JÁ EXISTE!", pautaDto.titulo());
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Pauta com título '" + pautaDto.titulo() + "' já existe.");
        }

        PautaDto pauta = pautaService.criarPauta(pautaDto);

        logger.info("Pauta 'id={}' CRIADA com sucesso!", pauta.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @Operation(
            summary = "Listar pautas",
            description = "Listar todas as pautas cadastradas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pautas listadas com sucesso")
    })
    @GetMapping
    public Page<PautaDto> listarPautas(
            @Parameter(description = "Página selecionada", required = false)
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Número de resultados em uma página", required = false)
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return pautaService.listarPautas(pageable);
    }
}
