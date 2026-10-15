
package com.ecommerce.catalogo.Infraestructure.entry_points;

import com.ecommerce.catalogo.Domain.model.Carrito;
import com.ecommerce.catalogo.Domain.model.ItemCarrito;
import com.ecommerce.catalogo.Domain.usecase.CarritoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ecommerce/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoUseCase carritoUseCase;

    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<Carrito> crearCarrito(
            @PathVariable String idUsuario
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(carritoUseCase.crearCarrito(idUsuario));
    }

    @GetMapping("/{idCarrito}")
    public ResponseEntity<Carrito> buscarCarrito(
            @PathVariable Long idCarrito
    ) {
        return ResponseEntity.ok(
                carritoUseCase.buscarCarrito(idCarrito)
        );
    }

    @GetMapping("/usuario/{idUsuario}/activo")
    public ResponseEntity<Carrito> buscarCarritoActivo(
            @PathVariable String idUsuario
    ) {
        return ResponseEntity.ok(
                carritoUseCase.buscarCarritoActivo(idUsuario)
        );
    }

    @PostMapping("/{idCarrito}/productos")
    public ResponseEntity<Carrito> agregarProducto(
            @PathVariable Long idCarrito,
            @RequestBody ItemCarrito itemCarrito
    ) {
        return ResponseEntity.ok(
                carritoUseCase.agregarProducto(
                        idCarrito,
                        itemCarrito.getIdProducto(),
                        itemCarrito.getCantidad()
                )
        );
    }

    @PutMapping("/{idCarrito}/productos/{idProducto}/cantidad")
    public ResponseEntity<Carrito> cambiarCantidad(
            @PathVariable Long idCarrito,
            @PathVariable Long idProducto,
            @RequestBody Integer cantidad
    ) {
        return ResponseEntity.ok(
                carritoUseCase.cambiarCantidad(
                        idCarrito,
                        idProducto,
                        cantidad
                )
        );
    }

    @DeleteMapping("/{idCarrito}/productos/{idProducto}")
    public ResponseEntity<Carrito> retirarProducto(
            @PathVariable Long idCarrito,
            @PathVariable Long idProducto
    ) {
        return ResponseEntity.ok(
                carritoUseCase.retirarProducto(
                        idCarrito,
                        idProducto
                )
        );
    }
    @PostMapping("/{idCarrito}/comprar")
    public ResponseEntity<Carrito> comprarCarrito(
            @PathVariable Long idCarrito
    ) {

        return ResponseEntity.ok(
                carritoUseCase.comprarCarrito(
                        idCarrito
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(
            IllegalArgumentException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Map.of(
                                "error",
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> manejarJsonInvalido(
            HttpMessageNotReadableException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Map.of(
                                "error",
                                "El cuerpo de la petición no es válido"
                        )
                );
    }

}
