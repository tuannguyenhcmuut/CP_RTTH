package org.ut.server.common.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ut.server.common.server.model.Receiver;
import org.ut.server.common.server.model.User;

import java.util.List;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
    List<Receiver> findReceiversByUser(User shopOwner);

}
