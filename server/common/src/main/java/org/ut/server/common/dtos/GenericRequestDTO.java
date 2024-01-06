package org.ut.server.common.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class GenericRequestDTO<T> extends GenericDTO{
    private static final long serialVersionUID = 1L;
}
