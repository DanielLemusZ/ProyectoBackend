package co.edu.autonoma.reservasapi.controller;

import co.edu.autonoma.reservasapi.dto.AsistenciaActualizacionRequest;
import co.edu.autonoma.reservasapi.dto.AsistenciaResponse;
import co.edu.autonoma.reservasapi.service.AsistenciaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// La asistencia se registra con PATCH /api/inscripciones/{id}/asistencia.
// En esta ruta solo se consulta, corrige o elimina.
@RestController
@RequestMapping("/api/asistencias")
@RequiredArgsConstructor
public class AsistenciaController
{
    private final AsistenciaServicio asistenciaServicio;

    @GetMapping
    public List<AsistenciaResponse> listarAsistencias()
    {
        return asistenciaServicio.listarAsistencias();
    }

    @GetMapping("/{id}")
    public AsistenciaResponse consultarAsistencia(@PathVariable Integer id)
    {
        return asistenciaServicio.consultarAsistencia(id);
    }

    @PutMapping("/{id}")
    public AsistenciaResponse actualizarAsistencia(@PathVariable Integer id,
                                                   @Valid @RequestBody AsistenciaActualizacionRequest request)
    {
        return asistenciaServicio.actualizarAsistencia(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarAsistencia(@PathVariable Integer id)
    {
        asistenciaServicio.eliminarAsistencia(id);
    }
}
