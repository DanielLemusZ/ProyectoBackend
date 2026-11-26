package co.edu.autonoma.reservasapi.service;

import co.edu.autonoma.reservasapi.dto.InscripcionRequest;
import co.edu.autonoma.reservasapi.dto.InscripcionResponse;
import co.edu.autonoma.reservasapi.entity.*;
import co.edu.autonoma.reservasapi.repository.ActividadRepository;
import co.edu.autonoma.reservasapi.repository.InscripcionRepository;
import co.edu.autonoma.reservasapi.repository.VoluntarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscripcionServicioTest
{
    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private ActividadRepository actividadRepository;

    @Mock
    private VoluntarioRepository voluntarioRepository;

    @InjectMocks
    private InscripcionServicio servicio;

    @Test
    void consultarInscripcion_existente_devuelveLaInscripcion()
    {
        // Arrange
        InscripcionEntity inscripcion = new InscripcionEntity(nuevaActividad(10), nuevoVoluntario(), "clave-001");
        when(inscripcionRepository.findById(3)).thenReturn(Optional.of(inscripcion));

        // Act
        InscripcionResponse respuesta = servicio.consultarInscripcion(3);

        // Assert
        assertThat(respuesta.getClaveIdempotencia()).isEqualTo("clave-001");
        assertThat(respuesta.getEstado()).isEqualTo(EstadoInscripcion.CONFIRMADA);
    }

    @Test
    void consultarInscripcion_inexistente_lanza404()
    {
        // Arrange
        when(inscripcionRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> servicio.consultarInscripcion(99));

        // Assert
        assertThat(error.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void crearInscripcion_ultimoCupoDisponible_quedaConfirmadaYCierraLaActividad()
    {
        // Arrange
        ActividadEntity actividad = nuevaActividad(10);
        when(voluntarioRepository.findById(1)).thenReturn(Optional.of(nuevoVoluntario()));
        when(actividadRepository.findById(2)).thenReturn(Optional.of(actividad));
        // RN-01: ya hay 9 de 10 cupos ocupados
        when(inscripcionRepository.countByActividadIdAndEstado(actividad.getId(), EstadoInscripcion.CONFIRMADA))
                .thenReturn(9L);
        // save devuelve el mismo objeto que recibe, como haría la base
        when(inscripcionRepository.save(any(InscripcionEntity.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        // Act
        InscripcionResponse respuesta = servicio.crearInscripcion(new InscripcionRequest(2, 1, "clave-001"));

        // Assert
        assertThat(respuesta.getEstado()).isEqualTo(EstadoInscripcion.CONFIRMADA);
        // RN-03: al ocuparse el último cupo, la actividad pasa a CERRADA
        assertThat(actividad.getEstado()).isEqualTo(EstadoActividad.CERRADA);
    }

    @Test
    void cancelarInscripcion_existente_guardaLaInscripcionEncontrada()
    {
        InscripcionEntity inscripcion = new InscripcionEntity(nuevaActividad(10), nuevoVoluntario(), "clave-001");
        when(inscripcionRepository.findById(3)).thenReturn(Optional.of(inscripcion));
        when(inscripcionRepository.save(inscripcion)).thenReturn(inscripcion);

        servicio.cancelarInscripcion(3);

        verify(inscripcionRepository).save(inscripcion);
    }

    @Test
    void cancelarInscripcion_inexistente_lanza404YNoGuarda()
    {
        when(inscripcionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> servicio.cancelarInscripcion(99));
        verify(inscripcionRepository, never()).save(any());
    }

    // Actividad ABIERTA cuya inscripción cierra dentro de 5 días (RN-03)
    private ActividadEntity nuevaActividad(int cupoMaximo)
    {
        OrganizacionEntity organizacion = new OrganizacionEntity("Fundación Manos Unidas", "900123456-7",
                "contacto@manosunidas.org", "6041234567");
        CoordinadorEntity coordinador = new CoordinadorEntity(organizacion, "Laura", "Gómez",
                "laura.gomez@manosunidas.org", "3001234567");
        LocalDateTime inicio = LocalDateTime.now().plusDays(10);

        return new ActividadEntity(coordinador, "Jornada de siembra", "Siembra de árboles nativos",
                "Parque Caldas", inicio, inicio.plusHours(4), inicio.minusDays(5), cupoMaximo);
    }

    private VoluntarioEntity nuevoVoluntario()
    {
        return new VoluntarioEntity("1053000111", "Juan", "Pérez", "juan.perez@correo.com", "3101112233");
    }

}
