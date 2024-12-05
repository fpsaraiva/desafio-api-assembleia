package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.dto.AssociadoDto;
import dev.fpsaraiva.api_assembleia.entity.Associado;
import dev.fpsaraiva.api_assembleia.repository.AssociadoRepository;
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
public class AssociadoServiceTests {

    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private AssociadoService associadoService;

    @Test
    void testCadastrarAssociadoDeveSalvarEretornarDto() {
        AssociadoDto associadoDto = new AssociadoDto(null, "João Silva", "12345678901");
        Associado associado = new Associado();

        when(associadoRepository.save(any(Associado.class))).thenReturn(associado);

        AssociadoDto resultado = associadoService.cadastrarAssociado(associadoDto);

        assertNotNull(resultado);
        verify(associadoRepository, times(1)).save(any(Associado.class));
    }

    @Test
    void testExistsByCpfDeveRetornarTrueQuandoCpfExistir() {
        String cpf = "12345678901";
        when(associadoRepository.existsByCpf(cpf)).thenReturn(true);

        boolean existe = associadoService.existsByCpf(cpf);

        assertTrue(existe);
        verify(associadoRepository, times(1)).existsByCpf(cpf);
    }

    @Test
    void testListarAssociadosDeveRetornarPaginaDeDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Associado> associados = List.of(
                new Associado(),
                new Associado());
        Page<Associado> associadoPage = new PageImpl<>(associados);

        when(associadoRepository.findAll(pageable)).thenReturn(associadoPage);

        Page<AssociadoDto> resultado = associadoService.listarAssociados(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        verify(associadoRepository, times(1)).findAll(pageable);
    }

    @Test
    void testBuscaPorIdDeveRetornarAssociadoQuandoIdExistir() {
        UUID id = UUID.randomUUID();
        Associado associado = new Associado();

        when(associadoRepository.findById(id)).thenReturn(Optional.of(associado));

        Optional<Associado> resultado = associadoService.buscaPorId(id);

        assertTrue(resultado.isPresent());
        verify(associadoRepository, times(1)).findById(id);
    }

    @Test
    void testBuscaPorIdDeveRetornarOptionalVazioQuandoIdNaoExistir() {
        UUID id = UUID.randomUUID();
        when(associadoRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Associado> resultado = associadoService.buscaPorId(id);

        assertTrue(resultado.isEmpty());
        verify(associadoRepository, times(1)).findById(id);
    }

    @Test
    void testDeletarPorIdDeveChamarRepositoryDelete() {
        UUID id = UUID.randomUUID();

        associadoService.deletarPorId(id);

        verify(associadoRepository, times(1)).deleteById(id);
    }
}
