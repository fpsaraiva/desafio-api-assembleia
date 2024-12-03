package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.dto.PautaDto;
import dev.fpsaraiva.api_assembleia.entity.Pauta;
import dev.fpsaraiva.api_assembleia.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    public PautaDto criarPauta(PautaDto pautaDto) {
        Pauta pauta = pautaRepository.save(Pauta.toModel(pautaDto));
        return PautaDto.toDto(pauta);
    }

    public boolean existsByTitulo(String titulo) {
        return pautaRepository.existsByTitulo(titulo);
    }
}
