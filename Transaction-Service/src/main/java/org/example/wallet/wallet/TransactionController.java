package org.example.wallet.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    KafkaTemplate<String, String>kafkaTemplate;

    @PostMapping("/txn")
    public void updateTransaction(@RequestParam String receiverId, @RequestBody String reason, @RequestBody Double amount) throws JsonProcessingException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String res =  transactionService.initiateTxn(userDetails.getUsername(), receiverId, reason, amount);
    }

}
