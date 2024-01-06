package org.ut.server.common.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Store extends Person {
    private Boolean isDefault;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private ShopOwner shopOwner;
}
