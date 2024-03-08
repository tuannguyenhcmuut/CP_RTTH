package org.ut.server.omsserver.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
