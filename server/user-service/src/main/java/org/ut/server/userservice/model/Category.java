package org.ut.server.userservice.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    private String description;

    @NotNull
    private UUID userId;

//    //un-directional many-to-many association to Product
//    @ManyToMany(mappedBy = "categories")
//    @JsonBackReference
//    private List<Product> products;

}
