package org.example.wallet.wallet;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {


    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.logger(UserController.class);

    @PostMapping("/User")
    public void storeUser(@RequestBody UserRequest userRequest)
    {
       try {
            userService.createUser(userRequest);
       }catch (Exception ex){
            logger.info(ex.getMessage());
       }
    }

    @GetMapping("/User")
    List<Optional<User>> getUsers(@RequestParam(required = false) List<Integer> id){
        try{
            return userService.getUsersById(id);
        }catch (Exception ex){
            logger.info(ex.getMessage());
        }
        return null;
    }
    @GetMapping("/getAllUsers")
    List<User> getAllUsers(){
        try{
            return userService.getAllUsers();
        }catch (Exception ex){
            logger.info(ex.getMessage());
        }
        return null;
    }

}
