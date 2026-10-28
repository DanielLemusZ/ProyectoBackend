package co.edu.autonoma.reservasapi.dto;

import co.edu.autonoma.reservasapi.entity.OrganizacionEntity;
import lombok.Getter;

import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/organizaciones
@Getter
public class OrganizacionResponse
{
    private final Integer id;
    private final String nombre;
    private final String nit;
    private final String email;
    private final String telefono;
    private final LocalDateTime fechaCreacion;

    public OrganizacionResponse(
            Integer id,
            String nombre,
            String nit,
            String email,
            String telefono,
            LocalDateTime fechaCreacion
    )
    {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.email = email;
        this.telefono = telefono;
        this.fechaCreacion = fechaCreacion;
    }

    // Conversión de la entidad a la respuesta; la usa OrganizacionServicio
    public static OrganizacionResponse desde(OrganizacionEntity organizacion)
    {
        return new OrganizacionResponse(
                organizacion.getId(),
                organizacion.getNombre(),
                organizacion.getNit(),
                organizacion.getEmail(),
                organizacion.getTelefono(),
                organizacion.getFechaCreacion()
        );
    }
}
