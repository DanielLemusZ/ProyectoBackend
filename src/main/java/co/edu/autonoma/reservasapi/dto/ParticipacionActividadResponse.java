package co.edu.autonoma.reservasapi.dto;

import co.edu.autonoma.reservasapi.entity.EstadoActividad;

import java.math.BigDecimal;

// Fila por actividad dentro de ParticipacionOrganizacionResponse
public class ParticipacionActividadResponse
{
    private Integer actividadId;
    private String titulo;
    private EstadoActividad estado;
    private Integer cupoMaximo;
    private long inscripcionesConfirmadas;
    private long asistencias;
    private BigDecimal horas;

    public ParticipacionActividadResponse()
    {

    }

    public ParticipacionActividadResponse(
            Integer actividadId,
            String titulo,
            EstadoActividad estado,
            Integer cupoMaximo,
            long inscripcionesConfirmadas,
            long asistencias,
            BigDecimal horas
    )
    {
        this.actividadId = actividadId;
        this.titulo = titulo;
        this.estado = estado;
        this.cupoMaximo = cupoMaximo;
        this.inscripcionesConfirmadas = inscripcionesConfirmadas;
        this.asistencias = asistencias;
        this.horas = horas;
    }

    public Integer getActividadId()
    {
        return actividadId;
    }

    public void setActividadId(Integer actividadId)
    {
        this.actividadId = actividadId;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public EstadoActividad getEstado()
    {
        return estado;
    }

    public void setEstado(EstadoActividad estado)
    {
        this.estado = estado;
    }

    public Integer getCupoMaximo()
    {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo)
    {
        this.cupoMaximo = cupoMaximo;
    }

    public long getInscripcionesConfirmadas()
    {
        return inscripcionesConfirmadas;
    }

    public void setInscripcionesConfirmadas(long inscripcionesConfirmadas)
    {
        this.inscripcionesConfirmadas = inscripcionesConfirmadas;
    }

    public long getAsistencias()
    {
        return asistencias;
    }

    public void setAsistencias(long asistencias)
    {
        this.asistencias = asistencias;
    }

    public BigDecimal getHoras()
    {
        return horas;
    }

    public void setHoras(BigDecimal horas)
    {
        this.horas = horas;
    }
}
