package co.edu.autonoma.reservasapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "organizacion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizacionEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, unique = true, length = 20)
    private String nit;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public OrganizacionEntity(String nombre, String nit, String email, String telefono)
    {
        this.nombre = nombre;
        this.nit = nit;
        this.email = email;
        this.telefono = telefono;
    }

    // PUT reemplaza todos los campos editables
    public void actualizar(String nombre, String nit, String email, String telefono)
    {
        this.nombre = nombre;
        this.nit = nit;
        this.email = email;
        this.telefono = telefono;
    }

    @PrePersist
    protected void alCrear()
    {
        fechaCreacion = LocalDateTime.now();
    }
}
