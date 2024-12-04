package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.dto.SessaoDto;
import dev.fpsaraiva.api_assembleia.entity.Pauta;
import dev.fpsaraiva.api_assembleia.entity.Sessao;
import dev.fpsaraiva.api_assembleia.repository.SessaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessaoServiceTests {

    @Mock
    private SessaoRepository sessaoRepository;

    @InjectMocks
    private SessaoService sessaoService;

    @Test
    void testAbrirSessaoComSucesso() {
        SessaoDto sessaoDto = new SessaoDto(UUID.randomUUID(), UUID.randomUUID(), 60);
        Sessao sessao = Sessao.toModel(sessaoDto);
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);

        SessaoDto result = sessaoService.abrirSessao(sessaoDto);

        assertNotNull(result);
        assertEquals(sessao.getId(), result.id());
        verify(sessaoRepository).save(any(Sessao.class));
    }

    @Test
    void testListarSessoesComSucesso() {
        Sessao sessao1 = new Sessao(UUID.randomUUID(), new Pauta(), 60, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60));
        Sessao sessao2 = new Sessao(UUID.randomUUID(), new Pauta(), 30, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        Page<Sessao> sessoesPage = new PageImpl<>(List.of(sessao1, sessao2));
        when(sessaoRepository.findAll(any(Pageable.class))).thenReturn(sessoesPage);

        Pageable pageable = PageRequest.of(0, 10);

        Page<SessaoDto> result = sessaoService.listarSessoes(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(sessaoRepository).findAll(any(Pageable.class));
    }

    @Test
    void testExistsByIdSessaoExistente() {
        UUID sessaoId = UUID.randomUUID();
        Sessao sessao = new Sessao(sessaoId, new Pauta(), 60, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60));
        when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

        Optional<Sessao> result = sessaoService.existsById(sessaoId);

        assertTrue(result.isPresent());
        assertEquals(sessaoId, result.get().getId());
        verify(sessaoRepository).findById(sessaoId);
    }

    @Test
    void testExistsByIdSessaoNaoExistente() {
        UUID sessaoId = UUID.randomUUID();
        when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.empty());

        Optional<Sessao> result = sessaoService.existsById(sessaoId);

        assertTrue(result.isEmpty());
        verify(sessaoRepository).findById(sessaoId);
    }

    @Test
    void testValidarAberturaSessaoAberta() {
        UUID sessaoId = UUID.randomUUID();
        LocalDateTime inicio = LocalDateTime.now().minusMinutes(30);
        LocalDateTime fim = LocalDateTime.now().plusMinutes(30);
        Sessao sessao = new Sessao(sessaoId, new Pauta(), 60, inicio, fim);
        when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

        boolean result = sessaoService.validarAbertura(sessaoId);

        assertFalse(result);
        verify(sessaoRepository).findById(sessaoId);
    }

    @Test
    void testValidarAberturaSessaoEncerrada() {
        UUID sessaoId = UUID.randomUUID();
        LocalDateTime inicio = LocalDateTime.now().minusHours(2);
        LocalDateTime fim = LocalDateTime.now().minusMinutes(30);
        Sessao sessao = new Sessao(sessaoId, new Pauta(), 2, inicio, fim);
        when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessao));

        boolean result = sessaoService.validarAbertura(sessaoId);

        assertTrue(result);
        verify(sessaoRepository).findById(sessaoId);
    }
}
