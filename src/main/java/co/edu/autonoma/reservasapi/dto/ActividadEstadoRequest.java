package co.edu.autonoma.reservasapi.dto;

import co.edu.autonoma.reservasapi.entity.EstadoActividad;
import jakarta.validation.constraints.NotNull;

// Usada por PATCH /api/actividades/{id}/estado.
// CANCELADA no se acepta aquí: la cancelación se hace con DELETE.
public record ActividadEstadoRequest(
        @NotNull EstadoActividad estado
)
{

}
