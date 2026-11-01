package co.edu.autonoma.reservasapi.controller;

import co.edu.autonoma.reservasapi.dto.InscripcionRequest;
import co.edu.autonoma.reservasapi.dto.InscripcionResponse;
import co.edu.autonoma.reservasapi.entity.EstadoInscripcion;
import co.edu.autonoma.reservasapi.service.InscripcionServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionController
{
    private final InscripcionServicio inscripcionServicio;

    @GetMapping
    public List<InscripcionResponse> listarInscripciones(@RequestParam(required = false) EstadoInscripcion estado)
    {
        return inscripcionServicio.listarInscripciones(estado);
    }

    @GetMapping("/{id}")
    public InscripcionResponse consultarInscripcion(@PathVariable Integer id)
    {
        return inscripcionServicio.consultarInscripcion(id);
    }

    // RN-06: responde 200 si la clave ya existe y 201 si crea la inscripción,
    // por eso usa ResponseEntity en lugar de @ResponseStatus
    @PostMapping
    public ResponseEntity<InscripcionResponse> crearInscripcion(@Valid @RequestBody InscripcionRequest request)
    {
        Optional<InscripcionResponse> repetida = inscripcionServicio.buscarRepetida(request);

        if (repetida.isPresent())
        {
            return ResponseEntity.ok(repetida.get());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionServicio.crearInscripcion(request));
    }

    // Cancelación lógica: responde 200 con la inscripción, no 204
    @DeleteMapping("/{id}")
    public InscripcionResponse cancelarInscripcion(@PathVariable Integer id)
    {
        return inscripcionServicio.cancelarInscripcion(id);
    }
}
