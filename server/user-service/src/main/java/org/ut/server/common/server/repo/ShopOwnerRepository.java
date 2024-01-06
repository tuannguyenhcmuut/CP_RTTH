package org.ut.server.common.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.common.server.model.ShopOwner;

import java.util.UUID;

public interface ShopOwnerRepository extends JpaRepository<ShopOwner, UUID> {

}
