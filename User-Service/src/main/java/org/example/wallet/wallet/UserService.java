package org.example.wallet.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public void createUser(User user){
        userRepository.save(user);
    }

    public List<Optional<User>> getUsersById(List<Integer> ids){
       return ids.stream().
                map(userRepository::findById)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
