package co.edu.autonoma.reservasapi.dto;

import lombok.Getter;

import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/coordinadores
@Getter
public class CoordinadorResponse
{
    private final Integer id;
    private final Integer organizacionId;
    private final String organizacionNombre;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final String telefono;
    private final LocalDateTime fechaCreacion;

    public CoordinadorResponse(
            Integer id,
            Integer organizacionId,
            String organizacionNombre,
            String nombre,
            String apellido,
            String email,
            String telefono,
            LocalDateTime fechaCreacion
    )
    {
        this.id = id;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaCreacion = fechaCreacion;
    }
}
