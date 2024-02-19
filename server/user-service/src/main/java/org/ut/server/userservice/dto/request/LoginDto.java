package org.ut.server.userservice.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    private String password;
}
