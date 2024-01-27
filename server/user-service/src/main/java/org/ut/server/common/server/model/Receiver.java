package org.ut.server.common.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;


//@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
//@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class  Receiver extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "received_at_post")
    private Boolean receivedAtPost;
    @Column(nullable = true)
    private String postAddress;
    private String note;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
//    private ShopOwner shopOwner;

    @Builder
    public Receiver(Long id, String username, String phoneNumber, String address,
                    User user, Boolean receivedAtPost, String postAddress, String note) {
        super(username, phoneNumber, address);
        this.id = id;
        this.user = user;
        this.receivedAtPost = receivedAtPost;
        this.postAddress = postAddress;
        this.note = note;
    }
}
