package dev.fpsaraiva.api_assembleia.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fpsaraiva.api_assembleia.dto.ResultadoVotacaoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResultadoVotacaoProducer {

    @Value("${topicos.assembleia.resultado.votacao.topic}")
    private String topicoAssembleia;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ResultadoVotacaoDto resultadoVotacaoDto) throws JsonProcessingException {
        String resultadoAsMessage = objectMapper.writeValueAsString(resultadoVotacaoDto);
        kafkaTemplate.send(topicoAssembleia, resultadoAsMessage);
    }
}
