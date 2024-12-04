package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.SessaoDto;
import dev.fpsaraiva.api_assembleia.entity.Pauta;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.service.PautaService;
import dev.fpsaraiva.api_assembleia.service.SessaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessaoControllerTests {

    @Mock
    private SessaoService sessaoService;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private SessaoController sessaoController;

    @Test
    void testAbrirSessaoComSucesso() {
        UUID idPauta = UUID.randomUUID();
        SessaoDto sessaoDto = new SessaoDto(UUID.randomUUID(), idPauta, 60);
        when(pautaService.existsById(idPauta)).thenReturn(Optional.of(new Pauta()));
        when(sessaoService.abrirSessao(sessaoDto)).thenReturn(sessaoDto);

        ResponseEntity<SessaoDto> response = sessaoController.abrirSessao(sessaoDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sessaoDto, response.getBody());
        verify(pautaService).existsById(idPauta);
        verify(sessaoService).abrirSessao(sessaoDto);
    }

    @Test
    void testAbrirSessaoPautaNaoEncontrada() {
        UUID idPauta = UUID.randomUUID();
        SessaoDto sessaoDto = new SessaoDto(UUID.randomUUID(), idPauta, 60);
        when(pautaService.existsById(idPauta)).thenReturn(Optional.empty());

        ApiErroException exception = assertThrows(ApiErroException.class,
                () -> sessaoController.abrirSessao(sessaoDto));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("Pauta de ID '" + idPauta + "' não existe.", exception.getReason());
        verify(pautaService).existsById(idPauta);
        verifyNoInteractions(sessaoService);
    }

    @Test
    void testListarSessoesComSucesso() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        SessaoDto sessaoDto1 = new SessaoDto(UUID.randomUUID(), UUID.randomUUID(), 60);
        SessaoDto sessaoDto2 = new SessaoDto(UUID.randomUUID(), UUID.randomUUID(), 30);

        Page<SessaoDto> sessoesPage = new PageImpl<>(List.of(sessaoDto1, sessaoDto2));
        when(sessaoService.listarSessoes(pageable)).thenReturn(sessoesPage);

        Page<SessaoDto> result = sessaoController.listarSessoes(page, size);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(sessaoDto1));
        assertTrue(result.getContent().contains(sessaoDto2));
        verify(sessaoService).listarSessoes(pageable);
    }

    @Test
    void testListarSessoesPaginaVazia() {
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<SessaoDto> sessoesPage = new PageImpl<>(Collections.emptyList());
        when(sessaoService.listarSessoes(pageable)).thenReturn(sessoesPage);

        Page<SessaoDto> result = sessaoController.listarSessoes(page, size);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(sessaoService).listarSessoes(pageable);
    }
}
