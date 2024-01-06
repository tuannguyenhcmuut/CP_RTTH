package com.ut.server.productservice.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.ut.server.common.dtos.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8080", path = "/api/v1/user")
public interface UserFeign {
    @GetMapping("/{user_id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID user_id);
}
