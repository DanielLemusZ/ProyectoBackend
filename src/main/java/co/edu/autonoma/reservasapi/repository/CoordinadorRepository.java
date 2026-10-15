package co.edu.autonoma.reservasapi.repository;

import co.edu.autonoma.reservasapi.entity.CoordinadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordinadorRepository extends JpaRepository<CoordinadorEntity, Integer>
{
    Optional<CoordinadorEntity> findByEmail(String email);
}
