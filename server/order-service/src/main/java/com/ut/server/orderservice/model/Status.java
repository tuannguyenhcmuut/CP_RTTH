package com.ut.server.orderservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "status")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long code;

    @Column(columnDefinition = "varchar(255)")
    private String description;

}
