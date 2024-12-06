package com.tienda.tienda.controller;

import com.tienda.tienda.model.Compra;
import com.tienda.tienda.service.CompraService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "http://localhost:4200")
public class CompraController {

    @Autowired
    private CompraService compraService;

    // Endpoint para registrar una compra
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addCompra(@Valid @RequestBody Compra compra) {
        Compra savedCompra = compraService.saveCompra(compra);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Compra registrada correctamente.");
        response.put("compra", savedCompra);

        return ResponseEntity.ok(response);
    }

    // Endpoint para obtener el historial de compras de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> getComprasByUsuarioId(@PathVariable Long usuarioId) {
        List<Compra> compras = compraService.findByUsuarioId(usuarioId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Se encontraron " + compras.size() + " compras para el usuario con ID: " + usuarioId);
        response.put("compras", compras);

        return ResponseEntity.ok(response);
    }

    // Endpoint para eliminar una compra por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCompra(@PathVariable Long id) {
        compraService.deleteCompra(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Compra eliminada correctamente.");
        response.put("compraId", String.valueOf(id));

        return ResponseEntity.ok(response);
    }

    // Endpoint para obtener todas las compras
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCompras() {
        List<Compra> compras = compraService.getAllCompras();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Se encontraron " + compras.size() + " compras en total.");
        response.put("compras", compras);

        return ResponseEntity.ok(response);
    }
}
