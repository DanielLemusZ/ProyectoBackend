package co.edu.autonoma.reservasapi.service;

import co.edu.autonoma.reservasapi.dto.CoordinadorActualizacionRequest;
import co.edu.autonoma.reservasapi.dto.CoordinadorRequest;
import co.edu.autonoma.reservasapi.dto.CoordinadorResponse;
import co.edu.autonoma.reservasapi.entity.CoordinadorEntity;
import co.edu.autonoma.reservasapi.entity.OrganizacionEntity;
import co.edu.autonoma.reservasapi.repository.ActividadRepository;
import co.edu.autonoma.reservasapi.repository.AsistenciaRepository;
import co.edu.autonoma.reservasapi.repository.CoordinadorRepository;
import co.edu.autonoma.reservasapi.repository.OrganizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoordinadorServicio
{
    private final CoordinadorRepository coordinadorRepository;
    private final OrganizacionRepository organizacionRepository;
    private final ActividadRepository actividadRepository;
    private final AsistenciaRepository asistenciaRepository;

    public List<CoordinadorResponse> listarCoordinadores()
    {
        List<CoordinadorEntity> coordinadores = coordinadorRepository.findAll();
        List<CoordinadorResponse> respuesta = new ArrayList<>();

        for (CoordinadorEntity coordinador : coordinadores)
        {
            respuesta.add(CoordinadorResponse.desde(coordinador));
        }

        return respuesta;
    }

    public List<CoordinadorResponse> listarPorOrganizacion(Integer organizacionId)
    {
        // 404 si la organización no existe, en lugar de una lista vacía
        buscarOrganizacion(organizacionId);

        List<CoordinadorEntity> coordinadores = coordinadorRepository.findByOrganizacionId(organizacionId);
        List<CoordinadorResponse> respuesta = new ArrayList<>();

        for (CoordinadorEntity coordinador : coordinadores)
        {
            respuesta.add(CoordinadorResponse.desde(coordinador));
        }

        return respuesta;
    }

    public CoordinadorResponse consultarCoordinador(Integer id)
    {
        return CoordinadorResponse.desde(buscarCoordinador(id));
    }

    public CoordinadorResponse consultarPorEmail(String email)
    {
        Optional<CoordinadorEntity> coordinador = coordinadorRepository.findByEmail(email);

        if (coordinador.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe un coordinador con correo " + email);
        }

        return CoordinadorResponse.desde(coordinador.get());
    }

    public CoordinadorResponse crearCoordinador(CoordinadorRequest request)
    {
        OrganizacionEntity organizacion = buscarOrganizacion(request.organizacionId());

        if (coordinadorRepository.existsByEmail(request.email()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un coordinador con correo " + request.email());
        }

        CoordinadorEntity coordinador = new CoordinadorEntity(
                organizacion,
                request.nombre(),
                request.apellido(),
                request.email(),
                request.telefono()
        );
        return CoordinadorResponse.desde(coordinadorRepository.save(coordinador));
    }

    public CoordinadorResponse actualizarCoordinador(Integer id, CoordinadorActualizacionRequest request)
    {
        CoordinadorEntity coordinador = buscarCoordinador(id);

        if (coordinadorRepository.existsByEmailAndIdNot(request.email(), id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe otro coordinador con correo " + request.email());
        }

        coordinador.actualizar(request.nombre(), request.apellido(), request.email(), request.telefono());
        return CoordinadorResponse.desde(coordinadorRepository.save(coordinador));
    }

    public void eliminarCoordinador(Integer id)
    {
        CoordinadorEntity coordinador = buscarCoordinador(id);

        // Eliminación física: solo si no ha creado actividades ni registrado asistencias
        if (actividadRepository.existsByCoordinadorId(id) || asistenciaRepository.existsByCoordinadorId(id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El coordinador " + id + " tiene actividades o asistencias y no se puede eliminar");
        }

        coordinadorRepository.delete(coordinador);
    }

    private CoordinadorEntity buscarCoordinador(Integer id)
    {
        Optional<CoordinadorEntity> coordinador = coordinadorRepository.findById(id);

        if (coordinador.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe el coordinador " + id);
        }

        return coordinador.get();
    }

    private OrganizacionEntity buscarOrganizacion(Integer id)
    {
        Optional<OrganizacionEntity> organizacion = organizacionRepository.findById(id);

        if (organizacion.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe la organización " + id);
        }

        return organizacion.get();
    }
}
