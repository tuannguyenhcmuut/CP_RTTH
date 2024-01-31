package org.ut.server.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.ut.server.authservice.model.ERole;
import org.ut.server.authservice.model.Role;
import org.ut.server.authservice.repository.RoleRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        createDefaultRoles();
    }

    private void createDefaultRoles() {
        createRoleIfNotExists(ERole.ROLE_ADMIN);
        createRoleIfNotExists(ERole.ROLE_SHOPOWNER);
        createRoleIfNotExists(ERole.ROLE_SHIPPER);
        createRoleIfNotExists(ERole.ROLE_EMPLOYEE);

        // Add more roles as needed
    }

    private void createRoleIfNotExists(ERole roleName) {
        Role existingRole = roleRepository.findByName(roleName).orElse(null);
        if (existingRole == null) {
            Role newRole = new Role();
            newRole.setId((long) roleName.ordinal() + 1);
            newRole.setName(roleName);
            roleRepository.save(newRole);
        }
    }
}