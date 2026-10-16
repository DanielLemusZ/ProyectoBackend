package co.edu.autonoma.reservasapi.repository;

import co.edu.autonoma.reservasapi.entity.EstadoInscripcion;
import co.edu.autonoma.reservasapi.entity.InscripcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<InscripcionEntity, Integer>
{
    Optional<InscripcionEntity> findByClaveIdempotencia(String claveIdempotencia);

    long countByActividadIdAndEstado(Integer actividadId, EstadoInscripcion estado);

    boolean existsByActividadIdAndVoluntarioIdAndEstado(Integer actividadId, Integer voluntarioId, EstadoInscripcion estado);
}
