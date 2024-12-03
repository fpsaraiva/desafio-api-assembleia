package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.client.ValidaCpf;
import dev.fpsaraiva.api_assembleia.dto.ResultadoVotacaoDto;
import dev.fpsaraiva.api_assembleia.dto.VotoDto;
import dev.fpsaraiva.api_assembleia.entity.Voto;
import dev.fpsaraiva.api_assembleia.enums.VotoEnum;
import dev.fpsaraiva.api_assembleia.repository.SessaoRepository;
import dev.fpsaraiva.api_assembleia.repository.VotoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    //@Autowired
    //private ValidaCpf validaCpf;

    public VotoDto registrarVoto(@Valid VotoDto votoDto) {
        //TODO: valida cpf - URL da api retornando 404
        Voto voto = votoRepository.save(Voto.toModel(votoDto));
        return VotoDto.toDto(voto);
    }

    public ResultadoVotacaoDto getResultadoVotacao(UUID idSessao) {
        List<Voto> votos = votoRepository.findBySessao_Id(idSessao);

        int totalVotos = votos.size();
        int votosSim = 0;
        int votosNao = 0;

        for (Voto voto : votos) {
            votosSim += voto.getVoto().equals(VotoEnum.SIM) ? 1 : 0;
            votosNao += voto.getVoto().equals(VotoEnum.SIM) ? 0 : 1;
        }

        ResultadoVotacaoDto resultado = new ResultadoVotacaoDto(
            idSessao,
            totalVotos,
            votosSim,
            votosNao
        );

        return resultado;
    }
}
