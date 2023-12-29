package org.example.wallet.wallet;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    ObjectMapper objectMapper;

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

    public String initiateTxn(String senderId, String receiverId, String reason, Double amount) throws JsonProcessingException {
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

        kafkaTemplate.send(CommonConstants.TRANSACTION_CREATION_TOPIC, objectMapper.writeValueAsString(jsonObject));

        return transaction.getTransactionUUID();
    }

    @KafkaListener(topics = CommonConstants.WALLET_UPDATED_TOPIC, groupId = "group123")
    public void updateTxn(String msg) throws ParseException {
        JSONObject data = (JSONObject) new JSONParser().parse(msg);

        String txnId = (String) data.get("txnId");
        String receiverId = (String) data.get("receiver");
        String sender = (String) data.get("sender");
        Double amount = (Double) data.get("amount");

        WalletUpdateStatus walletUpdateStatus = WalletUpdateStatus.valueOf((String)data.get("walletUpdateStatus"));
        JSONObject senderObj = getUserFromUserService(sender);
        String senderEmail = (String) senderObj.get("email");
        String receiverEmail = null;

        if(walletUpdateStatus == WalletUpdateStatus.SUCCESS){

        }
        else{
        }

    }


    private JSONObject getUserFromUserService(String username){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth("txn_service", "txn123");
        HttpEntity request = new HttpEntity(httpHeaders);
        return restTemplate.exchange("http://localhost:6001/admin/user/"+username, HttpMethod.GET, request, JSONObject.class).getBody();
    }
}
