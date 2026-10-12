package com.ecommerce.auth.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.auth.Domain.model.Usuario;
import com.ecommerce.auth.Domain.model.gateway.EncrypterGateway;
import com.ecommerce.auth.Domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.Infraestructure.driver_adapter.UsuarioData;
import com.ecommerce.auth.Infraestructure.mapper.MapperUsuario;
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
        UsuarioData usuarioGuardado = repository.save(usuarioData);

        return mapperUsuario.toUsuario(usuarioGuardado);
    }

    @Override
    public Usuario buscarPorIdUsuario(String idUsuario) {

        return repository.findById(idUsuario)
                .map(mapperUsuario::toUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {

        if (!repository.existsById(usuario.getIdUsuario())) {
            throw new RuntimeException("Usuario con ID " + usuario.getIdUsuario() + " no existe"
            );
        }

        UsuarioData usuarioData = mapperUsuario.toData(usuario);
        UsuarioData usuarioActualizado = repository.save(usuarioData);

        return mapperUsuario.toUsuario(usuarioActualizado);
    }

    @Override
    public void eliminarPorIdUsuario(String idUsuario) {

        if (!repository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario con ID " + idUsuario + " no existe");
        }

        repository.deleteById(idUsuario);
    }

    @Override
    public Usuario login(String correo, String clave) {

        UsuarioData usuarioData = repository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Boolean claveCorrecta = encrypterGateway.checkPass(clave, usuarioData.getClave());

        if (!claveCorrecta) {
            throw new RuntimeException("Clave incorrecta");
        }

        return mapperUsuario.toUsuario(usuarioData);
    }
}