package co.edu.autonoma.reservasapi.service;

import co.edu.autonoma.reservasapi.dto.ActividadActualizacionRequest;
import co.edu.autonoma.reservasapi.dto.ActividadEstadoRequest;
import co.edu.autonoma.reservasapi.dto.ActividadRequest;
import co.edu.autonoma.reservasapi.dto.ActividadResponse;
import co.edu.autonoma.reservasapi.entity.ActividadEntity;
import co.edu.autonoma.reservasapi.entity.CoordinadorEntity;
import co.edu.autonoma.reservasapi.entity.EstadoActividad;
import co.edu.autonoma.reservasapi.entity.EstadoInscripcion;
import co.edu.autonoma.reservasapi.entity.InscripcionEntity;
import co.edu.autonoma.reservasapi.repository.ActividadRepository;
import co.edu.autonoma.reservasapi.repository.AsistenciaRepository;
import co.edu.autonoma.reservasapi.repository.CoordinadorRepository;
import co.edu.autonoma.reservasapi.repository.InscripcionRepository;
import co.edu.autonoma.reservasapi.repository.OrganizacionRepository;
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
public class ActividadServicio
{
    private final ActividadRepository actividadRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final OrganizacionRepository organizacionRepository;
    private final InscripcionRepository inscripcionRepository;
    private final AsistenciaRepository asistenciaRepository;

    public List<ActividadResponse> listarActividades(EstadoActividad estado)
    {
        // RN-03: se cierran primero, para que la consulta ya vea el estado real
        List<ActividadEntity> vencidas = actividadRepository.findByEstadoAndFechaCierreBefore(
                EstadoActividad.ABIERTA, LocalDateTime.now());

        for (ActividadEntity vencida : vencidas)
        {
            aplicarCierrePorFecha(vencida);
        }

        // Sin filtro se listan solo las actividades abiertas a inscripción
        EstadoActividad estadoConsultado = estado;
        if (estadoConsultado == null)
        {
            estadoConsultado = EstadoActividad.ABIERTA;
        }

        return convertirLista(actividadRepository.findByEstado(estadoConsultado));
    }

    public List<ActividadResponse> listarPorOrganizacion(Integer organizacionId)
    {
        // 404 si la organización no existe, en lugar de una lista vacía
        if (!organizacionRepository.existsById(organizacionId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe la organización " + organizacionId);
        }

        return convertirLista(actividadRepository.findByOrganizacionId(organizacionId));
    }

    public List<ActividadResponse> listarPorCoordinador(Integer coordinadorId)
    {
        // 404 si el coordinador no existe, en lugar de una lista vacía
        buscarCoordinador(coordinadorId);

        return convertirLista(actividadRepository.findByCoordinadorId(coordinadorId));
    }

    public ActividadResponse consultarActividad(Integer id)
    {
        ActividadEntity actividad = buscarActividad(id);
        aplicarCierrePorFecha(actividad);
        return convertir(actividad);
    }

    public ActividadResponse crearActividad(ActividadRequest request)
    {
        validarFechas(request.fechaInicio(), request.fechaFin(), request.fechaCierre());

        CoordinadorEntity coordinador = buscarCoordinador(request.coordinadorId());

        ActividadEntity actividad = new ActividadEntity(
                coordinador,
                request.titulo(),
                request.descripcion(),
                request.ubicacion(),
                request.fechaInicio(),
                request.fechaFin(),
                request.fechaCierre(),
                request.cupoMaximo()
        );
        return convertir(actividadRepository.save(actividad));
    }

    public ActividadResponse actualizarActividad(Integer id, ActividadActualizacionRequest request)
    {
        validarFechas(request.fechaInicio(), request.fechaFin(), request.fechaCierre());

        ActividadEntity actividad = buscarActividad(id);

        if (esEstadoFinal(actividad.getEstado()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La actividad " + id + " está " + actividad.getEstado() + " y no se puede modificar");
        }
        if (contarCuposOcupados(actividad) > 0)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La actividad " + id + " ya tiene inscripciones confirmadas y no se puede modificar");
        }

        actividad.actualizar(
                request.titulo(),
                request.descripcion(),
                request.ubicacion(),
                request.fechaInicio(),
                request.fechaFin(),
                request.fechaCierre(),
                request.cupoMaximo()
        );
        return convertir(actividadRepository.save(actividad));
    }

    public ActividadResponse cambiarEstado(Integer id, ActividadEstadoRequest request)
    {
        EstadoActividad nuevoEstado = request.estado();

        // CANCELADA nunca es válido aquí: la cancelación se hace con DELETE
        if (nuevoEstado == EstadoActividad.CANCELADA)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Para cancelar una actividad se usa DELETE /api/actividades/{id}");
        }

        ActividadEntity actividad = buscarActividad(id);

        if (esEstadoFinal(actividad.getEstado()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La actividad " + id + " está " + actividad.getEstado() + " y no puede cambiar de estado");
        }

        if (nuevoEstado == EstadoActividad.CERRADA)
        {
            validarCierre(actividad);
        }
        else if (nuevoEstado == EstadoActividad.ABIERTA)
        {
            validarReapertura(actividad);
        }
        else
        {
            validarFinalizacion(actividad);
        }

        actividad.cambiarEstado(nuevoEstado);
        return convertir(actividadRepository.save(actividad));
    }

    public ActividadResponse cancelarActividad(Integer id)
    {
        ActividadEntity actividad = buscarActividad(id);

        // Repetir la cancelación devuelve el estado actual
        if (actividad.getEstado() == EstadoActividad.CANCELADA)
        {
            return convertir(actividad);
        }

        if (actividad.getEstado() == EstadoActividad.FINALIZADA)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La actividad " + id + " está FINALIZADA y no se puede cancelar");
        }
        if (asistenciaRepository.existsByInscripcionActividadId(id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La actividad " + id + " tiene asistencias registradas y no se puede cancelar");
        }

        List<InscripcionEntity> inscripciones = inscripcionRepository.findByActividadIdAndEstado(
                id, EstadoInscripcion.CONFIRMADA);

        // Sin transacción, la actividad se cancela al final: si algo falla antes,
        // sigue sin cancelar y repetir el DELETE completa las inscripciones que falten.
        for (InscripcionEntity inscripcion : inscripciones)
        {
            inscripcion.cancelar();
            inscripcionRepository.save(inscripcion);
        }

        actividad.cambiarEstado(EstadoActividad.CANCELADA);
        return convertir(actividadRepository.save(actividad));
    }

    private void validarFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin, LocalDateTime fechaCierre)
    {
        if (!fechaFin.isAfter(fechaInicio))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de fin debe ser posterior a la fecha de inicio");
        }
        // RN-03
        if (fechaCierre.isAfter(fechaInicio))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de cierre no puede ser posterior a la fecha de inicio");
        }
    }

    private void validarCierre(ActividadEntity actividad)
    {
        if (actividad.getEstado() != EstadoActividad.ABIERTA)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se puede cerrar una actividad ABIERTA");
        }
    }

    private void validarReapertura(ActividadEntity actividad)
    {
        if (actividad.getEstado() != EstadoActividad.CERRADA)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se puede reabrir una actividad CERRADA");
        }
        if (LocalDateTime.now().isAfter(actividad.getFechaCierre()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede reabrir la actividad porque ya pasó su fecha de cierre");
        }
        if (contarCuposOcupados(actividad) >= actividad.getCupoMaximo())
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede reabrir la actividad porque no tiene cupos disponibles");
        }
    }

    private void validarFinalizacion(ActividadEntity actividad)
    {
        if (!LocalDateTime.now().isAfter(actividad.getFechaFin()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede finalizar la actividad antes de su fecha de fin");
        }
    }

    private boolean esEstadoFinal(EstadoActividad estado)
    {
        return estado == EstadoActividad.CANCELADA || estado == EstadoActividad.FINALIZADA;
    }

    // RN-03: una actividad ABIERTA cuya fecha de cierre ya pasó pasa a CERRADA al consultarla
    private void aplicarCierrePorFecha(ActividadEntity actividad)
    {
        if (actividad.getEstado() == EstadoActividad.ABIERTA
                && LocalDateTime.now().isAfter(actividad.getFechaCierre()))
        {
            actividad.cambiarEstado(EstadoActividad.CERRADA);
            actividadRepository.save(actividad);
        }
    }

    private long contarCuposOcupados(ActividadEntity actividad)
    {
        return inscripcionRepository.countByActividadIdAndEstado(actividad.getId(), EstadoInscripcion.CONFIRMADA);
    }

    private ActividadResponse convertir(ActividadEntity actividad)
    {
        return ActividadResponse.desde(actividad, contarCuposOcupados(actividad));
    }

    private List<ActividadResponse> convertirLista(List<ActividadEntity> actividades)
    {
        List<ActividadResponse> respuesta = new ArrayList<>();

        for (ActividadEntity actividad : actividades)
        {
            aplicarCierrePorFecha(actividad);
            respuesta.add(convertir(actividad));
        }

        return respuesta;
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
