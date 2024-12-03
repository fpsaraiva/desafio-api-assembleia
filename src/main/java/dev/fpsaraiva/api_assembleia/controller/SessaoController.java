package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.SessaoDto;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.service.PautaService;
import dev.fpsaraiva.api_assembleia.service.SessaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;
    @Autowired
    private PautaService pautaService;

    @PostMapping
    public ResponseEntity<SessaoDto> abrirSessao(
            @Valid @RequestBody SessaoDto sessaoDto
    ) {
        if (pautaService.existsById(sessaoDto.idPauta()).isEmpty()) {
            throw new ApiErroException(HttpStatus.NOT_FOUND, "Pauta de ID '" + sessaoDto.idPauta() + "' não existe.");
        }

        SessaoDto sessao = sessaoService.abrirSessao(sessaoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessao);
    }

    @GetMapping
    public Page<SessaoDto> listarSessoes(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return sessaoService.listarSessoes(pageable);
    }
}
