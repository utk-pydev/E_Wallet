package org.example.wallet.wallet;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private String password;

    private String Country;
    private String dob;

    private String authorities;
    @Column(unique = true)
    private String identifierValue;

    private UserIdentifier userIdentifier;

    @CreationTimestamp
    private Date createdOn;

    @CreationTimestamp
    private Date updatedOn;


}
