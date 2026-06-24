package com.fitting.shippingservice.controller;

import com.fitting.shippingservice.dto.ShippingRequest;
import com.fitting.shippingservice.dto.ShippingResponse;
import com.fitting.shippingservice.entity.ShippingStatus;
import com.fitting.shippingservice.service.ShippingService;
import com.fitting.shippingservice.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Envíos", description = "Gestión y seguimiento de envíos")
@RestController
@RequestMapping("/api/v1/shippings")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @Operation(summary = "Crear envío")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Envío creado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Ya existe un envío para esa orden")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ShippingResponse>> create(
            @Valid @RequestBody ShippingRequest request) {
        log.info("POST /api/v1/shippings — orden ID: {}", request.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Envío creado",
                        shippingService.create(request)));
    }

    @Operation(summary = "Listar envíos")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista obtenida")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ShippingResponse>>> findAll() {
        log.info("GET /api/v1/shippings");
        return ResponseEntity.ok(ApiResponse.ok("Lista de envíos",
                shippingService.findAll()));
    }

    @Operation(summary = "Buscar envío por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShippingResponse>> findById(@PathVariable Long id) {
        log.info("GET /api/v1/shippings/{}", id);
        return ResponseEntity.ok(ApiResponse.ok("Envío encontrado",
                shippingService.findById(id)));
    }

    @Operation(summary = "Buscar envío por orden")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<ShippingResponse>> findByOrderId(
            @PathVariable Long orderId) {
        log.info("GET /api/v1/shippings/order/{}", orderId);
        return ResponseEntity.ok(ApiResponse.ok("Envío de la orden",
                shippingService.findByOrderId(orderId)));
    }

    @Operation(summary = "Filtrar envíos por estado")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ShippingResponse>>> findByStatus(
            @PathVariable ShippingStatus status) {
        log.info("GET /api/v1/shippings/status/{}", status);
        return ResponseEntity.ok(ApiResponse.ok("Envíos por estado",
                shippingService.findByStatus(status)));
    }

    @Operation(summary = "Actualizar datos del envío")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShippingResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ShippingRequest request) {
        log.info("PUT /api/v1/shippings/{}", id);
        return ResponseEntity.ok(ApiResponse.ok("Envío actualizado",
                shippingService.update(id, request)));
    }

    @Operation(summary = "Actualizar estado del envío", description = "Ciclo: PREPARING → DISPATCHED → IN_TRANSIT → DELIVERED")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Transición de estado inválida")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ShippingResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam ShippingStatus status) {
        log.info("PATCH /api/v1/shippings/{}/status -> {}", id, status);
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado",
                shippingService.updateStatus(id, status)));
    }

    @Operation(summary = "Eliminar envío")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/shippings/{}", id);
        shippingService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Envío eliminado", null));
    }
}