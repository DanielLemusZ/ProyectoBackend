package co.edu.autonoma.reservasapi.controller;

import co.edu.autonoma.reservasapi.dto.ActividadActualizacionRequest;
import co.edu.autonoma.reservasapi.dto.ActividadEstadoRequest;
import co.edu.autonoma.reservasapi.dto.ActividadRequest;
import co.edu.autonoma.reservasapi.dto.ActividadResponse;
import co.edu.autonoma.reservasapi.dto.InscripcionResponse;
import co.edu.autonoma.reservasapi.entity.EstadoActividad;
import co.edu.autonoma.reservasapi.entity.EstadoInscripcion;
import co.edu.autonoma.reservasapi.service.ActividadServicio;
import co.edu.autonoma.reservasapi.service.InscripcionServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
@RequiredArgsConstructor
public class ActividadController
{
    private final ActividadServicio actividadServicio;
    private final InscripcionServicio inscripcionServicio;

    @GetMapping
    public List<ActividadResponse> listarActividades(@RequestParam(required = false) EstadoActividad estado)
    {
        return actividadServicio.listarActividades(estado);
    }

    @GetMapping("/{id}")
    public ActividadResponse consultarActividad(@PathVariable Integer id)
    {
        return actividadServicio.consultarActividad(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActividadResponse crearActividad(@Valid @RequestBody ActividadRequest request)
    {
        return actividadServicio.crearActividad(request);
    }

    @PutMapping("/{id}")
    public ActividadResponse actualizarActividad(@PathVariable Integer id,
                                                 @Valid @RequestBody ActividadActualizacionRequest request)
    {
        return actividadServicio.actualizarActividad(id, request);
    }

    @PatchMapping("/{id}/estado")
    public ActividadResponse cambiarEstado(@PathVariable Integer id,
                                           @Valid @RequestBody ActividadEstadoRequest request)
    {
        return actividadServicio.cambiarEstado(id, request);
    }

    // Cancelación lógica: responde 200 con la actividad, no 204
    @DeleteMapping("/{id}")
    public ActividadResponse cancelarActividad(@PathVariable Integer id)
    {
        return actividadServicio.cancelarActividad(id);
    }

    @GetMapping("/{id}/inscripciones")
    public List<InscripcionResponse> listarInscripciones(@PathVariable Integer id,
                                                         @RequestParam(required = false) EstadoInscripcion estado)
    {
        return inscripcionServicio.listarPorActividad(id, estado);
    }
}
