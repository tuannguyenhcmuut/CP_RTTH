package org.ut.server.userservice.dto.request;


import lombok.Builder;
import lombok.Data;
import org.ut.server.userservice.dto.GenericDTO;

@Data
@Builder
public class GenericRequestDTO<T> extends GenericDTO {
    private static final long serialVersionUID = 1L;
}
