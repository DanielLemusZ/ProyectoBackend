package co.edu.autonoma.reservasapi.dto;

import co.edu.autonoma.reservasapi.entity.OrganizacionEntity;
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

    // Conversión de la entidad a la respuesta; los totales se suman a partir de las filas por actividad
    public static ParticipacionOrganizacionResponse desde(OrganizacionEntity organizacion,
                                                          List<ParticipacionActividadResponse> actividades)
    {
        long totalInscripcionesConfirmadas = 0;
        long totalAsistencias = 0;
        BigDecimal totalHoras = new BigDecimal("0.00");

        for (ParticipacionActividadResponse actividad : actividades)
        {
            totalInscripcionesConfirmadas += actividad.getInscripcionesConfirmadas();
            totalAsistencias += actividad.getAsistencias();
            totalHoras = totalHoras.add(actividad.getHoras());
        }

        return new ParticipacionOrganizacionResponse(
                organizacion.getId(),
                organizacion.getNombre(),
                actividades.size(),
                totalInscripcionesConfirmadas,
                totalAsistencias,
                totalHoras,
                actividades
        );
    }
}
