package co.edu.autonoma.reservasapi.controller;

import co.edu.autonoma.reservasapi.dto.VoluntarioRequest;
import co.edu.autonoma.reservasapi.dto.VoluntarioResponse;
import co.edu.autonoma.reservasapi.service.VoluntarioServicio;
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
@RequestMapping("/api/voluntarios")
@RequiredArgsConstructor
public class VoluntarioController
{
    private final VoluntarioServicio voluntarioServicio;

    @GetMapping
    public List<VoluntarioResponse> listarVoluntarios()
    {
        return voluntarioServicio.listarVoluntarios();
    }

    @GetMapping("/{id}")
    public VoluntarioResponse consultarVoluntario(@PathVariable Integer id)
    {
        return voluntarioServicio.consultarVoluntario(id);
    }

    @GetMapping("/documento/{documento}")
    public VoluntarioResponse consultarPorDocumento(@PathVariable String documento)
    {
        return voluntarioServicio.consultarPorDocumento(documento);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VoluntarioResponse crearVoluntario(@Valid @RequestBody VoluntarioRequest request)
    {
        return voluntarioServicio.crearVoluntario(request);
    }

    @PutMapping("/{id}")
    public VoluntarioResponse actualizarVoluntario(@PathVariable Integer id,
                                                   @Valid @RequestBody VoluntarioRequest request)
    {
        return voluntarioServicio.actualizarVoluntario(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarVoluntario(@PathVariable Integer id)
    {
        voluntarioServicio.eliminarVoluntario(id);
    }
}
