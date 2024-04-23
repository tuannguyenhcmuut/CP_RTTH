package org.ut.server.omsserver.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ut.server.omsserver.model.Receiver;
import org.ut.server.omsserver.model.ShopOwner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
    List<Receiver> findReceiversByShopOwner(ShopOwner shopOwner);
    List<Receiver> findReceiversByShopOwner(ShopOwner shopOwner, Pageable pageable);
    Optional<Receiver> findReceiverByIdAndShopOwner_Id(Long receiverId, UUID userId);

    Optional<Receiver> findByIdAndShopOwner_Id(Long receiverId, UUID userId);
}
