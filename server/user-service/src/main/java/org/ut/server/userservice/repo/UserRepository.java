package org.ut.server.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.userservice.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID id);
//    Optional<User> findByAccount(String username);
    Optional<User> findUserByEmail(String email);
    // find by phone
    Optional<User> findUserByPhoneNumber(String phoneNumber);

    Optional<User> findByAccount_Username(String username);
}
