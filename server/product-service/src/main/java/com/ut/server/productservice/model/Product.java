package com.ut.server.productservice.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String description;

    private BigDecimal price;
    private String photo;

    @Column(name = "user_id")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID userId;  // mapping to inventory-service

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    // bidirectional many-to-many association to Category
    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
//    @JsonManagedReference
    private List<Category> categories;

    private Float height;
    private Float width;
    private Float depth;

}
