package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.PautaDto;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.service.PautaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaControllerTests {

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private PautaController pautaController;

    @Test
    void testCriarPautaComSucesso() {
        PautaDto pautaDto = new PautaDto(UUID.randomUUID(), "Nova Pauta");
        when(pautaService.existsByTitulo(pautaDto.titulo())).thenReturn(false);
        when(pautaService.criarPauta(pautaDto)).thenReturn(pautaDto);

        ResponseEntity<PautaDto> response = pautaController.criarPauta(pautaDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pautaDto, response.getBody());
        verify(pautaService).existsByTitulo(pautaDto.titulo());
        verify(pautaService).criarPauta(pautaDto);
    }

    @Test
    void testCriarPautaTituloDuplicado() {
        PautaDto pautaDto = new PautaDto(UUID.randomUUID(), "Pauta Duplicada");
        when(pautaService.existsByTitulo(pautaDto.titulo())).thenReturn(true);

        ApiErroException exception = assertThrows(ApiErroException.class,
                () -> pautaController.criarPauta(pautaDto)
        );

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertEquals("Pauta com título 'Pauta Duplicada' já existe.", exception.getReason());
        verify(pautaService).existsByTitulo(pautaDto.titulo());
        verify(pautaService, never()).criarPauta(any());
    }
}
