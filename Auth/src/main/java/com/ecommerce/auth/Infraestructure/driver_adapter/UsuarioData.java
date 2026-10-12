package com.ecommerce.auth.Infraestructure.driver_adapter;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Length;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class UsuarioData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String idUsuario;

    private Integer edad;
    private String nombre;

    @Column(length = 100, nullable = false, unique = true)
    private String correo;

    @Column(length = 100, nullable = false, unique = true)
    private String clave;

    private String rol;

    private String numeroTelefonico;
}