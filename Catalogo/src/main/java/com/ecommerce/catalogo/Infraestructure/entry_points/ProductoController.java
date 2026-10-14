package com.ecommerce.catalogo.Infraestructure.entry_points;

import com.ecommerce.catalogo.Domain.model.Producto;
import com.ecommerce.catalogo.Domain.usecase.ProductoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ecommerce/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoUseCase productoUseCase;

    @PostMapping("/save")
    public ResponseEntity<Producto> guardarProducto(
            @RequestBody Producto producto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        productoUseCase.guardarProducto(producto)
                );
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {

        return ResponseEntity.ok(
                productoUseCase.listarProductos()
        );
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<Producto> buscarProducto(
            @PathVariable Long idProducto
    ) {

        return ResponseEntity.ok(
                productoUseCase.buscarProducto(idProducto)
        );
    }

    @PutMapping("/{idProducto}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long idProducto,
            @RequestBody Producto producto
    ) {

        producto.setIdProducto(idProducto);

        return ResponseEntity.ok(
                productoUseCase.actualizarProducto(producto)
        );
    }

    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long idProducto
    ) {

        productoUseCase.eliminarProducto(idProducto);

        return ResponseEntity.noContent().build();
    }

    /*
     * Maneja las validaciones generadas en ProductoUseCase.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>>
    manejarValidacion(
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

    /*
     * Maneja JSON inválido o tipos incorrectos.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>>
    manejarJsonInvalido(
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
