package org.ut.server.omsserver.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.Delivery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findDeliveryByIdAndOrderId(Long deliveryId, Long orderId);

    Optional<Delivery> findByIdAndOrderId(Long id, Long orderId);
    List<Delivery> findAllByShipper_Id(UUID shipperId);
    List<Delivery> findAllByShipper_Id(UUID shipperId, Pageable pageable);
    Optional<Delivery> findByIdAndShipper_Id(Long deliveryId, UUID shipperId);
}
