package co.edu.autonoma.reservasapi.dto;

import co.edu.autonoma.reservasapi.entity.EstadoActividad;

import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/actividades
public class ActividadResponse
{
    private Integer id;
    private Integer organizacionId;
    private String organizacionNombre;
    private Integer coordinadorId;
    private String coordinadorNombre;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LocalDateTime fechaCierre;
    private Integer cupoMaximo;

    // RN-01: inscripciones CONFIRMADA (countByActividadIdAndEstado)
    private long cuposOcupados;

    // RN-01: cupoMaximo - cuposOcupados
    private long cuposDisponibles;

    private EstadoActividad estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public ActividadResponse()
    {

    }

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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getOrganizacionId()
    {
        return organizacionId;
    }

    public void setOrganizacionId(Integer organizacionId)
    {
        this.organizacionId = organizacionId;
    }

    public String getOrganizacionNombre()
    {
        return organizacionNombre;
    }

    public void setOrganizacionNombre(String organizacionNombre)
    {
        this.organizacionNombre = organizacionNombre;
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

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getUbicacion()
    {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion)
    {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaInicio()
    {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio)
    {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin()
    {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin)
    {
        this.fechaFin = fechaFin;
    }

    public LocalDateTime getFechaCierre()
    {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre)
    {
        this.fechaCierre = fechaCierre;
    }

    public Integer getCupoMaximo()
    {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo)
    {
        this.cupoMaximo = cupoMaximo;
    }

    public long getCuposOcupados()
    {
        return cuposOcupados;
    }

    public void setCuposOcupados(long cuposOcupados)
    {
        this.cuposOcupados = cuposOcupados;
    }

    public long getCuposDisponibles()
    {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(long cuposDisponibles)
    {
        this.cuposDisponibles = cuposDisponibles;
    }

    public EstadoActividad getEstado()
    {
        return estado;
    }

    public void setEstado(EstadoActividad estado)
    {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion()
    {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion)
    {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion()
    {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion)
    {
        this.fechaActualizacion = fechaActualizacion;
    }
}
