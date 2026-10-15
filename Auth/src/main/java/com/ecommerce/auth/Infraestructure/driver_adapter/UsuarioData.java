package com.ecommerce.auth.Infraestructure.driver_adapter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(name = "uk_usuarios_correo", columnNames = "correo"))
public class UsuarioData {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column (nullable = false, updatable = false
    )
    private String idUsuario;

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "correo", nullable = false, length = 150)
    private String correo;

    @Column(nullable = false, length = 255)
    private String clave;

    @Column(nullable = false, length = 20)
    private String rol;

    @Column(nullable = false, length = 10)
    private String numeroTelefonico;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;
}
