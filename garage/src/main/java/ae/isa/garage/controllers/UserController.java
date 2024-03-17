package ae.isa.garage.controllers;


import ae.isa.garage.dao.UserDAO;
import ae.isa.garage.entities.UserInfo;
import ae.isa.garage.services.UserCurdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserCurdService userCurdService;

    @GetMapping
    public List<UserInfo> getAllUsers() {

        return userCurdService.fetchAllUsers();
    }

    @PostMapping
    public UserInfo createUser(@RequestBody UserDAO user)
    {
        System.out.println(user.toString());
        return userCurdService.addNewUser(user);
    }
}
