package com.ecommerce.auth.Infraestructure.entry_points;

import com.ecommerce.auth.Domain.model.Usuario;

import com.ecommerce.auth.Domain.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecommerce/usuarios")
@RequiredArgsConstructor
public class UsuarioController {


    private final UsuarioUseCase usuarioUseCase;

    @PostMapping("/save")
    public Usuario guardarUsuario(@RequestBody Usuario usuario) {
        return usuarioUseCase.guardarUsuario(usuario);
    }

    @GetMapping("/{id}")
    public Usuario buscarUsuario(@PathVariable String id) {
        return usuarioUseCase.buscarUsuario(id);
    }

    @PutMapping("/{id}")
    public Usuario actualizarUsuario(
            @PathVariable String id,
            @RequestBody Usuario usuario) {

        usuario.setIdUsuario(id);
        return usuarioUseCase.actualizarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    public String eliminarUsuario(@PathVariable String id) {
        usuarioUseCase.eliminarUsuario(id);
        return "Usuario eliminado correctamente";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {

        try {

            String mensaje = usuarioUseCase.login(
                    usuario.getCorreo(),
                    usuario.getClave()
            );

            return new ResponseEntity<>(mensaje, HttpStatus.OK);

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );

        } catch (RuntimeException e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}


