package com.ut.server.orderservice.repo;

import com.ut.server.orderservice.model.OrderOptionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionTypeRepository extends JpaRepository<OrderOptionType, Long> {
}
