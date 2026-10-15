package com.ecommerce.auth.Domain.usecase;

import com.ecommerce.auth.Domain.model.EstadoMovimientoSaldo;
import com.ecommerce.auth.Domain.model.MovimientoSaldo;
import com.ecommerce.auth.Domain.model.Usuario;
import com.ecommerce.auth.Domain.model.gateway.EncrypterGateway;
import com.ecommerce.auth.Domain.model.gateway.MovimientoSaldoGateway;
import com.ecommerce.auth.Domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;
    private final MovimientoSaldoGateway movimientoSaldoGateway;

    private void validarUsuario(Usuario usuario) {

        Usuario usuarioValidado = Optional.ofNullable(usuario)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Los datos del usuario son obligatorios"
                        )
                );

        validarNombre(usuarioValidado.getNombre());
        validarCorreo(usuarioValidado.getCorreo());
        validarClave(usuarioValidado.getClave());
        validarRol(usuarioValidado.getRol());
        validarNumeroTelefonico(
                usuarioValidado.getNumeroTelefonico()
        );
        validarEdad(usuarioValidado.getEdad());
    }

    public Usuario guardarUsuario(Usuario usuario) {

        validarUsuario(usuario);

        usuario.setSaldo(BigDecimal.ZERO);

        usuario.setClave(
                encrypterGateway.encrypt(
                        usuario.getClave()
                )
        );

        return usuarioGateway.guardarusuario(usuario);
    }

    public Usuario actualizarUsuario(Usuario usuario) {

        Usuario usuarioValidado = Optional.ofNullable(usuario)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Los datos del usuario son obligatorios"
                        )
                );

        validarIdUsuario(
                usuarioValidado.getIdUsuario()
        );

        validarUsuario(usuarioValidado);

        Usuario usuarioExistente =
                obtenerUsuarioExistente(
                        usuarioValidado.getIdUsuario()
                );

        usuarioValidado.setSaldo(
                obtenerSaldoActual(
                        usuarioExistente.getSaldo()
                )
        );

        usuarioValidado.setClave(
                encrypterGateway.encrypt(
                        usuarioValidado.getClave()
                )
        );

        return usuarioGateway.actualizarUsuario(
                usuarioValidado
        );
    }

    public Usuario buscarUsuario(String idUsuario) {

        validarIdUsuario(idUsuario);

        return obtenerUsuarioExistente(idUsuario);
    }

    public void eliminarUsuario(String idUsuario) {

        validarIdUsuario(idUsuario);

        obtenerUsuarioExistente(idUsuario);

        usuarioGateway.eliminarPorIdUsuario(
                idUsuario
        );
    }

    public BigDecimal consultarSaldo(
            String idUsuario
    ) {

        Usuario usuario = buscarUsuario(idUsuario);

        return obtenerSaldoActual(
                usuario.getSaldo()
        );
    }

    public Usuario recargarSaldo(
            String idUsuario,
            BigDecimal valor
    ) {

        validarIdUsuario(idUsuario);
        validarValorSaldo(valor);

        Usuario usuario = buscarUsuario(idUsuario);

        BigDecimal saldoActual =
                obtenerSaldoActual(
                        usuario.getSaldo()
                );

        BigDecimal nuevoSaldo =
                saldoActual.add(valor);

        usuario.setSaldo(nuevoSaldo);

        return usuarioGateway.actualizarUsuario(
                usuario
        );
    }

    @Transactional
    public Usuario descontarSaldo(
            String idUsuario,
            BigDecimal valor,
            String referenciaCompra
    ) {

        validarIdUsuario(idUsuario);
        validarValorSaldo(valor);
        validarReferenciaCompra(referenciaCompra);
        validarReferenciaNoProcesada(
                referenciaCompra
        );

        Usuario usuario = buscarUsuario(idUsuario);

        BigDecimal saldoActual =
                obtenerSaldoActual(
                        usuario.getSaldo()
                );

        validarSaldoSuficiente(
                saldoActual,
                valor
        );

        BigDecimal nuevoSaldo =
                saldoActual.subtract(valor);

        usuario.setSaldo(nuevoSaldo);

        Usuario usuarioActualizado =
                usuarioGateway.actualizarUsuario(
                        usuario
                );

        MovimientoSaldo movimientoSaldo =
                new MovimientoSaldo();

        movimientoSaldo.setIdUsuario(idUsuario);
        movimientoSaldo.setValor(valor);

        movimientoSaldo.setReferenciaCompra(
                referenciaCompra
        );

        movimientoSaldo.setEstado(
                EstadoMovimientoSaldo.DESCONTADO
        );

        movimientoSaldoGateway.guardarMovimiento(
                movimientoSaldo
        );

        return usuarioActualizado;
    }

    @Transactional
    public Usuario revertirDescuento(
            String idUsuario,
            String referenciaCompra
    ) {

        validarIdUsuario(idUsuario);
        validarReferenciaCompra(referenciaCompra);

        MovimientoSaldo movimientoSaldo =
                obtenerMovimientoExistente(
                        referenciaCompra
                );

        validarMovimientoDelUsuario(
                movimientoSaldo,
                idUsuario
        );

        validarMovimientoDescontado(
                movimientoSaldo
        );

        Usuario usuario = buscarUsuario(idUsuario);

        BigDecimal saldoActual =
                obtenerSaldoActual(
                        usuario.getSaldo()
                );

        BigDecimal nuevoSaldo =
                saldoActual.add(
                        movimientoSaldo.getValor()
                );

        usuario.setSaldo(nuevoSaldo);

        Usuario usuarioActualizado =
                usuarioGateway.actualizarUsuario(
                        usuario
                );

        movimientoSaldo.setEstado(
                EstadoMovimientoSaldo.REVERTIDO
        );

        movimientoSaldoGateway.guardarMovimiento(
                movimientoSaldo
        );

        return usuarioActualizado;
    }

    private Usuario obtenerUsuarioExistente(
            String idUsuario
    ) {

        return Optional.ofNullable(
                        usuarioGateway.buscarPorIdUsuario(
                                idUsuario
                        )
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un usuario con el ID: "
                                        + idUsuario
                        )
                );
    }

    private MovimientoSaldo obtenerMovimientoExistente(
            String referenciaCompra
    ) {

        return Optional.ofNullable(
                        movimientoSaldoGateway
                                .buscarPorReferenciaCompra(
                                        referenciaCompra
                                )
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un movimiento con la referencia: "
                                        + referenciaCompra
                        )
                );
    }

    private void validarIdUsuario(
            String idUsuario
    ) {

        Optional.ofNullable(idUsuario)
                .filter(id -> !id.isBlank())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El ID del usuario es obligatorio"
                        )
                );
    }

    private void validarReferenciaCompra(
            String referenciaCompra
    ) {

        Optional.ofNullable(referenciaCompra)
                .filter(valor -> !valor.isBlank())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La referencia de compra es obligatoria"
                        )
                );
    }

    private void validarReferenciaNoProcesada(
            String referenciaCompra
    ) {

        Optional.of(
                        movimientoSaldoGateway
                                .existeReferenciaCompra(
                                        referenciaCompra
                                )
                )
                .filter(existe -> !existe)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La compra ya fue procesada anteriormente"
                        )
                );
    }

    private void validarMovimientoDelUsuario(
            MovimientoSaldo movimientoSaldo,
            String idUsuario
    ) {

        Optional.ofNullable(
                        movimientoSaldo.getIdUsuario()
                )
                .filter(idMovimiento ->
                        idMovimiento.equals(idUsuario)
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El movimiento no pertenece al usuario"
                        )
                );
    }

    private void validarMovimientoDescontado(
            MovimientoSaldo movimientoSaldo
    ) {

        Optional.ofNullable(
                        movimientoSaldo.getEstado()
                )
                .filter(estado ->
                        estado
                                == EstadoMovimientoSaldo.DESCONTADO
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El descuento ya fue revertido"
                        )
                );
    }

    private void validarNombre(String nombre) {

        Optional.ofNullable(nombre)
                .filter(valor -> !valor.isBlank())
                .filter(valor -> valor.length() >= 3)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El nombre debe tener mínimo 3 caracteres"
                        )
                );
    }

    private void validarCorreo(String correo) {

        Optional.ofNullable(correo)
                .filter(valor -> !valor.isBlank())
                .filter(valor ->
                        valor.matches(
                                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
                        )
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El correo no es válido"
                        )
                );
    }

    private void validarClave(String clave) {

        Optional.ofNullable(clave)
                .filter(valor -> !valor.isBlank())
                .filter(valor -> valor.length() >= 8)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La clave debe tener mínimo 8 caracteres"
                        )
                );
    }

    private void validarRol(String rol) {

        Optional.ofNullable(rol)
                .filter(valor -> !valor.isBlank())
                .filter(valor ->
                        valor.equals("ADMIN")
                                || valor.equals("USER")
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El rol debe ser ADMIN o USER"
                        )
                );
    }

    private void validarNumeroTelefonico(
            String numero
    ) {

        Optional.ofNullable(numero)
                .filter(valor -> !valor.isBlank())
                .filter(valor ->
                        valor.matches("\\d{10}")
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El número telefónico debe tener 10 dígitos"
                        )
                );
    }

    private void validarEdad(Integer edad) {

        Optional.ofNullable(edad)
                .filter(valor -> valor >= 18)
                .filter(valor -> valor <= 120)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La edad debe estar entre 18 y 120 años"
                        )
                );
    }

    private void validarValorSaldo(
            BigDecimal valor
    ) {

        Optional.ofNullable(valor)
                .filter(monto ->
                        monto.compareTo(
                                BigDecimal.ZERO
                        ) > 0
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El valor debe ser mayor que cero"
                        )
                );
    }

    private void validarSaldoSuficiente(
            BigDecimal saldoActual,
            BigDecimal valor
    ) {

        Optional.ofNullable(saldoActual)
                .filter(saldo ->
                        saldo.compareTo(valor) >= 0
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Saldo insuficiente para realizar la compra"
                        )
                );
    }

    private BigDecimal obtenerSaldoActual(
            BigDecimal saldo
    ) {

        return Optional.ofNullable(saldo)
                .orElse(BigDecimal.ZERO);
    }

    public String login(
            String correo,
            String clave
    ) {

        Optional.ofNullable(correo)
                .filter(valor -> !valor.isBlank())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El correo es obligatorio"
                        )
                );

        Optional.ofNullable(clave)
                .filter(valor -> !valor.isBlank())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La clave es obligatoria"
                        )
                );

        usuarioGateway.login(correo, clave);

        return "Credenciales válidas";
    }
}