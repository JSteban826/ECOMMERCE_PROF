package com.ecommerce.auth.Domain.model;

import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Usuario {

    private String idUsuario;
    private String nombre;
    private String correo;
    private String clave;
    private String rol;
    private String numeroTelefonico;
    private Integer edad;
}
