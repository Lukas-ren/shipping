package com.fitting.shippingservice.entity;

public enum ShippingStatus {
    PREPARING,   // preparando el paquete
    DISPATCHED,  // despachado al transportista
    IN_TRANSIT,  // en camino
    DELIVERED,   // entregado
    RETURNED     // devuelto
}