package org.ut.server.authservice.server.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponseDTO<T> extends GenericDTO {
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;
    private T data;
    private Date timestamps;
}
