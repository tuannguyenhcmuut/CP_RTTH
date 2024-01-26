package org.ut.server.authservice.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.ut.server.common.dtos.GenericResponseDTO;
import org.ut.server.common.dtos.user.UserRequestDTO;
import org.ut.server.common.dtos.user.UserResponseDTO;

import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8091", path = "/api/v1/user")
public interface UserFeign {
    @PostMapping("/create")
    public GenericResponseDTO<UserResponseDTO> createUser(@RequestBody UserRequestDTO newUser);
    @GetMapping("/username/{username}/id")
    public GenericResponseDTO<UUID> getUserIdByUsername(@PathVariable String username);
}
