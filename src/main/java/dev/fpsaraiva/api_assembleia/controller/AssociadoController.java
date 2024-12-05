package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.AssociadoDto;
import dev.fpsaraiva.api_assembleia.entity.Associado;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.exception.ErroPadronizado;
import dev.fpsaraiva.api_assembleia.service.AssociadoService;
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

import java.util.UUID;

@Tag(name = "Associado", description = "Operações relacionadas a associados")
@RestController
@RequestMapping("/api/v1/associados")
public class AssociadoController {

    @Autowired
    private AssociadoService associadoService;

    private final Logger logger = LoggerFactory.getLogger(Associado.class);

    @Operation(
            summary = "Cadastrar um associado",
            description = "Cadastrar um novo associado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associado criado com sucesso"),
            @ApiResponse(
                    responseCode = "422",
                    description = "Associado com o mesmo cpf já existente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErroPadronizado.class)))
    })
    @PostMapping
    public ResponseEntity<AssociadoDto> cadastrarAssociado(
            @Valid @RequestBody AssociadoDto associadoDto
    ) {
        if (associadoService.existsByCpf(associadoDto.cpf())) {
            logger.error("ERRO: Associado com cpf 'cpf={} JÁ EXISTE!", associadoDto.cpf());
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Associado com cpf '" + associadoDto.cpf() + "' já existe.");
        }

        AssociadoDto associado = associadoService.cadastrarAssociado(associadoDto);

        logger.info("Associado 'id={} CRIADO com sucesso!", associado.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(associado);
    }

    @Operation(
            summary = "Listar associados",
            description = "Listar todas os associados cadastrados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associados listados com sucesso")
    })
    @GetMapping
    public Page<AssociadoDto> listarAssociados(
            @Parameter(description = "Página selecionada", required = false)
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Número de resultados em uma página", required = false)
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return associadoService.listarAssociados(pageable);
    }

    @Operation(
            summary = "Remover associado",
            description = "Deleta um associado cadastrado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Associado removido com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Associado não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErroPadronizado.class))
            )
    })
    @DeleteMapping("/{idAssociado}")
    public ResponseEntity<Void> removerAssociado(
            @Parameter(description = "ID do associado", required = true)
            @PathVariable(name = "idAssociado") UUID idAssociado) {
        if (associadoService.buscaPorId(idAssociado).isEmpty()) {
            throw new ApiErroException(HttpStatus.NOT_FOUND, "Associado de ID '" + idAssociado + "' não existe.");
        }

        associadoService.deletarPorId(idAssociado);

        logger.info("Associado 'id={}' REMOVIDO com sucesso!", idAssociado);

        return ResponseEntity.noContent().build();
    }
}
