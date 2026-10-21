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
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Function;

// Comprobación temporal de persistencia: se retira cuando existan los endpoints GET.
@Component
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

    public PersistenciaCheck(OrganizacionRepository organizacionRepository,
                             CoordinadorRepository coordinadorRepository,
                             VoluntarioRepository voluntarioRepository,
                             ActividadRepository actividadRepository,
                             InscripcionRepository inscripcionRepository,
                             AsistenciaRepository asistenciaRepository)
    {
        this.organizacionRepository = organizacionRepository;
        this.coordinadorRepository = coordinadorRepository;
        this.voluntarioRepository = voluntarioRepository;
        this.actividadRepository = actividadRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.asistenciaRepository = asistenciaRepository;
    }

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
        ActividadEntity actividad = actividadRepository.save(nuevaActividad(organizacion, coordinador));
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
        OrganizacionEntity organizacion = new OrganizacionEntity();
        organizacion.setNombre("Organización de prueba");
        organizacion.setNit(NIT_PRUEBA);
        organizacion.setEmail("contacto.prueba@damano.org");
        return organizacionRepository.save(organizacion);
    }

    private CoordinadorEntity crearCoordinador(OrganizacionEntity organizacion)
    {
        CoordinadorEntity coordinador = new CoordinadorEntity();
        coordinador.setOrganizacion(organizacion);
        coordinador.setNombre("Coordinador");
        coordinador.setApellido("Prueba");
        coordinador.setEmail(EMAIL_COORDINADOR_PRUEBA);
        return coordinadorRepository.save(coordinador);
    }

    private VoluntarioEntity crearVoluntario()
    {
        VoluntarioEntity voluntario = new VoluntarioEntity();
        voluntario.setDocumento(DOCUMENTO_VOLUNTARIO_PRUEBA);
        voluntario.setNombre("Voluntario");
        voluntario.setApellido("Prueba");
        voluntario.setEmail("voluntario.prueba@damano.org");
        return voluntarioRepository.save(voluntario);
    }

    private ActividadEntity nuevaActividad(OrganizacionEntity organizacion, CoordinadorEntity coordinador)
    {
        LocalDateTime inicio = LocalDateTime.now().plusDays(7);

        ActividadEntity actividad = new ActividadEntity();
        actividad.setOrganizacion(organizacion);
        actividad.setCoordinador(coordinador);
        actividad.setTitulo("Jornada de limpieza de prueba");
        actividad.setUbicacion("Parque central");
        actividad.setFechaInicio(inicio);
        actividad.setFechaFin(inicio.plusHours(4));
        actividad.setFechaCierre(inicio.minusDays(1));
        actividad.setCupoMaximo(20);
        return actividad;
    }

    private InscripcionEntity nuevaInscripcion(ActividadEntity actividad, VoluntarioEntity voluntario)
    {
        InscripcionEntity inscripcion = new InscripcionEntity();
        inscripcion.setActividad(actividad);
        inscripcion.setVoluntario(voluntario);
        // La actividad es nueva en cada arranque, así que la clave también es única
        inscripcion.setClaveIdempotencia("prueba-actividad-" + actividad.getId());
        return inscripcion;
    }

    private AsistenciaEntity nuevaAsistencia(InscripcionEntity inscripcion, CoordinadorEntity coordinador)
    {
        AsistenciaEntity asistencia = new AsistenciaEntity();
        asistencia.setInscripcion(inscripcion);
        asistencia.setCoordinador(coordinador);
        asistencia.setHoras(new BigDecimal("4.00"));
        return asistencia;
    }
}
