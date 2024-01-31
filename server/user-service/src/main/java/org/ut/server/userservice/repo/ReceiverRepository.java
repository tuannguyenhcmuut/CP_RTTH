package org.ut.server.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ut.server.userservice.model.Receiver;
import org.ut.server.userservice.model.User;

import java.util.List;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
    List<Receiver> findReceiversByUser(User shopOwner);

}
