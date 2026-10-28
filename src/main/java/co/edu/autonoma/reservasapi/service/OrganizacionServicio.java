package co.edu.autonoma.reservasapi.service;

import co.edu.autonoma.reservasapi.dto.OrganizacionRequest;
import co.edu.autonoma.reservasapi.dto.OrganizacionResponse;
import co.edu.autonoma.reservasapi.entity.OrganizacionEntity;
import co.edu.autonoma.reservasapi.repository.ActividadRepository;
import co.edu.autonoma.reservasapi.repository.CoordinadorRepository;
import co.edu.autonoma.reservasapi.repository.OrganizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizacionServicio
{
    private final OrganizacionRepository organizacionRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final ActividadRepository actividadRepository;

    public List<OrganizacionResponse> listarOrganizaciones()
    {
        return organizacionRepository.findAll().stream()
                .map(OrganizacionResponse::desde)
                .toList();
    }

    public OrganizacionResponse consultarOrganizacion(Integer id)
    {
        return OrganizacionResponse.desde(buscarOrganizacion(id));
    }

    public OrganizacionResponse consultarPorNit(String nit)
    {
        OrganizacionEntity organizacion = organizacionRepository.findByNit(nit)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe una organización con NIT " + nit));
        return OrganizacionResponse.desde(organizacion);
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

    private OrganizacionEntity buscarOrganizacion(Integer id)
    {
        return organizacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe la organización " + id));
    }
}
