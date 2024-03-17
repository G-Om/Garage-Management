package ae.isa.garage.services;

import ae.isa.garage.models.AuthenticationRequest;
import ae.isa.garage.models.AuthenticationResponse;
import ae.isa.garage.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/hello")
    public String sayHello() {
        return
                "<h1> Hello User </h1>" ;
    }

    @GetMapping("/{name}/hello")
    public String sayHelloUser(@PathVariable String name) {
        return "<h1> Hello " + name + "</h1>";
    }

    @GetMapping("/admin")
    public String sayHelloAdmin() {
        return "<h1> Hello Admin </h1>";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody  AuthenticationRequest authenticationRequest)
        throws Exception {
        System.out.println("Called CreateAuthToken function");
        try {
//            System.out.println(
//                    "User:\n"+
//                            "username : " + authenticationRequest.getUsername()+
//                            "\npassword : "+ authenticationRequest.getPassword()+
//                    "\nDatabase\n"+
//                            "username : "+ userDetailsService
//                            .loadUserByUsername(authenticationRequest.getUsername()).getUsername()+
//                            "\npassword : "+ userDetailsService
//                            .loadUserByUsername(authenticationRequest.getUsername()).getPassword()
//
//            );
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(), authenticationRequest.getPassword()
                    )
            );
        }catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        // if no exception occurs following code will run
        System.out.println("fetching user........... ");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(
                authenticationRequest.getUsername()
        );

        System.out.println("fetched user: " + userDetails.toString());

        final String jwt = jwtUtil.generateToken(userDetails);
        System.out.println(jwt);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }


}
