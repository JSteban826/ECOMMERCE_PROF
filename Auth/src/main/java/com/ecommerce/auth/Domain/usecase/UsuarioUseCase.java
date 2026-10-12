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

    private void validarUsuario(Usuario usuario) {

        validarNombre(usuario.getNombre());
        validarCorreo(usuario.getCorreo());
        validarClave(usuario.getClave());
        validarRol(usuario.getRol());
        validarNumeroTelefonico(usuario.getNumeroTelefonico());
        validarEdad(usuario.getEdad());

    }

    public Usuario guardarUsuario(Usuario usuario) {

        validarUsuario(usuario);

        usuario.setClave(encrypterGateway.encrypt(usuario.getClave())
        );

        return usuarioGateway.guardarusuario(usuario);
    }

    public Usuario actualizarUsuario(Usuario usuario) {

        validarIdUsuario(usuario.getIdUsuario());
        validarUsuario(usuario);

        usuario.setClave(encrypterGateway.encrypt(usuario.getClave()));

        return usuarioGateway.actualizarUsuario(usuario);
    }

    public Usuario buscarUsuario(String idUsuario) {

        validarIdUsuario(idUsuario);

        return usuarioGateway.buscarPorIdUsuario(idUsuario);
    }

    public void eliminarUsuario(String idUsuario) {

        validarIdUsuario(idUsuario);

        usuarioGateway.eliminarPorIdUsuario(idUsuario);
    }


    private void validarIdUsuario(String idUsuario) {

        Optional.ofNullable(idUsuario)
                .filter(id -> !id.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("El ID del usuario es obligatorio"));
    }

    private void validarNombre(String nombre) {

        Optional.ofNullable(nombre)
                .filter(valor -> !valor.isBlank())
                .filter(valor -> valor.length() >= 3)
                .orElseThrow(() -> new IllegalArgumentException("El nombre debe tener mínimo 3 caracteres"));
    }

    private void validarCorreo(String correo) {

        Optional.ofNullable(correo)
                .filter(valor -> !valor.isBlank())
                .filter(valor -> valor.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                .orElseThrow(() -> new IllegalArgumentException("El correo no es válido"));
    }

    private void validarClave(String clave) {

        Optional.ofNullable(clave)
                .filter(valor -> !valor.isBlank())
                .filter(valor -> valor.length() >= 8)
                .orElseThrow(() -> new IllegalArgumentException("La clave debe tener mínimo 8 caracteres"));
    }

    private void validarRol(String rol) {

        Optional.ofNullable(rol)
                .filter(valor -> !valor.isBlank())
                .filter(valor -> valor.equals("ADMIN") || valor.equals("USER"))
                .orElseThrow(() -> new IllegalArgumentException("El rol debe ser ADMIN o USER"));
    }

    private void validarNumeroTelefonico(String numero) {

        Optional.ofNullable(numero)
                .filter(valor -> !valor.isBlank())
                .filter(valor -> valor.matches("\\d{10}"))
                .orElseThrow(() -> new IllegalArgumentException("El número telefónico debe tener 10 dígitos"));
    }


    private void validarEdad(Integer edad) {

        Optional.ofNullable(edad)
                .filter(valor -> valor >= 18)
                .filter(valor -> valor <= 120)
                .orElseThrow(() -> new IllegalArgumentException("La edad debe estar entre 18 y 120 años"));
    }



    public String login(String correo, String clave) {

        Optional.ofNullable(correo)
                .filter(valor -> !valor.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("El correo es obligatorio"));

        Optional.ofNullable(clave)
                .filter(valor -> !valor.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("La clave es obligatoria"));

        usuarioGateway.login(correo, clave);

        return "Credenciales válidas";
    }
}