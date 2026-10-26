package co.edu.autonoma.reservasapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Usada por POST y PUT de /api/voluntarios
public record VoluntarioRequest(
        @NotBlank @Size(max = 20) String documento,
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 100) String apellido,
        @NotBlank @Email @Size(max = 150) String email,
        @Size(max = 20) String telefono
)
{

}
