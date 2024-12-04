package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.dto.PautaDto;
import dev.fpsaraiva.api_assembleia.entity.Pauta;
import dev.fpsaraiva.api_assembleia.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTests {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    void testCriarPauta() {
        PautaDto pautaDto = new PautaDto(UUID.randomUUID(), "Título de Teste");
        Pauta pautaMock = new Pauta();
        pautaMock.setTitulo(pautaDto.titulo());

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaMock);

        PautaDto resultado = pautaService.criarPauta(pautaDto);

        assertNotNull(resultado);
        assertEquals(pautaDto.titulo(), resultado.titulo());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    void testExistsByTitulo() {
        String titulo = "Título de Teste";

        when(pautaRepository.existsByTitulo(titulo)).thenReturn(true);

        boolean exists = pautaService.existsByTitulo(titulo);

        assertTrue(exists);
        verify(pautaRepository, times(1)).existsByTitulo(titulo);
    }

    @Test
    void testListarPautas() {
        Pageable pageable = PageRequest.of(0, 10);
        Pauta pautaMock = new Pauta();
        Page<Pauta> pagePautas = new PageImpl<>(List.of(pautaMock));

        when(pautaRepository.findAll(pageable)).thenReturn(pagePautas);

        Page<PautaDto> resultado = pautaService.listarPautas(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(pautaMock.getTitulo(), resultado.getContent().get(0).titulo());
        verify(pautaRepository, times(1)).findAll(pageable);
    }

    @Test
    void testExistsById() {
        UUID pautaId = UUID.randomUUID();
        Pauta pautaMock = new Pauta();

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pautaMock));

        Optional<Pauta> resultado = pautaService.existsById(pautaId);

        assertTrue(resultado.isPresent());
        assertEquals(pautaMock, resultado.get());
        verify(pautaRepository, times(1)).findById(pautaId);
    }
}
