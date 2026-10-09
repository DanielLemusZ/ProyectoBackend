package co.edu.autonoma.reservasapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencia")
public class AsistenciaEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RN-04: la asistencia corresponde a una inscripción confirmada.
    // unique = true limita a una sola asistencia por inscripción.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inscripcion_id", nullable = false, unique = true)
    private InscripcionEntity inscripcion;

    // Muchas asistencias son registradas por un coordinador
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coordinador_id", nullable = false)
    private CoordinadorEntity coordinador;

    // RN-05: las horas deben ser positivas
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal horas;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void alCrear()
    {
        fechaRegistro = LocalDateTime.now();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public InscripcionEntity getInscripcion()
    {
        return inscripcion;
    }

    public void setInscripcion(InscripcionEntity inscripcion)
    {
        this.inscripcion = inscripcion;
    }

    public CoordinadorEntity getCoordinador()
    {
        return coordinador;
    }

    public void setCoordinador(CoordinadorEntity coordinador)
    {
        this.coordinador = coordinador;
    }

    public BigDecimal getHoras()
    {
        return horas;
    }

    public void setHoras(BigDecimal horas)
    {
        this.horas = horas;
    }

    public String getObservaciones()
    {
        return observaciones;
    }

    public void setObservaciones(String observaciones)
    {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaRegistro()
    {
        return fechaRegistro;
    }
}
