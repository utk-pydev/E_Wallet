package org.example.wallet.wallet;


import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService implements UserDetailsService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    }

    public void initiateTxn(String senderId, String receiverId, String reason, Double amount){
        Transaction transaction = Transaction.builder()
                .receiver(receiverId)
                .sender(senderId)
                .transactionUUID(UUID.randomUUID().toString())
                .reason(reason)
                .amount(amount)
                .build();
        transactionRepository.save(transaction);
    }
}
