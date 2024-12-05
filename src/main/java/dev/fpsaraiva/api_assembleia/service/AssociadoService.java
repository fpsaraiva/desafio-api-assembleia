package dev.fpsaraiva.api_assembleia.service;

import dev.fpsaraiva.api_assembleia.dto.AssociadoDto;
import dev.fpsaraiva.api_assembleia.entity.Associado;
import dev.fpsaraiva.api_assembleia.repository.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    //@Autowired
    //private ValidaCpf validaCpf;

    public AssociadoDto cadastrarAssociado(AssociadoDto associadoDto) {
        //TODO: valida cpf - URL da api retornando 404
        Associado associado = associadoRepository.save(Associado.toModel(associadoDto));
        return AssociadoDto.toDto(associado);
    }

    public boolean existsByCpf(String cpf) {
        return associadoRepository.existsByCpf(cpf);
    }

    public Page<AssociadoDto> listarAssociados(Pageable pageable) {
        Page<Associado> associadosPage = associadoRepository.findAll(pageable);
        return associadosPage
                .map(associado -> new AssociadoDto(
                        associado.getId(),
                        associado.getNome(),
                        associado.getCpf()
                ));
    }

    public Optional<Associado> buscaPorId(UUID idAssociado) {
        return associadoRepository.findById(idAssociado);
    }

    public void deletarPorId(UUID idAssociado) {
        associadoRepository.deleteById(idAssociado);
    }
}
