package com.ut.server.orderservice.repo;

import com.ut.server.orderservice.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
