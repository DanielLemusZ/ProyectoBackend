package co.edu.autonoma.reservasapi.repository;

import co.edu.autonoma.reservasapi.entity.CoordinadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoordinadorRepository extends JpaRepository<CoordinadorEntity, Integer>
{
    Optional<CoordinadorEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    // Para PUT: el valor único no cuenta al propio coordinador
    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByOrganizacionId(Integer organizacionId);

    List<CoordinadorEntity> findByOrganizacionId(Integer organizacionId);
}
