package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.dto.PautaDto;
import dev.fpsaraiva.api_assembleia.entity.Pauta;
import dev.fpsaraiva.api_assembleia.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    public Page<PautaDto> listarPautas(Pageable pageable) {
        Page<Pauta> pautasPage = pautaRepository.findAll(pageable);
        return pautasPage
                .map(pauta -> new PautaDto(
                   pauta.getId(),
                   pauta.getTitulo()
                ));
    }

    public Optional<Pauta> existsById(UUID pautaId) {
        return pautaRepository.findById(pautaId);
    }
}
