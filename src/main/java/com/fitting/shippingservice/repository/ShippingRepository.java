package com.fitting.shippingservice.repository;

import com.fitting.shippingservice.entity.Shipping;
import com.fitting.shippingservice.entity.ShippingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    Optional<Shipping> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);

    List<Shipping> findByStatus(ShippingStatus status);
}