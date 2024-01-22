package org.ut.server.authservice.server.common.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.authservice.server.common.server.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
