package co.edu.autonoma.reservasapi.repository;

import co.edu.autonoma.reservasapi.entity.VoluntarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoluntarioRepository extends JpaRepository<VoluntarioEntity, Integer>
{
    Optional<VoluntarioEntity> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    boolean existsByEmail(String email);

    // Para PUT: el valor único no cuenta al propio voluntario
    boolean existsByDocumentoAndIdNot(String documento, Integer id);

    boolean existsByEmailAndIdNot(String email, Integer id);
}
