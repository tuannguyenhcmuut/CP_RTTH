package org.ut.server.common.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.common.server.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
