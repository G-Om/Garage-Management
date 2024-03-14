package ae.isa.garage;

import ae.isa.garage.filters.JwtRequestFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

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

    // Configuring Security Class
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        csrf -> csrf.disable()
                )
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/authenticate").permitAll()
                                .requestMatchers("/*/hello").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                ).addFilterBefore(
                        jwtRequestFilter, UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }


    // defining a bean so that it can be Autowired
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Password Hashing

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
