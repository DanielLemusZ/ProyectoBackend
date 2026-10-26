package co.edu.autonoma.reservasapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/asistencias, de PATCH /api/inscripciones/{id}/asistencia
// y de las rutas anidadas /api/actividades/{id}/asistencias y /api/voluntarios/{id}/asistencias
public class AsistenciaResponse
{
    private Integer id;
    private Integer inscripcionId;
    private Integer actividadId;
    private String actividadTitulo;
    private Integer voluntarioId;
    private String voluntarioNombre;
    private Integer coordinadorId;
    private String coordinadorNombre;
    private BigDecimal horas;
    private String observaciones;
    private LocalDateTime fechaRegistro;

    public AsistenciaResponse()
    {

    }

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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getInscripcionId()
    {
        return inscripcionId;
    }

    public void setInscripcionId(Integer inscripcionId)
    {
        this.inscripcionId = inscripcionId;
    }

    public Integer getActividadId()
    {
        return actividadId;
    }

    public void setActividadId(Integer actividadId)
    {
        this.actividadId = actividadId;
    }

    public String getActividadTitulo()
    {
        return actividadTitulo;
    }

    public void setActividadTitulo(String actividadTitulo)
    {
        this.actividadTitulo = actividadTitulo;
    }

    public Integer getVoluntarioId()
    {
        return voluntarioId;
    }

    public void setVoluntarioId(Integer voluntarioId)
    {
        this.voluntarioId = voluntarioId;
    }

    public String getVoluntarioNombre()
    {
        return voluntarioNombre;
    }

    public void setVoluntarioNombre(String voluntarioNombre)
    {
        this.voluntarioNombre = voluntarioNombre;
    }

    public Integer getCoordinadorId()
    {
        return coordinadorId;
    }

    public void setCoordinadorId(Integer coordinadorId)
    {
        this.coordinadorId = coordinadorId;
    }

    public String getCoordinadorNombre()
    {
        return coordinadorNombre;
    }

    public void setCoordinadorNombre(String coordinadorNombre)
    {
        this.coordinadorNombre = coordinadorNombre;
    }

    public BigDecimal getHoras()
    {
        return horas;
    }

    public void setHoras(BigDecimal horas)
    {
        this.horas = horas;
    }

    public String getObservaciones()
    {
        return observaciones;
    }

    public void setObservaciones(String observaciones)
    {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaRegistro()
    {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro)
    {
        this.fechaRegistro = fechaRegistro;
    }
}
