package org.ut.server.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ut.server.userservice.dto.GenericDTO;

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
