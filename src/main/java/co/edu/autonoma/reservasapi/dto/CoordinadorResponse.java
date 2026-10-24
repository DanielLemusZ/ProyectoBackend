package co.edu.autonoma.reservasapi.dto;

import java.time.LocalDateTime;

// Respuesta de los endpoints de /api/coordinadores
public class CoordinadorResponse
{
    private Integer id;
    private Integer organizacionId;
    private String organizacionNombre;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDateTime fechaCreacion;

    public CoordinadorResponse()
    {

    }

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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getOrganizacionId()
    {
        return organizacionId;
    }

    public void setOrganizacionId(Integer organizacionId)
    {
        this.organizacionId = organizacionId;
    }

    public String getOrganizacionNombre()
    {
        return organizacionNombre;
    }

    public void setOrganizacionNombre(String organizacionNombre)
    {
        this.organizacionNombre = organizacionNombre;
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
