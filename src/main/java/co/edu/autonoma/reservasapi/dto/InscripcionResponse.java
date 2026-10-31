package co.edu.autonoma.reservasapi.dto;

import co.edu.autonoma.reservasapi.entity.EstadoInscripcion;
import lombok.Getter;

import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/inscripciones y de las rutas anidadas
// /api/actividades/{id}/inscripciones y /api/voluntarios/{id}/inscripciones
@Getter
public class InscripcionResponse
{
    private final Integer id;
    private final Integer actividadId;
    private final String actividadTitulo;
    private final Integer voluntarioId;
    private final String voluntarioNombre;
    private final String claveIdempotencia;
    private final EstadoInscripcion estado;
    private final LocalDateTime fechaInscripcion;

    // null mientras la inscripción esté CONFIRMADA
    private final LocalDateTime fechaCancelacion;

    public InscripcionResponse(
            Integer id,
            Integer actividadId,
            String actividadTitulo,
            Integer voluntarioId,
            String voluntarioNombre,
            String claveIdempotencia,
            EstadoInscripcion estado,
            LocalDateTime fechaInscripcion,
            LocalDateTime fechaCancelacion
    )
    {
        this.id = id;
        this.actividadId = actividadId;
        this.actividadTitulo = actividadTitulo;
        this.voluntarioId = voluntarioId;
        this.voluntarioNombre = voluntarioNombre;
        this.claveIdempotencia = claveIdempotencia;
        this.estado = estado;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaCancelacion = fechaCancelacion;
    }
}
