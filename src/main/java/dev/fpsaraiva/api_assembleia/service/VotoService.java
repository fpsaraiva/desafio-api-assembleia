package dev.fpsaraiva.api_assembleia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fpsaraiva.api_assembleia.dto.ResultadoVotacaoDto;
import dev.fpsaraiva.api_assembleia.dto.VotoDto;
import dev.fpsaraiva.api_assembleia.entity.Voto;
import dev.fpsaraiva.api_assembleia.enums.VotoEnum;
import dev.fpsaraiva.api_assembleia.repository.SessaoRepository;
import dev.fpsaraiva.api_assembleia.repository.VotoRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    private final Logger logger = LoggerFactory.getLogger(Voto.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topicos.assembleia.resultado.votacao.topic}")
    private String topicoAssembleia;

    @Autowired
    private ObjectMapper objectMapper;

    public VotoDto registrarVoto(@Valid VotoDto votoDto) {
        Voto voto = votoRepository.save(Voto.toModel(votoDto));
        return VotoDto.toDto(voto);
    }

    public ResultadoVotacaoDto getResultadoVotacao(UUID idSessao) throws JsonProcessingException {
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

        kafkaTemplate.send(topicoAssembleia, objectMapper.writeValueAsString(resultado));

        logger.info("Resultado de votação da seção 'id={}' CONTABILIZADO com sucesso!", resultado.idSessao());

        return resultado;
    }
}
