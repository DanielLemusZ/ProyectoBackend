package co.edu.autonoma.reservasapi.repository;

import co.edu.autonoma.reservasapi.entity.OrganizacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizacionRepository extends JpaRepository<OrganizacionEntity, Integer>
{
    Optional<OrganizacionEntity> findByNit(String nit);

    boolean existsByNit(String nit);

    boolean existsByEmail(String email);

    // Para PUT: el valor único no cuenta a la propia organización
    boolean existsByNitAndIdNot(String nit, Integer id);

    boolean existsByEmailAndIdNot(String email, Integer id);
}
