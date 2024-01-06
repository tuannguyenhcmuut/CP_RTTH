package org.ut.server.common.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String homeNumber;
    @Column(nullable = false, length = 50)
    private String street;
    @Column(nullable = false, length = 50)
    private String ward;
    @Column(nullable = false, length = 50)
    private String city;

    private String description;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
