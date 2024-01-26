package org.ut.server.common.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.common.server.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findUserByEmail(String email);
    // find by phone
    Optional<User> findUserByPhoneNumber(String phoneNumber);
}
