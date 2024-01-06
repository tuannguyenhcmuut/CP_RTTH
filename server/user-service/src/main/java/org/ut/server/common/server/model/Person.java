package org.ut.server.common.server.model;

import javax.persistence.*;

@Entity
//@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @Column(nullable = false)
    protected String username;

    @Column(nullable = false, length = 15)
    protected String phoneNumber;

    @Column(nullable = false)
    protected String address;

}
