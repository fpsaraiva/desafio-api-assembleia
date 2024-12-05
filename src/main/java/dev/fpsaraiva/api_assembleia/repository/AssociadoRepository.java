package dev.fpsaraiva.api_assembleia.repository;

import dev.fpsaraiva.api_assembleia.entity.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, UUID> {
    boolean existsByCpf(String cpf);
}
