package co.edu.autonoma.reservasapi.controller;

import co.edu.autonoma.reservasapi.dto.EstadoResponse;
import co.edu.autonoma.reservasapi.service.EstadoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estado")
@RequiredArgsConstructor
public class EstadoController
{
    private final EstadoServicio estadoServicio;

    @GetMapping
    public EstadoResponse consultaEstado()
    {
        return  estadoServicio.consultarEstado();
    }
}
