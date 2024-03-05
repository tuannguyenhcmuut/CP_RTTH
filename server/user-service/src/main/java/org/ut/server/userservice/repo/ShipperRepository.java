package org.ut.server.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.userservice.model.Shipper;
import org.ut.server.userservice.model.ShopOwner;

import java.util.Optional;
import java.util.UUID;

public interface ShipperRepository extends JpaRepository<Shipper, UUID> {
    Optional<Shipper> findByAccount_Username(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
