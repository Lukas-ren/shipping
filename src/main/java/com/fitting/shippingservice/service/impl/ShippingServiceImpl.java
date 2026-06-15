package com.fitting.shippingservice.service.impl;

import com.fitting.shippingservice.dto.ShippingRequest;
import com.fitting.shippingservice.dto.ShippingResponse;
import com.fitting.shippingservice.entity.Shipping;
import com.fitting.shippingservice.entity.ShippingStatus;
import com.fitting.shippingservice.exception.BusinessException;
import com.fitting.shippingservice.exception.ResourceNotFoundException;
import com.fitting.shippingservice.repository.ShippingRepository;
import com.fitting.shippingservice.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository shippingRepository;

    @Override
    @Transactional
    public ShippingResponse create(ShippingRequest request) {
        log.info("Creando envío para orden ID: {}", request.getOrderId());

        if (shippingRepository.existsByOrderId(request.getOrderId())) {
            throw new BusinessException(
                    "Ya existe un envío registrado para la orden ID: " + request.getOrderId());
        }

        Shipping shipping = Shipping.builder()
                .orderId(request.getOrderId())
                .address(request.getAddress())
                .status(ShippingStatus.PREPARING)
                .estimatedDate(request.getEstimatedDate())
                .build();

        Shipping saved = shippingRepository.save(shipping);
        log.info("Envío creado con ID: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ShippingResponse findById(Long id) {
        log.debug("Buscando envío con ID: {}", id);
        return toResponse(getShippingOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ShippingResponse findByOrderId(Long orderId) {
        log.debug("Buscando envío para orden ID: {}", orderId);
        Shipping shipping = shippingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Envío para orden ID " + orderId + " no encontrado"));
        return toResponse(shipping);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShippingResponse> findAll() {
        log.debug("Listando todos los envíos");
        return shippingRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShippingResponse> findByStatus(ShippingStatus status) {
        log.debug("Buscando envíos con estado: {}", status);
        return shippingRepository.findByStatus(status).stream()
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public ShippingResponse update(Long id, ShippingRequest request) {
        log.info("Actualizando envío con ID: {}", id);

        Shipping shipping = getShippingOrThrow(id);

        // Si cambió el orderId, verificar que no esté tomado por otro envío
        if (!shipping.getOrderId().equals(request.getOrderId())
                && shippingRepository.existsByOrderId(request.getOrderId())) {
            throw new BusinessException(
                    "Ya existe un envío registrado para la orden ID: " + request.getOrderId());
        }

        shipping.setOrderId(request.getOrderId());
        shipping.setAddress(request.getAddress());
        shipping.setEstimatedDate(request.getEstimatedDate());

        return toResponse(shippingRepository.save(shipping));
    }

    @Override
    @Transactional
    public ShippingResponse updateStatus(Long id, ShippingStatus newStatus) {
        log.info("Actualizando estado de envío ID {} a {}", id, newStatus);

        Shipping shipping = getShippingOrThrow(id);
        validateStatusTransition(shipping.getStatus(), newStatus);
        shipping.setStatus(newStatus);

        Shipping updated = shippingRepository.save(shipping);
        log.info("Envío {} actualizado a estado: {}", updated.getId(), newStatus);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Eliminando envío con ID: {}", id);
        if (!shippingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Envío", id);
        }
        shippingRepository.deleteById(id);
        log.info("Envío eliminado: {}", id);
    }

    // ── Validación de transición de estados ─────────────────────────────────────

    private void validateStatusTransition(ShippingStatus current, ShippingStatus next) {
        boolean valid = switch (current) {
            case PREPARING  -> next == ShippingStatus.DISPATCHED;
            case DISPATCHED -> next == ShippingStatus.IN_TRANSIT;
            case IN_TRANSIT -> next == ShippingStatus.DELIVERED || next == ShippingStatus.RETURNED;
            default         -> false;
        };
        if (!valid) {
            throw new BusinessException(String.format(
                    "Transición de estado inválida: %s → %s", current, next));
        }
    }

    // ── Helper repository ───────────────────────────────────────────────────────

    private Shipping getShippingOrThrow(Long id) {
        return shippingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío", id));
    }

    // ── Mapper interno ──────────────────────────────────────────────────────────

    private ShippingResponse toResponse(Shipping s) {
        return ShippingResponse.builder()
                .id(s.getId())
                .orderId(s.getOrderId())
                .address(s.getAddress())
                .status(s.getStatus())
                .estimatedDate(s.getEstimatedDate())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}