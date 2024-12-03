package dev.fpsaraiva.api_assembleia.repository;

import dev.fpsaraiva.api_assembleia.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, UUID> {
    boolean existsByTitulo(String titulo);
}
