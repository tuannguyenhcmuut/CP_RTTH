package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ut.server.omsserver.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    //List<Address> findByUser_Id(Long user_id);
}
