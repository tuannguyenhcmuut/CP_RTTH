package org.ut.server.omsserver.model;

import lombok.*;
import org.ut.server.omsserver.model.enums.ProductStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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

//    @Lob
//    @Type(type="org.hibernate.type.BinaryType")
    private String photoUrl;


//    @Column(name = "user_id")
//    @Type(type="org.hibernate.type.PostgresUUIDType")
//    private UUID userId;  // mapping to inventory-service
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ShopOwner shopOwner;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    // bidirectional many-to-many association to Category
    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    private Float weight;
    private Float height;
    private Float width;
    private Float length;

}
