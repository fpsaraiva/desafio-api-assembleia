package dev.fpsaraiva.api_assembleia.controller;

import dev.fpsaraiva.api_assembleia.dto.AssociadoDto;
import dev.fpsaraiva.api_assembleia.entity.Associado;
import dev.fpsaraiva.api_assembleia.exception.ApiErroException;
import dev.fpsaraiva.api_assembleia.service.AssociadoService;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssociadoControllerTests {

    @Mock
    private AssociadoService associadoService;

    @InjectMocks
    private AssociadoController associadoController;

    @Test
    void testCadastrarAssociadoDeveLancarExcecaoQuandoCpfJaExiste() {
        AssociadoDto associadoDto = new AssociadoDto(null, "Maria Oliveira", "98765432100");

        when(associadoService.existsByCpf(associadoDto.cpf())).thenReturn(true);

        ApiErroException exception = assertThrows(ApiErroException.class,
                () -> associadoController.cadastrarAssociado(associadoDto)
        );

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertEquals("Associado com cpf '" + associadoDto.cpf() + "' já existe.", exception.getReason());
        verify(associadoService, times(1)).existsByCpf(associadoDto.cpf());
        verify(associadoService, never()).cadastrarAssociado(any());
    }

    @Test
    void testCadastrarAssociadoDeveRetornarCreatedQuandoCadastroBemSucedido() {
        AssociadoDto associadoDto = new AssociadoDto(null, "João Silva", "12345678901");
        AssociadoDto associadoCriado = new AssociadoDto(UUID.randomUUID(), "João Silva", "12345678901");

        when(associadoService.existsByCpf(associadoDto.cpf())).thenReturn(false);
        when(associadoService.cadastrarAssociado(associadoDto)).thenReturn(associadoCriado);

        ResponseEntity<AssociadoDto> response = associadoController.cadastrarAssociado(associadoDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(associadoCriado, response.getBody());
        verify(associadoService, times(1)).existsByCpf(associadoDto.cpf());
        verify(associadoService, times(1)).cadastrarAssociado(associadoDto);
    }

    @Test
    void testListarAssociadosDeveRetornarPaginaComAssociados() {
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        AssociadoDto associado1 = new AssociadoDto(UUID.randomUUID(), "João Silva", "12345678901");
        AssociadoDto associado2 = new AssociadoDto(UUID.randomUUID(), "Maria Oliveira", "98765432100");
        List<AssociadoDto> associados = List.of(associado1, associado2);

        Page<AssociadoDto> associadoPage = new PageImpl<>(associados, pageable, associados.size());
        when(associadoService.listarAssociados(pageable)).thenReturn(associadoPage);

        Page<AssociadoDto> response = associadoController.listarAssociados(page, size);

        assertNotNull(response);
        assertEquals(2, response.getTotalElements());
        assertEquals(associados, response.getContent());
        verify(associadoService, times(1)).listarAssociados(pageable);
    }

    @Test
    void testListarAssociadosDeveRetornarPaginaVaziaQuandoNaoHouverAssociados() {
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        Page<AssociadoDto> associadoPage = Page.empty(pageable);
        when(associadoService.listarAssociados(pageable)).thenReturn(associadoPage);

        Page<AssociadoDto> response = associadoController.listarAssociados(page, size);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(associadoService, times(1)).listarAssociados(pageable);
    }

    @Test
    void testRemoverAssociadoDeveRetornarNoContentQuandoAssociadoExistir() {
        UUID idAssociado = UUID.randomUUID();
        Associado associado = new Associado();

        when(associadoService.buscaPorId(idAssociado)).thenReturn(Optional.of(associado));

        ResponseEntity<Void> response = associadoController.removerAssociado(idAssociado);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(associadoService, times(1)).buscaPorId(idAssociado);
        verify(associadoService, times(1)).deletarPorId(idAssociado);
    }

    @Test
    void testRemoverAssociadoDeveLancarExcecaoQuandoAssociadoNaoExistir() {
        UUID idAssociado = UUID.randomUUID();
        when(associadoService.buscaPorId(idAssociado)).thenReturn(Optional.empty());

        ApiErroException exception = assertThrows(ApiErroException.class,
                () -> associadoController.removerAssociado(idAssociado));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("Associado de ID '" + idAssociado + "' não existe.", exception.getReason());
        verify(associadoService, times(1)).buscaPorId(idAssociado);
        verify(associadoService, never()).deletarPorId(idAssociado);
    }
}
