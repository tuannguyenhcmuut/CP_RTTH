package org.ut.server.common.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.common.server.model.Store;
import org.ut.server.common.server.model.User;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findStoresByUser(User user);
}
