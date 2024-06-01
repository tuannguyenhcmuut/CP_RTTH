package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.omsserver.model.Address;
import org.ut.server.omsserver.model.Receiver;
import org.ut.server.omsserver.model.Store;
import org.ut.server.omsserver.model.enums.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ShopOwnerDto {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Gender gender;
    private LocalDate dateOfBirth;
    private List<Address> addresses;
    private List<Receiver> receivers;
    private List<Store> stores;

    @Data
    @Builder
    public static class DeliveryDto {
        private Long id;
        private Long orderId;
        private UUID shipperId;
        private DeliveryStatus status;
        private String shipperName;
        private String shipperPhone;
        private Payer payer;
        private boolean hasLostInsurance;
        private boolean isCollected;
        private DeliveryMethod deliveryMethod;
        private LuuKho luuKho;
        private LayHang layHang;
        private GiaoHang giaoHang;
        private Long shippingFee;
        private Float collectionFee;
        private Boolean isDraft;
        private String note;
        private Date deliveryDate;
        private Date receivedDate;
    }
}
