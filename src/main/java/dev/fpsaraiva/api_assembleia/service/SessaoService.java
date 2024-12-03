package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.dto.SessaoDto;
import dev.fpsaraiva.api_assembleia.entity.Sessao;
import dev.fpsaraiva.api_assembleia.repository.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Page<SessaoDto> listarSessoes(Pageable pageable) {
        Page<Sessao> sessoesPage = sessaoRepository.findAll(pageable);
        return sessoesPage
                .map(sessao -> new SessaoDto(
                        sessao.getId(),
                        sessao.getPauta().getId(),
                        sessao.getDuracaoEmMinutos()
                ));
    }

    public Optional<Sessao> existsById(UUID sessaoId) {
        return sessaoRepository.findById(sessaoId);
    }

    public boolean validarAbertura(UUID sessaoId) {
        Optional<Sessao> sessao = sessaoRepository.findById(sessaoId);
        return LocalDateTime.now().isAfter(sessao.get().getFim());
    }
}
