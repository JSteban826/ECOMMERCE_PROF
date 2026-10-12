package com.ecommerce.auth.Domain.model.gateway;

public interface EncrypterGateway {
    String encrypt(String password);

    Boolean checkPass(String passUser, String passBD);

}
