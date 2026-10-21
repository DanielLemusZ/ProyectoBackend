package co.edu.autonoma.reservasapi.repository;

import co.edu.autonoma.reservasapi.entity.ActividadEntity;
import co.edu.autonoma.reservasapi.entity.EstadoActividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActividadRepository extends JpaRepository<ActividadEntity, Integer>
{
    List<ActividadEntity> findByEstado(EstadoActividad estado);

    long countByEstado(EstadoActividad estado);

    List<ActividadEntity> findByFechaCierreAfter(LocalDateTime fecha);
}
