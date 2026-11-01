package co.edu.autonoma.reservasapi.repository;

import co.edu.autonoma.reservasapi.entity.AsistenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsistenciaRepository extends JpaRepository<AsistenciaEntity, Integer>
{
    boolean existsByInscripcionId(Integer inscripcionId);

    boolean existsByCoordinadorId(Integer coordinadorId);

    // La actividad se alcanza a través de la inscripción
    boolean existsByInscripcionActividadId(Integer actividadId);

    List<AsistenciaEntity> findByInscripcionActividadId(Integer actividadId);

    // El voluntario también se alcanza a través de la inscripción
    List<AsistenciaEntity> findByInscripcionVoluntarioId(Integer voluntarioId);
}
