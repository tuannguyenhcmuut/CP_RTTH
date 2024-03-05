package org.ut.server.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.userservice.model.ShopOwner;
import org.ut.server.userservice.model.User;

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
