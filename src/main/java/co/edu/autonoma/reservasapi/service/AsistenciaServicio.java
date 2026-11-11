package co.edu.autonoma.reservasapi.service;

import co.edu.autonoma.reservasapi.dto.AsistenciaActualizacionRequest;
import co.edu.autonoma.reservasapi.dto.AsistenciaRequest;
import co.edu.autonoma.reservasapi.dto.AsistenciaResponse;
import co.edu.autonoma.reservasapi.entity.ActividadEntity;
import co.edu.autonoma.reservasapi.entity.AsistenciaEntity;
import co.edu.autonoma.reservasapi.entity.CoordinadorEntity;
import co.edu.autonoma.reservasapi.entity.EstadoInscripcion;
import co.edu.autonoma.reservasapi.entity.InscripcionEntity;
import co.edu.autonoma.reservasapi.repository.ActividadRepository;
import co.edu.autonoma.reservasapi.repository.AsistenciaRepository;
import co.edu.autonoma.reservasapi.repository.CoordinadorRepository;
import co.edu.autonoma.reservasapi.repository.InscripcionRepository;
import co.edu.autonoma.reservasapi.repository.VoluntarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AsistenciaServicio
{
    private final AsistenciaRepository asistenciaRepository;
    private final InscripcionRepository inscripcionRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final ActividadRepository actividadRepository;
    private final VoluntarioRepository voluntarioRepository;

    public List<AsistenciaResponse> listarAsistencias()
    {
        return convertirLista(asistenciaRepository.findAll());
    }

    public List<AsistenciaResponse> listarPorActividad(Integer actividadId)
    {
        // 404 si la actividad no existe, en lugar de una lista vacía
        if (!actividadRepository.existsById(actividadId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe la actividad " + actividadId);
        }

        return convertirLista(asistenciaRepository.findByInscripcionActividadId(actividadId));
    }

    public List<AsistenciaResponse> listarPorVoluntario(Integer voluntarioId)
    {
        // 404 si el voluntario no existe, en lugar de una lista vacía
        if (!voluntarioRepository.existsById(voluntarioId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe el voluntario " + voluntarioId);
        }

        return convertirLista(asistenciaRepository.findByInscripcionVoluntarioId(voluntarioId));
    }

    public AsistenciaResponse consultarAsistencia(Integer id)
    {
        return AsistenciaResponse.desde(buscarAsistencia(id));
    }

    // RN-05 (horas mayores que cero) la validan las anotaciones de AsistenciaRequest
    public AsistenciaResponse registrarAsistencia(Integer inscripcionId, AsistenciaRequest request)
    {
        InscripcionEntity inscripcion = buscarInscripcion(inscripcionId);
        CoordinadorEntity coordinador = buscarCoordinador(request.coordinadorId());
        ActividadEntity actividad = inscripcion.getActividad();

        // RN-04
        if (inscripcion.getEstado() != EstadoInscripcion.CONFIRMADA)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La inscripción " + inscripcionId + " no está confirmada");
        }
        if (asistenciaRepository.existsByInscripcionId(inscripcionId))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La inscripción " + inscripcionId + " ya tiene una asistencia registrada");
        }

        if (!coordinador.getOrganizacion().getId().equals(actividad.getOrganizacion().getId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El coordinador " + coordinador.getId() + " no pertenece a la organización de la actividad");
        }
        if (LocalDateTime.now().isBefore(actividad.getFechaInicio()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La actividad " + actividad.getId() + " todavía no ha iniciado");
        }

        AsistenciaEntity asistencia = new AsistenciaEntity(
                inscripcion,
                coordinador,
                request.horas(),
                request.observaciones()
        );
        return AsistenciaResponse.desde(asistenciaRepository.save(asistencia));
    }

    public AsistenciaResponse actualizarAsistencia(Integer id, AsistenciaActualizacionRequest request)
    {
        AsistenciaEntity asistencia = buscarAsistencia(id);

        asistencia.actualizar(request.horas(), request.observaciones());
        return AsistenciaResponse.desde(asistenciaRepository.save(asistencia));
    }

    public void eliminarAsistencia(Integer id)
    {
        asistenciaRepository.delete(buscarAsistencia(id));
    }

    private List<AsistenciaResponse> convertirLista(List<AsistenciaEntity> asistencias)
    {
        List<AsistenciaResponse> respuesta = new ArrayList<>();

        for (AsistenciaEntity asistencia : asistencias)
        {
            respuesta.add(AsistenciaResponse.desde(asistencia));
        }

        return respuesta;
    }

    private AsistenciaEntity buscarAsistencia(Integer id)
    {
        Optional<AsistenciaEntity> asistencia = asistenciaRepository.findById(id);

        if (asistencia.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe la asistencia " + id);
        }

        return asistencia.get();
    }

    private InscripcionEntity buscarInscripcion(Integer id)
    {
        Optional<InscripcionEntity> inscripcion = inscripcionRepository.findById(id);

        if (inscripcion.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe la inscripción " + id);
        }

        return inscripcion.get();
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
}
