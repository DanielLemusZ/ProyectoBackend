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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencia")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AsistenciaEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public AsistenciaEntity(InscripcionEntity inscripcion,
                            CoordinadorEntity coordinador,
                            BigDecimal horas,
                            String observaciones)
    {
        this.inscripcion = inscripcion;
        this.coordinador = coordinador;
        this.horas = horas;
        this.observaciones = observaciones;
    }

    @PrePersist
    protected void alCrear()
    {
        fechaRegistro = LocalDateTime.now();
    }
}
