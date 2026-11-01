package co.edu.autonoma.reservasapi.service;

import co.edu.autonoma.reservasapi.dto.InscripcionRequest;
import co.edu.autonoma.reservasapi.dto.InscripcionResponse;
import co.edu.autonoma.reservasapi.entity.ActividadEntity;
import co.edu.autonoma.reservasapi.entity.EstadoActividad;
import co.edu.autonoma.reservasapi.entity.EstadoInscripcion;
import co.edu.autonoma.reservasapi.entity.InscripcionEntity;
import co.edu.autonoma.reservasapi.entity.VoluntarioEntity;
import co.edu.autonoma.reservasapi.repository.ActividadRepository;
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
public class InscripcionServicio
{
    private final InscripcionRepository inscripcionRepository;
    private final ActividadRepository actividadRepository;
    private final VoluntarioRepository voluntarioRepository;

    public List<InscripcionResponse> listarInscripciones(EstadoInscripcion estado)
    {
        if (estado == null)
        {
            return convertirLista(inscripcionRepository.findAll());
        }

        return convertirLista(inscripcionRepository.findByEstado(estado));
    }

    public List<InscripcionResponse> listarPorActividad(Integer actividadId, EstadoInscripcion estado)
    {
        // 404 si la actividad no existe, en lugar de una lista vacía
        buscarActividad(actividadId);

        if (estado == null)
        {
            return convertirLista(inscripcionRepository.findByActividadId(actividadId));
        }

        return convertirLista(inscripcionRepository.findByActividadIdAndEstado(actividadId, estado));
    }

    public List<InscripcionResponse> listarPorVoluntario(Integer voluntarioId, EstadoInscripcion estado)
    {
        // 404 si el voluntario no existe, en lugar de una lista vacía
        buscarVoluntario(voluntarioId);

        if (estado == null)
        {
            return convertirLista(inscripcionRepository.findByVoluntarioId(voluntarioId));
        }

        return convertirLista(inscripcionRepository.findByVoluntarioIdAndEstado(voluntarioId, estado));
    }

    public InscripcionResponse consultarInscripcion(Integer id)
    {
        return InscripcionResponse.desde(buscarInscripcion(id));
    }

    // RN-06: devuelve la inscripción ya creada con esta clave, o vacío si la clave es nueva.
    // Responde 409 si la clave ya se usó con otra actividad u otro voluntario.
    public Optional<InscripcionResponse> buscarRepetida(InscripcionRequest request)
    {
        Optional<InscripcionEntity> existente =
                inscripcionRepository.findByClaveIdempotencia(request.claveIdempotencia());

        if (existente.isEmpty())
        {
            return Optional.empty();
        }

        InscripcionEntity inscripcion = existente.get();

        if (!inscripcion.getActividad().getId().equals(request.actividadId())
                || !inscripcion.getVoluntario().getId().equals(request.voluntarioId()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La clave " + request.claveIdempotencia() + " ya se usó con otros datos");
        }

        return Optional.of(InscripcionResponse.desde(inscripcion));
    }

    public InscripcionResponse crearInscripcion(InscripcionRequest request)
    {
        VoluntarioEntity voluntario = buscarVoluntario(request.voluntarioId());
        ActividadEntity actividad = buscarActividad(request.actividadId());

        // RN-03
        if (actividad.getEstado() != EstadoActividad.ABIERTA
                || LocalDateTime.now().isAfter(actividad.getFechaCierre()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La actividad " + actividad.getId() + " no está abierta a inscripciones");
        }

        // RN-02
        if (inscripcionRepository.existsByActividadIdAndVoluntarioIdAndEstado(
                actividad.getId(), voluntario.getId(), EstadoInscripcion.CONFIRMADA))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El voluntario " + voluntario.getId() + " ya está inscrito en la actividad " + actividad.getId());
        }

        // RN-01
        long cuposOcupados = inscripcionRepository.countByActividadIdAndEstado(
                actividad.getId(), EstadoInscripcion.CONFIRMADA);

        if (cuposOcupados >= actividad.getCupoMaximo())
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La actividad " + actividad.getId() + " no tiene cupos disponibles");
        }

        InscripcionEntity inscripcion = inscripcionRepository.save(
                new InscripcionEntity(actividad, voluntario, request.claveIdempotencia()));

        // RN-03: sin transacción, se cierra después de guardar. Si este paso falla,
        // RN-01 sigue rechazando nuevas inscripciones aunque la actividad siga ABIERTA.
        if (cuposOcupados + 1 >= actividad.getCupoMaximo())
        {
            actividad.cambiarEstado(EstadoActividad.CERRADA);
            actividadRepository.save(actividad);
        }

        return InscripcionResponse.desde(inscripcion);
    }

    public InscripcionResponse cancelarInscripcion(Integer id)
    {
        InscripcionEntity inscripcion = buscarInscripcion(id);

        // Repetir la cancelación devuelve el estado actual
        if (inscripcion.getEstado() == EstadoInscripcion.CANCELADA)
        {
            return InscripcionResponse.desde(inscripcion);
        }

        if (LocalDateTime.now().isAfter(inscripcion.getActividad().getFechaCierre()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La inscripción " + id + " no se puede cancelar porque ya pasó la fecha de cierre");
        }

        inscripcion.cancelar();
        return InscripcionResponse.desde(inscripcionRepository.save(inscripcion));
    }

    private List<InscripcionResponse> convertirLista(List<InscripcionEntity> inscripciones)
    {
        List<InscripcionResponse> respuesta = new ArrayList<>();

        for (InscripcionEntity inscripcion : inscripciones)
        {
            respuesta.add(InscripcionResponse.desde(inscripcion));
        }

        return respuesta;
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

    private ActividadEntity buscarActividad(Integer id)
    {
        Optional<ActividadEntity> actividad = actividadRepository.findById(id);

        if (actividad.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe la actividad " + id);
        }

        return actividad.get();
    }

    private VoluntarioEntity buscarVoluntario(Integer id)
    {
        Optional<VoluntarioEntity> voluntario = voluntarioRepository.findById(id);

        if (voluntario.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe el voluntario " + id);
        }

        return voluntario.get();
    }
}
