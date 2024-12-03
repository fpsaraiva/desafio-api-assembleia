package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.dto.VotoDto;
import dev.fpsaraiva.api_assembleia.entity.Voto;
import dev.fpsaraiva.api_assembleia.repository.VotoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    @Autowired
    public VotoRepository votoRepository;

    public VotoDto registrarVoto(@Valid VotoDto votoDto) {
        Voto voto = votoRepository.save(Voto.toModel(votoDto));
        return VotoDto.toDto(voto);
    }
}
