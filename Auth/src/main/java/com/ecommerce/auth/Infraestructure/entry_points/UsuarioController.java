package com.ecommerce.auth.Infraestructure.entry_points;
import com.ecommerce.auth.Domain.model.Usuario;
import com.ecommerce.auth.Domain.model.MovimientoSaldo;
import com.ecommerce.auth.Domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.Infraestructure.entry_points.dto.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ecommerce/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioUseCase usuarioUseCase;
    @PostMapping("/save")
    public ResponseEntity<UsuarioResponse> guardarUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioUseCase.guardarUsuario(usuario)));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(@PathVariable("id") String id) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioUseCase.buscarUsuario(id)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable("id") String id,
            @RequestBody Usuario usuario) {
        usuario.setIdUsuario(id);
        return ResponseEntity.ok(UsuarioResponse.from(usuarioUseCase.actualizarUsuario(usuario)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable("id") String id) {
        usuarioUseCase.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioUseCase.login(usuario.getCorreo(), usuario.getClave()));
    }
    @GetMapping("/{idUsuario}/saldo")
    public ResponseEntity<BigDecimal> consultarSaldo(@PathVariable("idUsuario") String idUsuario) {
        return ResponseEntity.ok(usuarioUseCase.consultarSaldo(idUsuario));
    }
    @PutMapping("/{idUsuario}/saldo/recargar")
    public ResponseEntity<UsuarioResponse> recargarSaldo(@PathVariable("idUsuario") String idUsuario,
            @RequestBody BigDecimal valor) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioUseCase.recargarSaldo(idUsuario, valor)));
    }
    @PutMapping("/{idUsuario}/saldo/descontar")
    public ResponseEntity<UsuarioResponse> descontarSaldo(@PathVariable("idUsuario") String idUsuario,
            @RequestBody MovimientoSaldo movimiento) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioUseCase.descontarSaldo(idUsuario,
                movimiento.getValor(), movimiento.getReferenciaCompra())));
    }
    @PutMapping("/{idUsuario}/saldo/revertir")
    public ResponseEntity<UsuarioResponse> revertirDescuento(@PathVariable("idUsuario") String idUsuario,
            @RequestBody MovimientoSaldo movimiento) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioUseCase.revertirDescuento(idUsuario,
                movimiento.getReferenciaCompra())));
    }
}
