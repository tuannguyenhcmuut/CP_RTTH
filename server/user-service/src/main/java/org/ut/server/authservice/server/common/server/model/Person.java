package org.ut.server.authservice.server.common.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

//@Entity
@MappedSuperclass
@Data
//@Builder
@AllArgsConstructor
public class Person {


    @Column(nullable = true)
    protected String username;

    @Column(nullable = false, length = 15)
    protected String phoneNumber;

    @Column(nullable = false)
    protected String address;

    public Person() {

    }


}
