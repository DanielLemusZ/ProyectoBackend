package co.edu.autonoma.reservasapi.dto;

import lombok.Getter;

// Respuesta de GET /api/estado
@Getter
public class EstadoResponse
{
    private final String servicio;
    private final String estado;

    public EstadoResponse(String servicio, String estado)
    {
        this.servicio = servicio;
        this.estado = estado;
    }
}
