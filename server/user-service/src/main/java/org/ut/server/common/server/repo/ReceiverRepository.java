package org.ut.server.common.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ut.server.common.server.model.Receiver;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
}
