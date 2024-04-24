package org.ut.server.omsserver.dto.request;

import lombok.Data;
import org.ut.server.omsserver.model.enums.DeliveryStatus;

@Data
public class DeliveryStatusRequest {
    private DeliveryStatus newStatus;
}
