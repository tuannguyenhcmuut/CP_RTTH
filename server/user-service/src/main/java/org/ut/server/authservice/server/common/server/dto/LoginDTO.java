package org.ut.server.authservice.server.common.server.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;
}