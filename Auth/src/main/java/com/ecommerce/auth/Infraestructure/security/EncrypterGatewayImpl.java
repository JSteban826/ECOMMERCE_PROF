package com.ecommerce.auth.Infraestructure.security;
import com.ecommerce.auth.Domain.exception.ErrorAplicacion;
import com.ecommerce.auth.Domain.exception.ErrorAplicacion.Tipo;
import com.ecommerce.auth.Domain.model.gateway.EncrypterGateway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class EncrypterGatewayImpl implements EncrypterGateway {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override public String encrypt(String password) {
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ErrorAplicacion(Tipo.VALIDACION, "CLAVE_EXCEDE_LIMITE",
                    "La clave no puede superar 72 bytes UTF-8 con BCrypt");
        }
        return encoder.encode(password);
    }
    @Override public Boolean checkPass(String password, String hash) {
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) return false;
        return encoder.matches(password, hash);
    }
}
