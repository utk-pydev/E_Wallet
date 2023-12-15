package org.example.wallet.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class UserController {


    @Autowired
    UserService userService;

    private static final Logger logger = new Logger(UserController.class);

    @PostMapping("/User")
    public void storeUser(@RequestBody UserRequest userRequest)
    {
       try {
            userService.createUser(userRequest.to());
       }catch (Exception ex){
            logger.info(ex.getMessage());
       }
    }

    @GetMapping("/User")
    List<User> getAllUsers(@RequestParam(required = false) List<Integer> id){
        if(id.isEmpty()){

        }
    }
}
