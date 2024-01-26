package com.ut.server.productservice.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.ut.server.common.dtos.GenericResponseDTO;
import org.ut.server.common.dtos.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.ut.server.common.dtos.user.UserResponseDTO;

import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8091", path = "/api/v1/user")
public interface UserFeign {
    @GetMapping("/{userId}")
    public GenericResponseDTO<UserResponseDTO> getUserInfo(@PathVariable UUID userId);
}
