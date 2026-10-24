package co.edu.autonoma.reservasapi.dto;

import co.edu.autonoma.reservasapi.entity.EstadoInscripcion;

import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/inscripciones y de las rutas anidadas
// /api/actividades/{id}/inscripciones y /api/voluntarios/{id}/inscripciones
public class InscripcionResponse
{
    private Integer id;
    private Integer actividadId;
    private String actividadTitulo;
    private Integer voluntarioId;
    private String voluntarioNombre;
    private String claveIdempotencia;
    private EstadoInscripcion estado;
    private LocalDateTime fechaInscripcion;

    // null mientras la inscripción esté CONFIRMADA
    private LocalDateTime fechaCancelacion;

    public InscripcionResponse()
    {

    }

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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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

    public String getClaveIdempotencia()
    {
        return claveIdempotencia;
    }

    public void setClaveIdempotencia(String claveIdempotencia)
    {
        this.claveIdempotencia = claveIdempotencia;
    }

    public EstadoInscripcion getEstado()
    {
        return estado;
    }

    public void setEstado(EstadoInscripcion estado)
    {
        this.estado = estado;
    }

    public LocalDateTime getFechaInscripcion()
    {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion)
    {
        this.fechaInscripcion = fechaInscripcion;
    }

    public LocalDateTime getFechaCancelacion()
    {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(LocalDateTime fechaCancelacion)
    {
        this.fechaCancelacion = fechaCancelacion;
    }
}
