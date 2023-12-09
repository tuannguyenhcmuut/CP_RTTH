package com.ut.server.productservice.config;

import com.ut.server.productservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/v1/user")
public interface UserFeign {
    @GetMapping("")
    public List<UserDTO> getUser();
}
