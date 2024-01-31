package org.ut.server.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.userservice.model.Store;
import org.ut.server.userservice.model.User;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findStoresByUser(User user);
}
