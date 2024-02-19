package org.ut.server.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ut.server.userservice.model.enums.DiscountStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "discount")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_code")
    private String discountCode;

    @Column(name = "expired_date")
    private Date expiredDate;

    @Column(name = "discount_status")
    private DiscountStatus discountStatus;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "discount_type_id")
    private DiscountType discountType;
}
