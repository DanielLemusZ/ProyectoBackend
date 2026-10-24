package co.edu.autonoma.reservasapi.dto;

import java.math.BigDecimal;
import java.util.List;

// Respuesta de GET /api/organizaciones/{id}/participacion
public class ParticipacionOrganizacionResponse
{
    private Integer organizacionId;
    private String organizacionNombre;
    private long totalActividades;
    private long totalInscripcionesConfirmadas;
    private long totalAsistencias;
    private BigDecimal totalHoras;
    private List<ParticipacionActividadResponse> actividades;

    public ParticipacionOrganizacionResponse()
    {

    }

    public ParticipacionOrganizacionResponse(
            Integer organizacionId,
            String organizacionNombre,
            long totalActividades,
            long totalInscripcionesConfirmadas,
            long totalAsistencias,
            BigDecimal totalHoras,
            List<ParticipacionActividadResponse> actividades
    )
    {
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.totalActividades = totalActividades;
        this.totalInscripcionesConfirmadas = totalInscripcionesConfirmadas;
        this.totalAsistencias = totalAsistencias;
        this.totalHoras = totalHoras;
        this.actividades = actividades;
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

    public long getTotalActividades()
    {
        return totalActividades;
    }

    public void setTotalActividades(long totalActividades)
    {
        this.totalActividades = totalActividades;
    }

    public long getTotalInscripcionesConfirmadas()
    {
        return totalInscripcionesConfirmadas;
    }

    public void setTotalInscripcionesConfirmadas(long totalInscripcionesConfirmadas)
    {
        this.totalInscripcionesConfirmadas = totalInscripcionesConfirmadas;
    }

    public long getTotalAsistencias()
    {
        return totalAsistencias;
    }

    public void setTotalAsistencias(long totalAsistencias)
    {
        this.totalAsistencias = totalAsistencias;
    }

    public BigDecimal getTotalHoras()
    {
        return totalHoras;
    }

    public void setTotalHoras(BigDecimal totalHoras)
    {
        this.totalHoras = totalHoras;
    }

    public List<ParticipacionActividadResponse> getActividades()
    {
        return actividades;
    }

    public void setActividades(List<ParticipacionActividadResponse> actividades)
    {
        this.actividades = actividades;
    }
}
