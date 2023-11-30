package com.ut.server.orderservice.repo;

import com.ut.server.orderservice.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

}
