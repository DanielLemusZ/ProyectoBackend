package co.edu.autonoma.reservasapi.config;

import co.edu.autonoma.reservasapi.entity.ActividadEntity;
import co.edu.autonoma.reservasapi.entity.AsistenciaEntity;
import co.edu.autonoma.reservasapi.entity.CoordinadorEntity;
import co.edu.autonoma.reservasapi.entity.EstadoActividad;
import co.edu.autonoma.reservasapi.entity.EstadoInscripcion;
import co.edu.autonoma.reservasapi.entity.InscripcionEntity;
import co.edu.autonoma.reservasapi.entity.OrganizacionEntity;
import co.edu.autonoma.reservasapi.entity.VoluntarioEntity;
import co.edu.autonoma.reservasapi.repository.ActividadRepository;
import co.edu.autonoma.reservasapi.repository.AsistenciaRepository;
import co.edu.autonoma.reservasapi.repository.CoordinadorRepository;
import co.edu.autonoma.reservasapi.repository.InscripcionRepository;
import co.edu.autonoma.reservasapi.repository.OrganizacionRepository;
import co.edu.autonoma.reservasapi.repository.VoluntarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Function;

// Comprobación temporal de persistencia: se retira cuando existan los endpoints GET.
@Component
@RequiredArgsConstructor
public class PersistenciaCheck implements CommandLineRunner
{
    private static final String NIT_PRUEBA = "900000001-1";
    private static final String EMAIL_COORDINADOR_PRUEBA = "coordinador.prueba@damano.org";
    private static final String DOCUMENTO_VOLUNTARIO_PRUEBA = "1000000001";

    private final OrganizacionRepository organizacionRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;
    private final AsistenciaRepository asistenciaRepository;

    @Override
    public void run(String... args)
    {
        // nit, email y documento son únicos: se reutilizan los registros de una ejecución anterior
        OrganizacionEntity organizacion = organizacionRepository.findByNit(NIT_PRUEBA)
                .orElseGet(this::crearOrganizacion);

        CoordinadorEntity coordinador = coordinadorRepository.findByEmail(EMAIL_COORDINADOR_PRUEBA)
                .orElseGet(() -> crearCoordinador(organizacion));

        VoluntarioEntity voluntario = voluntarioRepository.findByDocumento(DOCUMENTO_VOLUNTARIO_PRUEBA)
                .orElseGet(this::crearVoluntario);

        // Actividad, inscripción y asistencia se crean nuevas en cada arranque
        ActividadEntity actividad = actividadRepository.save(nuevaActividad(coordinador));
        InscripcionEntity inscripcion = inscripcionRepository.save(nuevaInscripcion(actividad, voluntario));
        asistenciaRepository.save(nuevaAsistencia(inscripcion, coordinador));

        mostrar("Organizaciones", organizacionRepository,
                o -> o.getId() + " | " + o.getNombre() + " | " + o.getNit());
        mostrar("Coordinadores", coordinadorRepository,
                c -> c.getId() + " | " + c.getNombre() + " " + c.getApellido() + " | " + c.getEmail());
        mostrar("Voluntarios", voluntarioRepository,
                v -> v.getId() + " | " + v.getNombre() + " " + v.getApellido() + " | " + v.getDocumento());
        mostrar("Actividades", actividadRepository,
                a -> a.getId() + " | " + a.getTitulo() + " | " + a.getEstado());
        mostrar("Inscripciones", inscripcionRepository,
                i -> i.getId() + " | " + i.getClaveIdempotencia() + " | " + i.getEstado());
        mostrar("Asistencias", asistenciaRepository,
                a -> a.getId() + " | " + a.getHoras() + " horas");

        System.out.println("== Query methods");
        System.out.println("findByEstado(ABIERTA): "
                + actividadRepository.findByEstado(EstadoActividad.ABIERTA).size());
        System.out.println("countByEstado(ABIERTA): "
                + actividadRepository.countByEstado(EstadoActividad.ABIERTA));
        System.out.println("findByFechaCierreAfter(ahora): "
                + actividadRepository.findByFechaCierreAfter(LocalDateTime.now()).size());
        System.out.println("findByClaveIdempotencia(" + inscripcion.getClaveIdempotencia() + "): "
                + inscripcionRepository.findByClaveIdempotencia(inscripcion.getClaveIdempotencia()).isPresent());
        System.out.println("countByActividadIdAndEstado(" + actividad.getId() + ", CONFIRMADA): "
                + inscripcionRepository.countByActividadIdAndEstado(actividad.getId(), EstadoInscripcion.CONFIRMADA));
        System.out.println("existsByActividadIdAndVoluntarioIdAndEstado(" + actividad.getId() + ", "
                + voluntario.getId() + ", CONFIRMADA): "
                + inscripcionRepository.existsByActividadIdAndVoluntarioIdAndEstado(
                        actividad.getId(), voluntario.getId(), EstadoInscripcion.CONFIRMADA));
        System.out.println("existsByInscripcionId(" + inscripcion.getId() + "): "
                + asistenciaRepository.existsByInscripcionId(inscripcion.getId()));
    }

    // Imprime count() y findAll() de un repositorio
    private <T> void mostrar(String titulo, JpaRepository<T, Integer> repository, Function<T, String> fila)
    {
        System.out.println("== " + titulo + " | Total: " + repository.count());
        repository.findAll().forEach(entidad -> System.out.println(fila.apply(entidad)));
    }

    private OrganizacionEntity crearOrganizacion()
    {
        return organizacionRepository.save(new OrganizacionEntity(
                "Organización de prueba",
                NIT_PRUEBA,
                "contacto.prueba@damano.org",
                null
        ));
    }

    private CoordinadorEntity crearCoordinador(OrganizacionEntity organizacion)
    {
        return coordinadorRepository.save(new CoordinadorEntity(
                organizacion,
                "Coordinador",
                "Prueba",
                EMAIL_COORDINADOR_PRUEBA,
                null
        ));
    }

    private VoluntarioEntity crearVoluntario()
    {
        return voluntarioRepository.save(new VoluntarioEntity(
                DOCUMENTO_VOLUNTARIO_PRUEBA,
                "Voluntario",
                "Prueba",
                "voluntario.prueba@damano.org",
                null
        ));
    }

    private ActividadEntity nuevaActividad(CoordinadorEntity coordinador)
    {
        LocalDateTime inicio = LocalDateTime.now().plusDays(7);

        return new ActividadEntity(
                coordinador,
                "Jornada de limpieza de prueba",
                null,
                "Parque central",
                inicio,
                inicio.plusHours(4),
                inicio.minusDays(1),
                20
        );
    }

    private InscripcionEntity nuevaInscripcion(ActividadEntity actividad, VoluntarioEntity voluntario)
    {
        // La actividad es nueva en cada arranque, así que la clave también es única
        return new InscripcionEntity(actividad, voluntario, "prueba-actividad-" + actividad.getId());
    }

    private AsistenciaEntity nuevaAsistencia(InscripcionEntity inscripcion, CoordinadorEntity coordinador)
    {
        return new AsistenciaEntity(inscripcion, coordinador, new BigDecimal("4.00"), null);
    }
}
