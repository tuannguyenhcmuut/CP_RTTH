package org.ut.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserVO {
    private UUID id;
    private String email;
    private String password;


}
