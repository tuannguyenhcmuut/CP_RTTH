package org.ut.server.omsserver.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_history")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_history_id")
    private Long id;

//    @Column(name = "action_type")
//    private String actionType;

//    @CreationTimestamp
    @Column(name = "action_date")
    private LocalDateTime actionDate;

//    @Column(name = "action_by")
//    private String actionBy;

    @Column(name = "description")
    private String description;

    @Column(name = "order_id")
    private Long orderId;
}
