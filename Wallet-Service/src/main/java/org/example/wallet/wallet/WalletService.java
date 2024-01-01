package org.example.wallet.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.logging.Logger;

@Service
public class WalletService {
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    KafkaTemplate<String, String>kafkaTemplate;

    @KafkaListener(topics = CommonConstants.USER_CREATION_TOPIC, groupId = "group123")
    public void createWallet(String msg) throws ParseException, org.jose4j.json.internal.json_simple.parser.ParseException {
        JSONObject data = (JSONObject) new JSONParser().parse(msg);

        String phoneNumber = (String) data.get(CommonConstants.USER_CREATION_TOPIC_PHONE_NUMBER);
        Long userId = (Long) data.get(CommonConstants.USER_CREATION_TOPIC_USERID);
        String indentifierKey = (String) data.get(CommonConstants.USER_CREATION_TOPIC_IDENTIFIER_KEY);
        String identifierValue = (String) data.get(CommonConstants.USER_CREATION_TOPIC_IDENTIFIER_VALUE);

        Wallet wallet = Wallet.builder()
                .userId(userId)
                .phoneNumber(phoneNumber)
                .userIdentifier(UserIdentifier.valueOf(indentifierKey))
                .identifierValue(identifierValue)
                .balance(10.0)
                .build();

        walletRepository.save(wallet);
    }

    @KafkaListener(topics = CommonConstants.TRANSACTION_CREATION_TOPIC, groupId = "group123")
    public void updateWalletsForTxn(String msg) throws org.jose4j.json.internal.json_simple.parser.ParseException, JsonProcessingException {
        JSONObject data = (JSONObject) new JSONParser().parse(msg);
        String sender = (String) data.get("sender");
        String receiver = (String) data.get("receiver");
        Double amount = (Double) data.get("amount");
        String txnId = (String) data.get("txnId");

        Wallet senderWallet = walletRepository.findByPhoneNumber(sender);
        Wallet receiverWallet = walletRepository.findByPhoneNumber(receiver);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("txnId", txnId);
        jsonObject.put("sender", sender);
        jsonObject.put("receiver",receiver);
        jsonObject.put("amount", amount);

        if(senderWallet == null || receiverWallet == null || senderWallet.getBalance() < amount){
            jsonObject.put("walletUpdateStatus", WalletUpdateStatus.FAILED);
            kafkaTemplate.send(CommonConstants.WALLET_UPDATED_TOPIC, objectMapper.writeValueAsString(msg));
            return;
        }
        if(performTransaction(sender, receiver, amount)){
            jsonObject.put("walletUpdateStatus", WalletUpdateStatus.SUCCESS);
           }
        else{
            jsonObject.put("walletUpdateStatus", WalletUpdateStatus.FAILED);
        }
        kafkaTemplate.send(CommonConstants.WALLET_UPDATED_TOPIC, objectMapper.writeValueAsString(msg));

    }

    @Transactional
    public boolean performTransaction(String sender, String receiver, double amount) {
        try {
            walletRepository.updateWallet(receiver, amount);
            walletRepository.updateWallet(sender, 0 - amount);
            // If everything goes well, the transaction is committed.
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
