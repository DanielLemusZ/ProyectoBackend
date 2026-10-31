package co.edu.autonoma.reservasapi.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/asistencias, de PATCH /api/inscripciones/{id}/asistencia
// y de las rutas anidadas /api/actividades/{id}/asistencias y /api/voluntarios/{id}/asistencias
@Getter
public class AsistenciaResponse
{
    private final Integer id;
    private final Integer inscripcionId;
    private final Integer actividadId;
    private final String actividadTitulo;
    private final Integer voluntarioId;
    private final String voluntarioNombre;
    private final Integer coordinadorId;
    private final String coordinadorNombre;
    private final BigDecimal horas;
    private final String observaciones;
    private final LocalDateTime fechaRegistro;

    public AsistenciaResponse(
            Integer id,
            Integer inscripcionId,
            Integer actividadId,
            String actividadTitulo,
            Integer voluntarioId,
            String voluntarioNombre,
            Integer coordinadorId,
            String coordinadorNombre,
            BigDecimal horas,
            String observaciones,
            LocalDateTime fechaRegistro
    )
    {
        this.id = id;
        this.inscripcionId = inscripcionId;
        this.actividadId = actividadId;
        this.actividadTitulo = actividadTitulo;
        this.voluntarioId = voluntarioId;
        this.voluntarioNombre = voluntarioNombre;
        this.coordinadorId = coordinadorId;
        this.coordinadorNombre = coordinadorNombre;
        this.horas = horas;
        this.observaciones = observaciones;
        this.fechaRegistro = fechaRegistro;
    }
}
