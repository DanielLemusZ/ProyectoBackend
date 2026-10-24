package co.edu.autonoma.reservasapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Usada por POST /api/inscripciones
public record InscripcionRequest(
        @NotNull Integer actividadId,
        @NotNull Integer voluntarioId,
        // RN-06: la genera el cliente (por ejemplo un UUID) y la reutiliza si reintenta
        @NotBlank @Size(max = 100) String claveIdempotencia
)
{

}
