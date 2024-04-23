package org.ut.server.omsserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

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

    public Person() {

    }


}
