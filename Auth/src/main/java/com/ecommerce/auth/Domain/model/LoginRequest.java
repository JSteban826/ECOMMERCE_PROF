package com.ecommerce.auth.Domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String correo;
    private String clave;

}