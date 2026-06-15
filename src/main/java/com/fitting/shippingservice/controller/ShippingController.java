package com.fitting.shippingservice.controller;

import com.fitting.shippingservice.dto.ShippingRequest;
import com.fitting.shippingservice.dto.ShippingResponse;
import com.fitting.shippingservice.entity.ShippingStatus;
import com.fitting.shippingservice.service.ShippingService;
import com.fitting.shippingservice.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/shippings")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShippingResponse>> create(
            @Valid @RequestBody ShippingRequest request) {
        log.info("POST /api/v1/shippings — orden ID: {}", request.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Envío creado",
                        shippingService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShippingResponse>>> findAll() {
        log.info("GET /api/v1/shippings");
        return ResponseEntity.ok(ApiResponse.ok("Lista de envíos",
                shippingService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShippingResponse>> findById(@PathVariable Long id) {
        log.info("GET /api/v1/shippings/{}", id);
        return ResponseEntity.ok(ApiResponse.ok("Envío encontrado",
                shippingService.findById(id)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<ShippingResponse>> findByOrderId(
            @PathVariable Long orderId) {
        log.info("GET /api/v1/shippings/order/{}", orderId);
        return ResponseEntity.ok(ApiResponse.ok("Envío de la orden",
                shippingService.findByOrderId(orderId)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ShippingResponse>>> findByStatus(
            @PathVariable ShippingStatus status) {
        log.info("GET /api/v1/shippings/status/{}", status);
        return ResponseEntity.ok(ApiResponse.ok("Envíos por estado",
                shippingService.findByStatus(status)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShippingResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ShippingRequest request) {
        log.info("PUT /api/v1/shippings/{}", id);
        return ResponseEntity.ok(ApiResponse.ok("Envío actualizado",
                shippingService.update(id, request)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ShippingResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam ShippingStatus status) {
        log.info("PATCH /api/v1/shippings/{}/status -> {}", id, status);
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado",
                shippingService.updateStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/shippings/{}", id);
        shippingService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Envío eliminado", null));
    }
}