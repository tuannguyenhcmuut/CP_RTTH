package org.ut.server.model;

import javax.persistence.*;

@MappedSuperclass
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected String username;

    @Column(nullable = false, length = 15)
    protected String phoneNumber;

    @Column(nullable = false)
    protected String address;

}
