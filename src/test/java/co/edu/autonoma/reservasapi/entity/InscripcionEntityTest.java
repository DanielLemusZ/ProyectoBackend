package co.edu.autonoma.reservasapi.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class InscripcionEntityTest
{

    @Test
    void cancelar_inscripcionConfirmada_quedaCanceladaConFechaActual()
    {
        // Arrange: cancelar() solo cambia el estado y la fecha; la actividad y el voluntario no intervienen
        InscripcionEntity inscripcion = new InscripcionEntity(null, null, "clave-001");

        // Act
        inscripcion.cancelar();

        // Assert
        assertThat(inscripcion.getEstado()).isEqualTo(EstadoInscripcion.CANCELADA);
        assertThat(inscripcion.getFechaCancelacion())
                .isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS));
    }
}
