package org.ut.server.omsserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Entity
@MappedSuperclass
@Data
//@Builder
@AllArgsConstructor
public class Person {


    @Column(nullable = false)
    protected String name;

    @Column(nullable = false, length = 15)
    protected String phoneNumber;

    @Column(nullable = false)
    protected String address;

    @Column(name ="detailed_address" ,nullable = true)
    private String detailedAddress;

    // CREATED DATE
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Person() {

    }
    public Person(String name, String phoneNumber, String address, String detailedAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.detailedAddress = detailedAddress;
    }


}
