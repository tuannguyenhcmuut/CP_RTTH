package org.ut.server.common.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@AllArgsConstructor
@Getter
public class UserVO {
    private UUID id;
    private String email;
    private String password;
}
