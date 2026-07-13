package com.fitting.shippingservice.dto;

import com.fitting.shippingservice.entity.ShippingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos del envío retornados por la API")
public class ShippingResponse {

    @Schema(description = "ID del envío", example = "1")
    private Long id;

    @Schema(description = "ID de la orden", example = "1")
    private Long orderId;

    @Schema(description = "Dirección de entrega", example = "Av. Siempre Viva 123, Santiago")
    private String address;

    @Schema(description = "Estado del envío", example = "PREPARING",
            allowableValues = {"PREPARING","DISPATCHED","IN_TRANSIT","DELIVERED","RETURNED"})
    private ShippingStatus status;

    @Schema(description = "Fecha estimada de entrega", example = "2026-06-01")
    private LocalDate estimatedDate;

    @Schema(description = "Fecha de creación", example = "2026-05-22T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de actualización", example = "2026-05-22T11:00:00")
    private LocalDateTime updatedAt;
}