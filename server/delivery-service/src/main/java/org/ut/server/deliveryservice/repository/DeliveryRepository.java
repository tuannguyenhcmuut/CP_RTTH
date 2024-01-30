package org.ut.server.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.deliveryservice.model.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
