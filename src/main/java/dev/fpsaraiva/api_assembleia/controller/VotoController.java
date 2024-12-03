package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.VotoDto;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.service.PautaService;
import dev.fpsaraiva.api_assembleia.service.VotoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votos")
public class VotoController {

    @Autowired
    private VotoService votoService;
    @Autowired
    private PautaService pautaService;

    @PostMapping
    public ResponseEntity<VotoDto> registrarVoto(
            @Valid @RequestBody VotoDto votoDto
    ) {
        try {
            if (pautaService.existsById(votoDto.idPauta()).isEmpty()) {
                throw new ApiErroException(HttpStatus.NOT_FOUND, "Pauta de ID '" + votoDto.idPauta() + "' não existe.");
            }

            VotoDto voto = votoService.registrarVoto(votoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(voto);
        } catch (DataIntegrityViolationException e) {
            throw new ApiErroException(HttpStatus.BAD_REQUEST,
                    "Não é possível registrar este voto. O associado já votou nesta pauta.");
        }
    }
}
