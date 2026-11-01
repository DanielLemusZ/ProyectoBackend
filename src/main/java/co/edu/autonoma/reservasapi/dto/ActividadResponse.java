package co.edu.autonoma.reservasapi.dto;

import co.edu.autonoma.reservasapi.entity.ActividadEntity;
import co.edu.autonoma.reservasapi.entity.CoordinadorEntity;
import co.edu.autonoma.reservasapi.entity.EstadoActividad;
import lombok.Getter;

import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/actividades
@Getter
public class ActividadResponse
{
    private final Integer id;
    private final Integer organizacionId;
    private final String organizacionNombre;
    private final Integer coordinadorId;
    private final String coordinadorNombre;
    private final String titulo;
    private final String descripcion;
    private final String ubicacion;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaFin;
    private final LocalDateTime fechaCierre;
    private final Integer cupoMaximo;

    // RN-01: inscripciones CONFIRMADA (countByActividadIdAndEstado)
    private final long cuposOcupados;

    // RN-01: cupoMaximo - cuposOcupados
    private final long cuposDisponibles;

    private final EstadoActividad estado;
    private final LocalDateTime fechaCreacion;
    private final LocalDateTime fechaActualizacion;

    public ActividadResponse(
            Integer id,
            Integer organizacionId,
            String organizacionNombre,
            Integer coordinadorId,
            String coordinadorNombre,
            String titulo,
            String descripcion,
            String ubicacion,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            LocalDateTime fechaCierre,
            Integer cupoMaximo,
            long cuposOcupados,
            long cuposDisponibles,
            EstadoActividad estado,
            LocalDateTime fechaCreacion,
            LocalDateTime fechaActualizacion
    )
    {
        this.id = id;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.coordinadorId = coordinadorId;
        this.coordinadorNombre = coordinadorNombre;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaCierre = fechaCierre;
        this.cupoMaximo = cupoMaximo;
        this.cuposOcupados = cuposOcupados;
        this.cuposDisponibles = cuposDisponibles;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Conversión de la entidad a la respuesta; la usa ActividadServicio.
    // cuposOcupados lo cuenta el servicio, porque la entidad no tiene sus inscripciones.
    public static ActividadResponse desde(ActividadEntity actividad, long cuposOcupados)
    {
        CoordinadorEntity coordinador = actividad.getCoordinador();

        return new ActividadResponse(
                actividad.getId(),
                actividad.getOrganizacion().getId(),
                actividad.getOrganizacion().getNombre(),
                coordinador.getId(),
                coordinador.getNombre() + " " + coordinador.getApellido(),
                actividad.getTitulo(),
                actividad.getDescripcion(),
                actividad.getUbicacion(),
                actividad.getFechaInicio(),
                actividad.getFechaFin(),
                actividad.getFechaCierre(),
                actividad.getCupoMaximo(),
                cuposOcupados,
                actividad.getCupoMaximo() - cuposOcupados,
                actividad.getEstado(),
                actividad.getFechaCreacion(),
                actividad.getFechaActualizacion()
        );
    }
}
