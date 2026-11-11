package co.edu.autonoma.reservasapi.service;

import co.edu.autonoma.reservasapi.dto.VoluntarioRequest;
import co.edu.autonoma.reservasapi.dto.VoluntarioResponse;
import co.edu.autonoma.reservasapi.entity.VoluntarioEntity;
import co.edu.autonoma.reservasapi.repository.InscripcionRepository;
import co.edu.autonoma.reservasapi.repository.VoluntarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoluntarioServicio
{
    private final VoluntarioRepository voluntarioRepository;
    private final InscripcionRepository inscripcionRepository;

    public List<VoluntarioResponse> listarVoluntarios()
    {
        List<VoluntarioEntity> voluntarios = voluntarioRepository.findAll();
        List<VoluntarioResponse> respuesta = new ArrayList<>();

        for (VoluntarioEntity voluntario : voluntarios)
        {
            respuesta.add(VoluntarioResponse.desde(voluntario));
        }

        return respuesta;
    }

    public VoluntarioResponse consultarVoluntario(Integer id)
    {
        return VoluntarioResponse.desde(buscarVoluntario(id));
    }

    public VoluntarioResponse consultarPorDocumento(String documento)
    {
        Optional<VoluntarioEntity> voluntario = voluntarioRepository.findByDocumento(documento);

        if (voluntario.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe un voluntario con documento " + documento);
        }

        return VoluntarioResponse.desde(voluntario.get());
    }

    public VoluntarioResponse crearVoluntario(VoluntarioRequest request)
    {
        if (voluntarioRepository.existsByDocumento(request.documento()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un voluntario con documento " + request.documento());
        }
        if (voluntarioRepository.existsByEmail(request.email()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un voluntario con correo " + request.email());
        }

        VoluntarioEntity voluntario = new VoluntarioEntity(
                request.documento(),
                request.nombre(),
                request.apellido(),
                request.email(),
                request.telefono()
        );
        return VoluntarioResponse.desde(voluntarioRepository.save(voluntario));
    }

    public VoluntarioResponse actualizarVoluntario(Integer id, VoluntarioRequest request)
    {
        VoluntarioEntity voluntario = buscarVoluntario(id);

        if (voluntarioRepository.existsByDocumentoAndIdNot(request.documento(), id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe otro voluntario con documento " + request.documento());
        }
        if (voluntarioRepository.existsByEmailAndIdNot(request.email(), id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe otro voluntario con correo " + request.email());
        }

        voluntario.actualizar(
                request.documento(),
                request.nombre(),
                request.apellido(),
                request.email(),
                request.telefono()
        );
        return VoluntarioResponse.desde(voluntarioRepository.save(voluntario));
    }

    public void eliminarVoluntario(Integer id)
    {
        VoluntarioEntity voluntario = buscarVoluntario(id);

        // Eliminación física: solo si no tiene inscripciones en ningún estado, para conservar el historial
        if (inscripcionRepository.existsByVoluntarioId(id))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El voluntario " + id + " tiene inscripciones y no se puede eliminar");
        }

        voluntarioRepository.delete(voluntario);
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
