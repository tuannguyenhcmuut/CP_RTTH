package org.ut.server.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ut.server.userservice.model.Receiver;
import org.ut.server.userservice.model.ShopOwner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
    List<Receiver> findReceiversByShopOwner(ShopOwner shopOwner);
    Optional<Receiver> findReceiverByIdAndShopOwner_Id(Long receiverId, UUID userId);

}
