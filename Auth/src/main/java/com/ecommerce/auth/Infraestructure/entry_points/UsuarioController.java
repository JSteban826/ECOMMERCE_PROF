package com.ecommerce.auth.Infraestructure.entry_points;

import com.ecommerce.auth.Domain.model.Usuario;

import com.ecommerce.auth.Domain.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ecommerce/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    @PostMapping("/save")
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {

        Usuario usuarioGuardado = usuarioUseCase.guardarUsuario(usuario);

        return new ResponseEntity<>(usuarioGuardado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable String id) {

        Usuario usuario = usuarioUseCase.buscarUsuario(id);

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable String id, @RequestBody Usuario usuario) {

        usuario.setIdUsuario(id);

        Usuario usuarioActualizado = usuarioUseCase.actualizarUsuario(usuario);

        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable String id) {

        usuarioUseCase.eliminarUsuario(id);

        return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {

        try {

            String mensaje = usuarioUseCase.login(
                    usuario.getCorreo(),
                    usuario.getClave()
            );

            return new ResponseEntity<>(mensaje, HttpStatus.OK
            );

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST
            );

        } catch (RuntimeException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED
            );
        }
    }
    @GetMapping("/{idUsuario}/saldo")
    public ResponseEntity<BigDecimal> consultarSaldo(
            @PathVariable String idUsuario
    ) {
        return ResponseEntity.ok(
                usuarioUseCase.consultarSaldo(idUsuario)
        );
    }

    @PutMapping("/{idUsuario}/saldo/recargar")
    public ResponseEntity<Usuario> recargarSaldo(
            @PathVariable String idUsuario,
            @RequestBody BigDecimal valor
    ) {
        return ResponseEntity.ok(
                usuarioUseCase.recargarSaldo(
                        idUsuario,
                        valor
                )
        );
    }

    @PutMapping("/{idUsuario}/saldo/descontar")
    public ResponseEntity<Usuario> descontarSaldo(
            @PathVariable String idUsuario,
            @RequestBody BigDecimal valor
    ) {
        return ResponseEntity.ok(
                usuarioUseCase.descontarSaldo(
                        idUsuario,
                        valor
                )
        );
    }


}

