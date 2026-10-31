package co.edu.autonoma.reservasapi.controller;

import co.edu.autonoma.reservasapi.dto.OrganizacionRequest;
import co.edu.autonoma.reservasapi.dto.OrganizacionResponse;
import co.edu.autonoma.reservasapi.service.OrganizacionServicio;
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
@RequestMapping("/api/organizaciones")
@RequiredArgsConstructor
public class OrganizacionController
{
    private final OrganizacionServicio organizacionServicio;

    @GetMapping
    public List<OrganizacionResponse> listarOrganizaciones()
    {
        return organizacionServicio.listarOrganizaciones();
    }

    @GetMapping("/{id}")
    public OrganizacionResponse consultarOrganizacion(@PathVariable Integer id)
    {
        return organizacionServicio.consultarOrganizacion(id);
    }

    @GetMapping("/nit/{nit}")
    public OrganizacionResponse consultarPorNit(@PathVariable String nit)
    {
        return organizacionServicio.consultarPorNit(nit);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizacionResponse crearOrganizacion(@Valid @RequestBody OrganizacionRequest request)
    {
        return organizacionServicio.crearOrganizacion(request);
    }

    @PutMapping("/{id}")
    public OrganizacionResponse actualizarOrganizacion(@PathVariable Integer id,
                                                       @Valid @RequestBody OrganizacionRequest request)
    {
        return organizacionServicio.actualizarOrganizacion(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarOrganizacion(@PathVariable Integer id)
    {
        organizacionServicio.eliminarOrganizacion(id);
    }
}
