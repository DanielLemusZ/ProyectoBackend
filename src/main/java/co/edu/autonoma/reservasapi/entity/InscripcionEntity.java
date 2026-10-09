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
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscripcion")
public class InscripcionEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Muchas inscripciones pertenecen a una actividad
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "actividad_id", nullable = false)
    private ActividadEntity actividad;

    // Muchas inscripciones pertenecen a un voluntario
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voluntario_id", nullable = false)
    private VoluntarioEntity voluntario;

    // RN-06: la misma clave devuelve la inscripción existente en lugar de crear otra
    @Column(name = "clave_idempotencia", nullable = false, unique = true, length = 100)
    private String claveIdempotencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoInscripcion estado = EstadoInscripcion.CONFIRMADA;

    @Column(name = "fecha_inscripcion", nullable = false, updatable = false)
    private LocalDateTime fechaInscripcion;

    @Column(name = "fecha_cancelacion")
    private LocalDateTime fechaCancelacion;

    @PrePersist
    protected void alCrear()
    {
        fechaInscripcion = LocalDateTime.now();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public ActividadEntity getActividad()
    {
        return actividad;
    }

    public void setActividad(ActividadEntity actividad)
    {
        this.actividad = actividad;
    }

    public VoluntarioEntity getVoluntario()
    {
        return voluntario;
    }

    public void setVoluntario(VoluntarioEntity voluntario)
    {
        this.voluntario = voluntario;
    }

    public String getClaveIdempotencia()
    {
        return claveIdempotencia;
    }

    public void setClaveIdempotencia(String claveIdempotencia)
    {
        this.claveIdempotencia = claveIdempotencia;
    }

    public EstadoInscripcion getEstado()
    {
        return estado;
    }

    public void setEstado(EstadoInscripcion estado)
    {
        this.estado = estado;
    }

    public LocalDateTime getFechaInscripcion()
    {
        return fechaInscripcion;
    }

    public LocalDateTime getFechaCancelacion()
    {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(LocalDateTime fechaCancelacion)
    {
        this.fechaCancelacion = fechaCancelacion;
    }
}
