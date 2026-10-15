package com.ecommerce.catalogo.Infraestructure.entry_points;
import com.ecommerce.catalogo.Domain.model.Producto;
import com.ecommerce.catalogo.Domain.usecase.ProductoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoUseCase productoUseCase;
    @PostMapping("/save")
    public ResponseEntity<Producto> guardarProducto(@RequestBody Producto producto) {
        return ResponseEntity.status(201).body(productoUseCase.guardarProducto(producto));
    }
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoUseCase.listarProductos());
    }
    @GetMapping("/{idProducto}")
    public ResponseEntity<Producto> buscarProducto(@PathVariable("idProducto") Long idProducto) {
        return ResponseEntity.ok(productoUseCase.buscarProducto(idProducto));
    }
    @PutMapping("/{idProducto}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable("idProducto") Long idProducto,
            @RequestBody Producto producto) {
        producto.setIdProducto(idProducto);
        return ResponseEntity.ok(productoUseCase.actualizarProducto(producto));
    }
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable("idProducto") Long idProducto) {
        productoUseCase.eliminarProducto(idProducto);
        return ResponseEntity.noContent().build();
    }
}
