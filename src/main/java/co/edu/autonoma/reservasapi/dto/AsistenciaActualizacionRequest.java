package co.edu.autonoma.reservasapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// Usada por PUT /api/asistencias/{id}. La inscripción y el coordinador no cambian.
public record AsistenciaActualizacionRequest(
        // RN-05: horas mayores que cero. @Digits corresponde a la columna NUMERIC(5,2)
        @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 3, fraction = 2)
        BigDecimal horas,
        String observaciones
)
{

}
