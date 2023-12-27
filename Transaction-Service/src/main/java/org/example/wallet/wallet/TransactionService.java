package org.example.wallet.wallet;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements UserDetailsService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          return null;
    }
}
