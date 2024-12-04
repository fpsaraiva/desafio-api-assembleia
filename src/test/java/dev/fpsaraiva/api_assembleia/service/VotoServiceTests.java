package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.dto.ResultadoVotacaoDto;
import dev.fpsaraiva.api_assembleia.dto.VotoDto;
import dev.fpsaraiva.api_assembleia.entity.Sessao;
import dev.fpsaraiva.api_assembleia.entity.Voto;
import dev.fpsaraiva.api_assembleia.enums.VotoEnum;
import dev.fpsaraiva.api_assembleia.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class VotoServiceTests {

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private VotoService votoService;

    public VotoServiceTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrarVoto() {
        VotoDto votoDto = new VotoDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                VotoEnum.SIM
        );

        Voto votoSalvo = new Voto(
                UUID.randomUUID(),
                new Sessao(votoDto.idSessao()),
                votoDto.idAssociado(),
                votoDto.voto()
        );

        when(votoRepository.save(any(Voto.class))).thenReturn(votoSalvo);

        VotoDto votoDtoResultado = votoService.registrarVoto(votoDto);

        assertEquals(votoSalvo.getId(), votoDtoResultado.id());
        assertEquals(votoSalvo.getSessao().getId(), votoDtoResultado.idSessao());
        assertEquals(votoSalvo.getIdAssociado(), votoDtoResultado.idAssociado());
        assertEquals(votoSalvo.getVoto(), votoDtoResultado.voto());
    }

    @Test
    public void testGetResultadoVotacao() {
        UUID idSessao = UUID.randomUUID();

        List<Voto> votosMock = List.of(
                new Voto(UUID.randomUUID(), new Sessao(idSessao), UUID.randomUUID(), VotoEnum.SIM),
                new Voto(UUID.randomUUID(), new Sessao(idSessao), UUID.randomUUID(), VotoEnum.SIM),
                new Voto(UUID.randomUUID(), new Sessao(idSessao), UUID.randomUUID(), VotoEnum.NAO)
        );

        when(votoRepository.findBySessao_Id(idSessao)).thenReturn(votosMock);

        ResultadoVotacaoDto resultado = votoService.getResultadoVotacao(idSessao);

        assertEquals(idSessao, resultado.idSessao());
        assertEquals(3, resultado.totalVotos());
        assertEquals(2, resultado.sim());
        assertEquals(1, resultado.nao());

        verify(votoRepository, times(1)).findBySessao_Id(idSessao);
    }

    @Test
    void testGetResultadoVotacaoSemVotos() {
        UUID idSessao = UUID.randomUUID();

        when(votoRepository.findBySessao_Id(idSessao)).thenReturn(List.of());

        ResultadoVotacaoDto resultado = votoService.getResultadoVotacao(idSessao);

        assertEquals(idSessao, resultado.idSessao());
        assertEquals(0, resultado.totalVotos());
        assertEquals(0, resultado.sim());
        assertEquals(0, resultado.nao());

        verify(votoRepository, times(1)).findBySessao_Id(idSessao);
    }
}
