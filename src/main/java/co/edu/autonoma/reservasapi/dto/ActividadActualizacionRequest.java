package co.edu.autonoma.reservasapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

// Usada por PUT /api/actividades/{id}, solo antes de que la actividad reciba
// inscripciones CONFIRMADA (lo valida el servicio).
// No incluye coordinadorId y no exige fechas futuras, para poder reprogramar
// una actividad sin inscritos cuya fecha de cierre ya pasó.
public record ActividadActualizacionRequest(
        @NotBlank @Size(max = 150) String titulo,
        String descripcion,
        @NotBlank @Size(max = 200) String ubicacion,
        @NotNull LocalDateTime fechaInicio,
        @NotNull LocalDateTime fechaFin,
        @NotNull LocalDateTime fechaCierre,
        @NotNull @Positive Integer cupoMaximo
)
{

}
