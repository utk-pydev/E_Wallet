package org.example.wallet.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserSecurityConfig {

    @Autowired
    UserService userService;

}
