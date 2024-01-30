package org.ut.server.common.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ut.server.common.server.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    //List<Address> findByUser_Id(Long user_id);
}
