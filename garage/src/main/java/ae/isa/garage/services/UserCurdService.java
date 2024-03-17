package ae.isa.garage.services;

import ae.isa.garage.dao.UserDAO;
import ae.isa.garage.entities.UserInfo;
import ae.isa.garage.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCurdService {

    @Autowired
    UserRepository repository;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    public UserInfo addNewUser(UserDAO user) {
        // Encode Password here
        user.setPassword(
                myUserDetailsService.passwordEncoder().encode(user.getPassword())
        );
        UserInfo userInfo = new UserInfo(user);

        return repository.save(userInfo);
    }

    public List<UserInfo> fetchAllUsers() {
        System.out.println(
                "USER Fetched: " +
                repository.findAll().get(3).toString());

        return repository.findAll();
    }
}
