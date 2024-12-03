package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.PautaDto;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.service.PautaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @PostMapping
    public ResponseEntity<PautaDto> criarPauta(
            @Valid @RequestBody PautaDto pautaDto
    ) {
        if (pautaService.existsByTitulo(pautaDto.titulo())) {
            throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Pauta com título '" + pautaDto.titulo() + "' já existe.");
        }

        PautaDto pauta = pautaService.criarPauta(pautaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @GetMapping
    public Page<PautaDto> listarPautas(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return pautaService.listarPautas(pageable);
    }
}
