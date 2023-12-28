package org.example.wallet.wallet;


import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.kafka.common.protocol.types.Field;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService implements UserDetailsService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JSONObject requestedUser = getUserFromUserService(username);
        List<GrantedAuthority> authorities;

        List<LinkedHashMap<String, String>>requestAuthorities = (List<LinkedHashMap<String, String>>) requestedUser.get("authorities");

        authorities = requestAuthorities
                .stream()
                .map(x->x.get("authority"))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(
                (String) requestedUser.get("username"),
                (String) requestedUser.get("password"),
                authorities
        );
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

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sender", senderId);
        jsonObject.put("receiver", receiverId);
        jsonObject.put("amount", amount);
        jsonObject.put("txnId", transaction.getTransactionUUID());



    }

    private JSONObject getUserFromUserService(String username){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth("txn_service", "txn123");
        HttpEntity request = new HttpEntity(httpHeaders);
        return restTemplate.exchange("http://localhost:6001/admin/user/"+username, HttpMethod.GET, request, JSONObject.class).getBody();
    }
}
