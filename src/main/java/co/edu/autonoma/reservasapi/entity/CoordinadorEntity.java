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

import java.time.LocalDateTime;

@Entity
@Table(name = "coordinador")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoordinadorEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Muchos coordinadores pertenecen a una organización
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacion_id", nullable = false)
    private OrganizacionEntity organizacion;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public CoordinadorEntity(OrganizacionEntity organizacion,
                             String nombre,
                             String apellido,
                             String email,
                             String telefono)
    {
        this.organizacion = organizacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
    }

    @PrePersist
    protected void alCrear()
    {
        fechaCreacion = LocalDateTime.now();
    }
}
