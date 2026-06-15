package com.fitting.shippingservice.service;

import com.fitting.shippingservice.dto.ShippingRequest;
import com.fitting.shippingservice.dto.ShippingResponse;
import com.fitting.shippingservice.entity.ShippingStatus;

import java.util.List;

public interface ShippingService {

    ShippingResponse create(ShippingRequest request);

    ShippingResponse findById(Long id);

    ShippingResponse findByOrderId(Long orderId);

    List<ShippingResponse> findAll();

    List<ShippingResponse> findByStatus(ShippingStatus status);

    ShippingResponse update(Long id, ShippingRequest request);

    ShippingResponse updateStatus(Long id, ShippingStatus status);

    void delete(Long id);
}