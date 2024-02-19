package org.ut.server.userservice.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.userservice.model.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
