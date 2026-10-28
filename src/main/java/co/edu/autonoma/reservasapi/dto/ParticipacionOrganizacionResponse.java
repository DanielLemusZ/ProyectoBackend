package co.edu.autonoma.reservasapi.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

// Respuesta de GET /api/organizaciones/{id}/participacion
@Getter
public class ParticipacionOrganizacionResponse
{
    private final Integer organizacionId;
    private final String organizacionNombre;
    private final long totalActividades;
    private final long totalInscripcionesConfirmadas;
    private final long totalAsistencias;
    private final BigDecimal totalHoras;
    private final List<ParticipacionActividadResponse> actividades;

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
}
