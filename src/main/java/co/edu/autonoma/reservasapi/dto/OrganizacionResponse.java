package co.edu.autonoma.reservasapi.dto;

import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/organizaciones
public class OrganizacionResponse
{
    private Integer id;
    private String nombre;
    private String nit;
    private String email;
    private String telefono;
    private LocalDateTime fechaCreacion;

    public OrganizacionResponse()
    {

    }

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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getNit()
    {
        return nit;
    }

    public void setNit(String nit)
    {
        this.nit = nit;
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
