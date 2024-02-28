package org.ut.server.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ut.server.userservice.model.enums.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    @Column(name = "order_id",nullable = false)
//    private Long orderId;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "shipper_id")
    private UUID shipperId;


    @Column(name = "status")
    private DeliveryStatus status;

    @Column(name = "shipper_name")
    private String shipperName;

    @Column(name = "shipper_phone")
    private String shipperPhone;

    @Column(name = "payer")
    private Payer payer;

    @Column(name = "has_lost_ensurance")
    private boolean hasLostInsurance;

    @Column(name = "is_collected")
    private boolean isCollected;

    private DeliveryMethod deliveryMethod;

    private LuuKho luuKho;

    private LayHang layHang;

    private GiaoHang giaoHang;

    @Column(name = "shipping_fee")
    private Long shippingFee;
    @Column(name = "collection_fee")
    private Float collectionFee;

    private Boolean isDraft;

    private String note;

    private Date deliveryDate;

    private Date receivedDate;



}