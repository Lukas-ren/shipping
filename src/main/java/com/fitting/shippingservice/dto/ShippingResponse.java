package com.fitting.shippingservice.dto;

import com.fitting.shippingservice.entity.ShippingStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingResponse {

    private Long id;
    private Long orderId;
    private String address;
    private ShippingStatus status;
    private LocalDate estimatedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}