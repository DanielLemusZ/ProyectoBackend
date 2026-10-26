package co.edu.autonoma.reservasapi.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

// Usada por POST /api/actividades.
// La organización se toma del coordinador. Las comparaciones entre fechas
// (fechaFin > fechaInicio, fechaCierre <= fechaInicio) las valida el servicio.
public record ActividadRequest(
        @NotNull Integer coordinadorId,
        @NotBlank @Size(max = 150) String titulo,
        String descripcion,
        @NotBlank @Size(max = 200) String ubicacion,
        @NotNull @Future LocalDateTime fechaInicio,
        @NotNull LocalDateTime fechaFin,
        // RN-03: fecha en que cierra la inscripción
        @NotNull @Future LocalDateTime fechaCierre,
        // RN-01: cupo máximo de voluntarios
        @NotNull @Positive Integer cupoMaximo
)
{

}
