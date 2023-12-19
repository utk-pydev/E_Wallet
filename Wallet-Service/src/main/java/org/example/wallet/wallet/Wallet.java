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

    private Long userId;

    @Column(unique = true)
    private String phoneNumber;

    private Double balance;

    @Column(unique = true)
    private String identifierValue;
    @Enumerated(value = EnumType.STRING)
    private UserIdentifier userIdentifier;
}
