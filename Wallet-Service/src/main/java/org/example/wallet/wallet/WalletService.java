package org.example.wallet.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.logging.Logger;

@Service
public class WalletService {
    private static Logger logger = (Logger) LoggerFactory.getLogger(WalletService.class);

    @Autowired
    WalletRepository walletRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    KafkaTemplate<String, String>kafkaTemplate;

    public void createWallet(String msg) throws ParseException{
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
}
