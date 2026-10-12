package com.ecommerce.auth.Infraestructure.mapper;

import com.ecommerce.auth.Domain.model.Usuario;
import com.ecommerce.auth.Infraestructure.driver_adapter.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class MapperUsuario {

    public Usuario toUsuario(UsuarioData usuarioData){

        Usuario usuario = new Usuario();

        usuario.setIdUsuario(usuarioData.getIdUsuario());
        usuario.setNombre(usuarioData.getNombre());
        usuario.setCorreo(usuarioData.getCorreo());
        usuario.setClave(usuarioData.getClave());
        usuario.setRol(usuarioData.getRol());
        usuario.setEdad(usuarioData.getEdad());
        usuario.setNumeroTelefonico(usuarioData.getNumeroTelefonico());

        return usuario;
    }

    public UsuarioData toData(Usuario usuario){

        UsuarioData usuarioData = new UsuarioData();

        usuarioData.setIdUsuario(usuario.getIdUsuario());
        usuarioData.setNombre(usuario.getNombre());
        usuarioData.setCorreo(usuario.getCorreo());
        usuarioData.setClave(usuario.getClave());
        usuarioData.setRol(usuario.getRol());
        usuarioData.setEdad(usuario.getEdad());
        usuarioData.setNumeroTelefonico(usuario.getNumeroTelefonico());

        return usuarioData;
    }
}