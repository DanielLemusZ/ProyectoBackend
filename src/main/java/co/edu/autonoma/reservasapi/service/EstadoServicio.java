package co.edu.autonoma.reservasapi.service;

import co.edu.autonoma.reservasapi.dto.EstadoResponse;
import org.springframework.stereotype.Service;

@Service
public class EstadoServicio
{
    public EstadoResponse consultarEstado()
    {
        return new EstadoResponse(
                "DaMano-api",
                "disponible"
        );
    }
}
