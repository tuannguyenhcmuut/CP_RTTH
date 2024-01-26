package org.ut.server.common.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericRequestDTO<T> extends GenericDTO{
    private static final long serialVersionUID = 1L;
}
