package org.ut.server.omsserver.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String message;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User receiver;
    private boolean isRead;
//    notification type
    private String type;
    private LocalDateTime createdAt;
}
