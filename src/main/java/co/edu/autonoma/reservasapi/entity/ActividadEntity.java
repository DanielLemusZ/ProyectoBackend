package co.edu.autonoma.reservasapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "actividad")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActividadEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Muchas actividades son publicadas por una organización
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacion_id", nullable = false)
    private OrganizacionEntity organizacion;

    // Muchas actividades son creadas por un coordinador
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coordinador_id", nullable = false)
    private CoordinadorEntity coordinador;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 200)
    private String ubicacion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    // RN-03: fecha en que cierra la inscripción
    @Column(name = "fecha_cierre", nullable = false)
    private LocalDateTime fechaCierre;

    // RN-01: cupo máximo de voluntarios
    @Column(name = "cupo_maximo", nullable = false)
    private Integer cupoMaximo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoActividad estado = EstadoActividad.ABIERTA;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // La organización se toma del coordinador, para que no puedan quedar inconsistentes
    public ActividadEntity(CoordinadorEntity coordinador,
                           String titulo,
                           String descripcion,
                           String ubicacion,
                           LocalDateTime fechaInicio,
                           LocalDateTime fechaFin,
                           LocalDateTime fechaCierre,
                           Integer cupoMaximo)
    {
        this.organizacion = coordinador.getOrganizacion();
        this.coordinador = coordinador;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaCierre = fechaCierre;
        this.cupoMaximo = cupoMaximo;
    }

    @PrePersist
    protected void alCrear()
    {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = fechaCreacion;
    }

    @PreUpdate
    protected void alActualizar()
    {
        fechaActualizacion = LocalDateTime.now();
    }
}
