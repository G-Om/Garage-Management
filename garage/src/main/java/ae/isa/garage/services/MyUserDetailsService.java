package ae.isa.garage.services;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MyUserDetailsService{


    // Registering a bean for User Info Service
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        List<UserDetails> userDetails = new ArrayList<>();

        userDetails.add(
                User
                        .withUsername("om")
                        .password(passwordEncoder().encode("root"))
                        .roles("USER")
                        .build());

        userDetails.add(
                User
                        .withUsername("root")
                        .password(passwordEncoder().encode("root"))
                        .roles("ADMIN")
                        .build());

        return new InMemoryUserDetailsManager(userDetails);
    }

    // Password Hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
