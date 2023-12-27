package org.example.wallet.wallet;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String sender;

    private String receiver;
    @Column(unique = true)
    private String transactionUUID;

    private String reason;

    private Double amount;
    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private  Date updatedAt;

}
