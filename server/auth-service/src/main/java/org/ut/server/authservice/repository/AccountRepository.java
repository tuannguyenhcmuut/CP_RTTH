package org.ut.server.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.authservice.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findAccountByUsername(String username);
}
