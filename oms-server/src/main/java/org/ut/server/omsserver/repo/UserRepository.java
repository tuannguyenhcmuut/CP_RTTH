package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserById(UUID id);
//    Optional<User> findByAccount(String username);
    Optional<User> findUserByEmail(String email);
    // find by phone
    Optional<User> findUserByPhoneNumber(String phoneNumber);
    // find by username
    Optional<User> findUserByAccount_Username(String username);

//    Optional<User> findByAccount_Username(String username);
}
