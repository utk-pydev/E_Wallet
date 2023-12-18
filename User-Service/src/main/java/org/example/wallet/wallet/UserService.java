package org.example.wallet.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    KafkaTemplate<String, String>kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public void createUser(UserRequest userRequest){
        User user = userRequest.to();
        user.setPassword(encryptPwd(user.getPassword()));
        user.setAuthorities(String.valueOf(UserConstants.USER_AUTHORITY));
        user = userRepository.save(user);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", user.getId());
        jsonObject.put("phoneNumber", user.getPhoneNumber());
        jsonObject.put("identifierValue", user.getIdentifierValue());
        jsonObject.put("userIdentifier", user.getIdentifierValue());

        kafkaTemplate.send("User_Created",  ObjectMapper.writeValueAsString(jsonObject));

    }

    public List<Optional<User>> getUsersById(List<Integer> ids){
       return ids.stream().
                map(userRepository::findById)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    private String encryptPwd(String rawPwd){
        return passwordEncoder.encode(rawPwd);
    }
}
