package com.fitting.shippingservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingRequest {

    @NotNull(message = "El ID de la orden es obligatorio")
    private Long orderId;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
    private String address;

    @NotNull(message = "La fecha estimada es obligatoria")
    @Future(message = "La fecha estimada debe ser una fecha futura")
    private LocalDate estimatedDate;
}