package co.edu.autonoma.reservasapi.dto;

import co.edu.autonoma.reservasapi.entity.VoluntarioEntity;
import lombok.Getter;

import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/voluntarios
@Getter
public class VoluntarioResponse
{
    private final Integer id;
    private final String documento;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final String telefono;
    private final LocalDateTime fechaCreacion;

    public VoluntarioResponse(
            Integer id,
            String documento,
            String nombre,
            String apellido,
            String email,
            String telefono,
            LocalDateTime fechaCreacion
    )
    {
        this.id = id;
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaCreacion = fechaCreacion;
    }

    // Conversión de la entidad a la respuesta; la usa VoluntarioServicio
    public static VoluntarioResponse desde(VoluntarioEntity voluntario)
    {
        return new VoluntarioResponse(
                voluntario.getId(),
                voluntario.getDocumento(),
                voluntario.getNombre(),
                voluntario.getApellido(),
                voluntario.getEmail(),
                voluntario.getTelefono(),
                voluntario.getFechaCreacion()
        );
    }
}
