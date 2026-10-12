package com.ecommerce.auth.Domain.model.gateway;

import com.ecommerce.auth.Domain.model.Usuario;

public interface UsuarioGateway {

    Usuario guardarusuario(Usuario usuario);

    Usuario buscarPorIdUsuario(String idUsuario);

    Usuario actualizarUsuario(Usuario usuario);

    void eliminarPorIdUsuario(String idUsuario);

    Usuario login(String correo, String clave);
}