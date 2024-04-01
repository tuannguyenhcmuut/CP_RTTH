package org.ut.server.omsserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ut.server.omsserver.model.enums.OrderStatus;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusRequest {
    private OrderStatus newStatus;
}
