package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.dto.SessaoDto;
import dev.fpsaraiva.api_assembleia.entity.Sessao;
import dev.fpsaraiva.api_assembleia.repository.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SessaoService {

    @Autowired
    private SessaoRepository sessaoRepository;

    public SessaoDto abrirSessao(SessaoDto sessaoDto) {
        Sessao sessao = sessaoRepository.save(Sessao.toModel(sessaoDto));
        return SessaoDto.toDto(sessao);
    }

    public Optional<Sessao> existsById(UUID pautaId) {
        return sessaoRepository.findById(pautaId);
    }
}
