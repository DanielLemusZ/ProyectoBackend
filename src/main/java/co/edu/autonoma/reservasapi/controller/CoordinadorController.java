package co.edu.autonoma.reservasapi.controller;

import co.edu.autonoma.reservasapi.dto.CoordinadorActualizacionRequest;
import co.edu.autonoma.reservasapi.dto.CoordinadorRequest;
import co.edu.autonoma.reservasapi.dto.CoordinadorResponse;
import co.edu.autonoma.reservasapi.service.CoordinadorServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coordinadores")
@RequiredArgsConstructor
public class CoordinadorController
{
    private final CoordinadorServicio coordinadorServicio;

    @GetMapping
    public List<CoordinadorResponse> listarCoordinadores()
    {
        return coordinadorServicio.listarCoordinadores();
    }

    @GetMapping("/{id}")
    public CoordinadorResponse consultarCoordinador(@PathVariable Integer id)
    {
        return coordinadorServicio.consultarCoordinador(id);
    }

    @GetMapping("/email/{email}")
    public CoordinadorResponse consultarPorEmail(@PathVariable String email)
    {
        return coordinadorServicio.consultarPorEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CoordinadorResponse crearCoordinador(@Valid @RequestBody CoordinadorRequest request)
    {
        return coordinadorServicio.crearCoordinador(request);
    }

    @PutMapping("/{id}")
    public CoordinadorResponse actualizarCoordinador(@PathVariable Integer id,
                                                     @Valid @RequestBody CoordinadorActualizacionRequest request)
    {
        return coordinadorServicio.actualizarCoordinador(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCoordinador(@PathVariable Integer id)
    {
        coordinadorServicio.eliminarCoordinador(id);
    }
}
