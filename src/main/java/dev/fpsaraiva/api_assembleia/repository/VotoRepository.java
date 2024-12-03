package dev.fpsaraiva.api_assembleia.repository;

import dev.fpsaraiva.api_assembleia.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VotoRepository extends JpaRepository<Voto, UUID> {
}
