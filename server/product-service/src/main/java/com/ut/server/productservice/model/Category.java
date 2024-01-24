package com.ut.server.productservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    private String description;

    @Column(name = "user_id")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID userId;

    //bi-directional many-to-many association to Product
    @ManyToMany(mappedBy = "categories")
    @JsonBackReference
    private List<Product> products;

}
