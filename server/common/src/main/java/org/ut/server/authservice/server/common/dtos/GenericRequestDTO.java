package org.ut.server.authservice.server.common.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericRequestDTO<T> extends GenericDTO{
    private static final long serialVersionUID = 1L;
}
