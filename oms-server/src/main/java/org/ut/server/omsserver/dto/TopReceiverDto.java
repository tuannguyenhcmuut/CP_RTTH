package org.ut.server.omsserver.dto;


import lombok.Builder;
import lombok.Data;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.SqlResultSetMapping;


//@Entity
//@Data
//@SqlResultSetMapping(
//        name = "TopReceiverDtoMapping",
//        classes = @ConstructorResult(
//                targetClass = TopReceiverDto.class,
//                columns = {
//                        @ColumnResult(name = "receiver_name", type = String.class),
//                        @ColumnResult(name = "total_spending", type = Double.class)
//                }
//        )
//)
@Data
@Builder
public class TopReceiverDto {
    private String name;
    private Double totalAmount;
}
