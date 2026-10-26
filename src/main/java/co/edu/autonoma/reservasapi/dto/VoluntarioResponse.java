package co.edu.autonoma.reservasapi.dto;

import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/voluntarios
public class VoluntarioResponse
{
    private Integer id;
    private String documento;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDateTime fechaCreacion;

    public VoluntarioResponse()
    {

    }

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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDocumento()
    {
        return documento;
    }

    public void setDocumento(String documento)
    {
        this.documento = documento;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getApellido()
    {
        return apellido;
    }

    public void setApellido(String apellido)
    {
        this.apellido = apellido;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public LocalDateTime getFechaCreacion()
    {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion)
    {
        this.fechaCreacion = fechaCreacion;
    }
}
