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

import java.time.LocalDateTime;

@Entity
@Table(name = "actividad")
public class ActividadEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public OrganizacionEntity getOrganizacion()
    {
        return organizacion;
    }

    public void setOrganizacion(OrganizacionEntity organizacion)
    {
        this.organizacion = organizacion;
    }

    public CoordinadorEntity getCoordinador()
    {
        return coordinador;
    }

    public void setCoordinador(CoordinadorEntity coordinador)
    {
        this.coordinador = coordinador;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getUbicacion()
    {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion)
    {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaInicio()
    {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio)
    {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin()
    {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin)
    {
        this.fechaFin = fechaFin;
    }

    public LocalDateTime getFechaCierre()
    {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre)
    {
        this.fechaCierre = fechaCierre;
    }

    public Integer getCupoMaximo()
    {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo)
    {
        this.cupoMaximo = cupoMaximo;
    }

    public EstadoActividad getEstado()
    {
        return estado;
    }

    public void setEstado(EstadoActividad estado)
    {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion()
    {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion()
    {
        return fechaActualizacion;
    }
}
