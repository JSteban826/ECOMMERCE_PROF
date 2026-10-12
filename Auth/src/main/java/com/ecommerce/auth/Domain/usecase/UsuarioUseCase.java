package com.ecommerce.auth.Domain.usecase;

import com.ecommerce.auth.Domain.model.Usuario;
import com.ecommerce.auth.Domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.Domain.model.gateway.EncrypterGateway;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;

    private final EncrypterGateway encrypterGateway;

    public Usuario actualizarUsuario(Usuario usuario) {

        validarIdUsuario(usuario.getIdUsuario());
        validarNombre(usuario);
        validarCorreo(usuario);
        validarClave(usuario);
        validarRol(usuario);
        validarNumeroTelefonico(usuario);
        validarEdad(usuario);

        return usuarioGateway.actualizarUsuario(usuario);
    }


    public Usuario guardarUsuario(Usuario usuario) {

        validarNombre(usuario);
        validarCorreo(usuario);
        validarClave(usuario);
        validarRol(usuario);
        validarNumeroTelefonico(usuario);
        validarEdad(usuario);

        usuario.setClave(
                encrypterGateway.encrypt(
                        usuario.getClave()
                )
        );

        return usuarioGateway.guardarusuario(usuario);
    }

    public Usuario buscarUsuario(String idUsuario) {
        return usuarioGateway.buscarPorIdUsuario(idUsuario);
    }
    public void eliminarUsuario(String idUsuario) {
        usuarioGateway.eliminarPorIdUsuario(idUsuario);
    }


    private void validarIdUsuario(String idUsuario) {

        Optional.ofNullable(idUsuario)
                .filter(id -> !id.isBlank())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El ID del usuario es obligatorio"));
    }

    private void validarNombre(Usuario usuario) {

        Optional.ofNullable(usuario.getNombre())
                .filter(nombre -> !nombre.isBlank())
                .filter(nombre -> nombre.length() >= 3)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El nombre debe tener mínimo 3 caracteres"));
    }

    private void validarCorreo(Usuario usuario) {

        Optional.ofNullable(usuario.getCorreo())
                .filter(correo -> !correo.isBlank())
                .filter(correo -> correo.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El correo no es válido"));
    }

    private void validarClave(Usuario usuario) {

        Optional.ofNullable(usuario.getClave())
                .filter(clave -> !clave.isBlank())
                .filter(clave -> clave.length() >= 8)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La clave debe tener mínimo 8 caracteres"));
    }

    private void validarRol(Usuario usuario) {

        Optional.ofNullable(usuario.getRol())
                .filter(rol -> !rol.isBlank())
                .filter(rol ->
                        rol.equals("ADMIN") ||
                                rol.equals("USER"))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El rol debe ser ADMIN o USER"));
    }

    private void validarNumeroTelefonico(Usuario usuario) {

        Optional.ofNullable(usuario.getNumeroTelefonico())
                .filter(numero -> !numero.isBlank())
                .filter(numero -> numero.matches("\\d{10}"))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El número telefónico debe tener 10 dígitos"));
    }

    private void validarEdad(Usuario usuario) {

        Optional.ofNullable(usuario.getEdad())
                .filter(edad -> edad >= 18)
                .filter(edad -> edad <= 120)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La edad debe estar entre 18 y 120 años"));
    }



    public String login(String correo, String clave) {

        Optional.ofNullable(correo)
                .filter(valor -> !valor.isBlank())
                .orElseThrow(() ->
                        new IllegalArgumentException("El correo es obligatorio"));

        Optional.ofNullable(clave)
                .filter(valor -> !valor.isBlank())
                .orElseThrow(() ->
                        new IllegalArgumentException("La clave es obligatoria"));

        usuarioGateway.login(correo, clave);

        return "Credenciales válidas";
    }
}