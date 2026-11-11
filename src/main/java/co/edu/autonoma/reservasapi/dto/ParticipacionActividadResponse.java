package co.edu.autonoma.reservasapi.dto;

import co.edu.autonoma.reservasapi.entity.ActividadEntity;
import co.edu.autonoma.reservasapi.entity.EstadoActividad;
import lombok.Getter;

import java.math.BigDecimal;

// Fila por actividad dentro de ParticipacionOrganizacionResponse
@Getter
public class ParticipacionActividadResponse
{
    private final Integer actividadId;
    private final String titulo;
    private final EstadoActividad estado;
    private final Integer cupoMaximo;
    private final long inscripcionesConfirmadas;
    private final long asistencias;
    private final BigDecimal horas;

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

    // Conversión de la entidad a la fila; los conteos y las horas los calcula OrganizacionServicio
    public static ParticipacionActividadResponse desde(ActividadEntity actividad,
                                                       long inscripcionesConfirmadas,
                                                       long asistencias,
                                                       BigDecimal horas)
    {
        return new ParticipacionActividadResponse(
                actividad.getId(),
                actividad.getTitulo(),
                actividad.getEstado(),
                actividad.getCupoMaximo(),
                inscripcionesConfirmadas,
                asistencias,
                horas
        );
    }
}
