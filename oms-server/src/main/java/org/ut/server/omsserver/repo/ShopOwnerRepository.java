package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.ShopOwner;

import java.util.Optional;
import java.util.UUID;

public interface ShopOwnerRepository extends JpaRepository<ShopOwner, UUID> {
    Optional<ShopOwner> findShopOwnerById(UUID id);
    Optional<ShopOwner> findShopOwnerByPhoneNumber(String phoneNumber);
    Optional<ShopOwner> findShopOwnerByEmail(String email);
    //    Optional<User> findByAccount(String username);
    Optional<ShopOwner> findUserByEmail(String email);
    // find by phone
    Optional<ShopOwner> findUserByPhoneNumber(String phoneNumber);

    Optional<ShopOwner> findByAccount_Username(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
