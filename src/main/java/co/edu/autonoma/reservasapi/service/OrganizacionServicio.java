package co.edu.autonoma.reservasapi.service;

import co.edu.autonoma.reservasapi.dto.OrganizacionRequest;
import co.edu.autonoma.reservasapi.dto.OrganizacionResponse;
import co.edu.autonoma.reservasapi.dto.ParticipacionActividadResponse;
import co.edu.autonoma.reservasapi.dto.ParticipacionOrganizacionResponse;
import co.edu.autonoma.reservasapi.entity.ActividadEntity;
import co.edu.autonoma.reservasapi.entity.AsistenciaEntity;
import co.edu.autonoma.reservasapi.entity.EstadoInscripcion;
import co.edu.autonoma.reservasapi.entity.OrganizacionEntity;
import co.edu.autonoma.reservasapi.repository.ActividadRepository;
import co.edu.autonoma.reservasapi.repository.AsistenciaRepository;
import co.edu.autonoma.reservasapi.repository.CoordinadorRepository;
import co.edu.autonoma.reservasapi.repository.InscripcionRepository;
import co.edu.autonoma.reservasapi.repository.OrganizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizacionServicio
{
    private final OrganizacionRepository organizacionRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final ActividadServicio actividadServicio;

    public List<OrganizacionResponse> listarOrganizaciones()
    {
        List<OrganizacionEntity> organizaciones = organizacionRepository.findAll();
        List<OrganizacionResponse> respuesta = new ArrayList<>();

        for (OrganizacionEntity organizacion : organizaciones)
        {
            respuesta.add(OrganizacionResponse.desde(organizacion));
        }

        return respuesta;
    }

    public OrganizacionResponse consultarOrganizacion(Integer id)
    {
        return OrganizacionResponse.desde(buscarOrganizacion(id));
    }

    public OrganizacionResponse consultarPorNit(String nit)
    {
        Optional<OrganizacionEntity> organizacion = organizacionRepository.findByNit(nit);

        if (organizacion.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe una organización con NIT " + nit);
        }

        return OrganizacionResponse.desde(organizacion.get());
    }

    public OrganizacionResponse crearOrganizacion(OrganizacionRequest request)
    {
        if (organizacionRepository.existsByNit(request.nit()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una organización con NIT " + request.nit());
        }
        if (organizacionRepository.existsByEmail(request.email()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una organización con correo " + request.email());
        }

        OrganizacionEntity organizacion = new OrganizacionEntity(
                request.nombre(),
                request.nit(),
                request.email(),
                request.telefono()
        );
        return OrganizacionResponse.desde(organizacionRepository.save(organizacion));
    }

    public OrganizacionResponse actualizarOrganizacion(Integer id, OrganizacionRequest request)
    {
        OrganizacionEntity organizacion = buscarOrganizacion(id);

        if (organizacionRepository.existsByNitAndIdNot(request.nit(), id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe otra organización con NIT " + request.nit());
        }
        if (organizacionRepository.existsByEmailAndIdNot(request.email(), id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe otra organización con correo " + request.email());
        }

        organizacion.actualizar(request.nombre(), request.nit(), request.email(), request.telefono());
        return OrganizacionResponse.desde(organizacionRepository.save(organizacion));
    }

    public void eliminarOrganizacion(Integer id)
    {
        OrganizacionEntity organizacion = buscarOrganizacion(id);

        // Eliminación física: solo si nada la referencia
        if (coordinadorRepository.existsByOrganizacionId(id) || actividadRepository.existsByOrganizacionId(id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La organización " + id + " tiene coordinadores o actividades y no se puede eliminar");
        }

        organizacionRepository.delete(organizacion);
    }

    // Incluye todas las actividades de la organización, en cualquier estado
    public ParticipacionOrganizacionResponse consultarParticipacion(Integer id)
    {
        OrganizacionEntity organizacion = buscarOrganizacion(id);
        List<ActividadEntity> actividades = actividadRepository.findByOrganizacionId(id);
        List<ParticipacionActividadResponse> filas = new ArrayList<>();

        for (ActividadEntity actividad : actividades)
        {
            actividadServicio.aplicarCierrePorFecha(actividad);
            filas.add(calcularParticipacion(actividad));
        }

        return ParticipacionOrganizacionResponse.desde(organizacion, filas);
    }

    private ParticipacionActividadResponse calcularParticipacion(ActividadEntity actividad)
    {
        long inscripcionesConfirmadas = inscripcionRepository.countByActividadIdAndEstado(
                actividad.getId(), EstadoInscripcion.CONFIRMADA);
        List<AsistenciaEntity> asistencias = asistenciaRepository.findByInscripcionActividadId(actividad.getId());
        BigDecimal horas = new BigDecimal("0.00");

        for (AsistenciaEntity asistencia : asistencias)
        {
            horas = horas.add(asistencia.getHoras());
        }

        return ParticipacionActividadResponse.desde(actividad, inscripcionesConfirmadas, asistencias.size(), horas);
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
