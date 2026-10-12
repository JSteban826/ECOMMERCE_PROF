package com.ecommerce.auth.Infraestructure.driver_adapter.jpa_repository;
import com.ecommerce.auth.Domain.model.Usuario;
import com.ecommerce.auth.Domain.model.gateway.EncrypterGateway;
import com.ecommerce.auth.Domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.Infraestructure.mapper.MapperUsuario;

import com.ecommerce.auth.Infraestructure.driver_adapter.UsuarioData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class UsuarioDataGatewayImpl implements UsuarioGateway {

    private final MapperUsuario mapperUsuario;
    private final UsuarioDataJpaRepository repository;
    private final EncrypterGateway encrypterGateway;

    @Override
    public Usuario guardarusuario(Usuario usuario) {
        UsuarioData usuarioData = mapperUsuario.toData(usuario);
        return mapperUsuario.toUsuario(repository.save(usuarioData));
    }

    @Override
    public Usuario buscarPorIdUsuario(String idUsuario) {
        return repository.findById(idUsuario)
                .map(usuarioData -> mapperUsuario.toUsuario(usuarioData))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        UsuarioData usuarioData = mapperUsuario.toData(usuario);

        if(!repository.existsById(usuario.getIdUsuario())){
            throw new RuntimeException("Usuario con id " + usuario.getIdUsuario() + " no existe");
        }
        return mapperUsuario.toUsuario(repository.save(usuarioData));
    }

    @Override
    public void eliminarPorIdUsuario(String idUsuario) {
        try {
            repository.deleteById(idUsuario);
        } catch (Exception error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    @Override
    public Usuario login(String correo, String clave) {

        UsuarioData usuarioData = repository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        if (!encrypterGateway.checkPass(
                clave,
                usuarioData.getClave()
        )) {
            throw new RuntimeException("Clave incorrecta");
        }

        return mapperUsuario.toUsuario(usuarioData);
    }
}
