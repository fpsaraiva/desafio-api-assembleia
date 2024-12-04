package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.VotoDto;
import dev.fpsaraiva.api_assembleia.entity.Sessao;
import dev.fpsaraiva.api_assembleia.enums.VotoEnum;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.service.SessaoService;
import dev.fpsaraiva.api_assembleia.service.VotoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VotoControllerTests {

    @Mock
    private SessaoService sessaoService;

    @Mock
    private VotoService votoService;

    @InjectMocks
    private VotoController votoController;

    @Test
    void testSessaoNaoEncontrada() {
        when(sessaoService.existsById(any())).thenReturn(Optional.empty());

        VotoDto votoDto = new VotoDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), VotoEnum.SIM);

        ApiErroException exception = assertThrows(ApiErroException.class,
                () -> votoController.registrarVoto(votoDto));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void testSessaoEncerrada() {
        when(sessaoService.existsById(any())).thenReturn(Optional.of(new Sessao()));
        when(sessaoService.validarAbertura(any())).thenReturn(true);

        VotoDto votoDto = new VotoDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), VotoEnum.SIM);

        ApiErroException exception = assertThrows(ApiErroException.class,
                () -> votoController.registrarVoto(votoDto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void testRegistrarVotoComSucesso() {
        VotoDto votoDto = new VotoDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), VotoEnum.SIM);
        when(sessaoService.existsById(any())).thenReturn(Optional.of(new Sessao()));
        when(sessaoService.validarAbertura(any())).thenReturn(false);
        when(votoService.registrarVoto(any())).thenReturn(votoDto);

        ResponseEntity<VotoDto> response = votoController.registrarVoto(votoDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(votoDto, response.getBody());
    }

    @Test
    void testVotoDuplicado() {
        VotoDto votoDto = new VotoDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), VotoEnum.SIM);
        when(sessaoService.existsById(any())).thenReturn(Optional.of(new Sessao()));
        when(sessaoService.validarAbertura(any())).thenReturn(false);
        when(votoService.registrarVoto(any())).thenThrow(new DataIntegrityViolationException(""));

        ApiErroException exception = assertThrows(ApiErroException.class,
                () -> votoController.registrarVoto(votoDto));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }
}
