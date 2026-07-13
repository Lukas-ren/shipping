package com.fitting.shippingservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos para registrar un envío")
public class ShippingRequest {

    @Schema(description = "ID de la orden", example = "1")
    @NotNull(message = "El ID de la orden es obligatorio")
    private Long orderId;

    @Schema(description = "Dirección de entrega", example = "Av. Siempre Viva 123, Santiago")
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255)
    private String address;

    @Schema(description = "Fecha estimada de entrega", example = "2026-06-01")
    @NotNull(message = "La fecha estimada es obligatoria")
    @Future
    private LocalDate estimatedDate;
}