package co.edu.autonoma.reservasapi.repository;

import co.edu.autonoma.reservasapi.entity.AsistenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsistenciaRepository extends JpaRepository<AsistenciaEntity, Integer>
{
    boolean existsByInscripcionId(Integer inscripcionId);

    boolean existsByCoordinadorId(Integer coordinadorId);

    // La actividad se alcanza a través de la inscripción
    boolean existsByInscripcionActividadId(Integer actividadId);
}
