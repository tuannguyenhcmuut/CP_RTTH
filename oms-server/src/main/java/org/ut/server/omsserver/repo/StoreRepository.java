package org.ut.server.omsserver.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.Store;
import org.ut.server.omsserver.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findStoresByShopOwner(User user);
    List<Store> findStoresByShopOwner(User user, Pageable pageable);
    Optional<Store> findStoreByIdAndShopOwner_Id(Long storeId, UUID userId);

    Optional<Store> findByIdAndShopOwner_Id(Long storeId, UUID userId);
}
