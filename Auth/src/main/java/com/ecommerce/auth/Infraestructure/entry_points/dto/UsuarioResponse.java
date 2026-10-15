package com.ecommerce.auth.Infraestructure.entry_points.dto;
import com.ecommerce.auth.Domain.model.Usuario;
import java.math.BigDecimal;
public record UsuarioResponse(String idUsuario, String nombre, String correo,
        String rol, String numeroTelefonico, Integer edad, BigDecimal saldo) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(usuario.getIdUsuario(), usuario.getNombre(), usuario.getCorreo(),
                usuario.getRol(), usuario.getNumeroTelefonico(), usuario.getEdad(), usuario.getSaldo());
    }
}
