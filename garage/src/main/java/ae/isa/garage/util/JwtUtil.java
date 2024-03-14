package ae.isa.garage.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {


    private final String Secret_Key = "bb726b76b53500d1ba29173bc89e7fe2dbaeab1dce4c0518fd1190c4de512e08fb7cbba1ec79242671e1e1e7309bf41d8c97b79706974071e1df359f16a539c9";


    public String extractUserName(String token) {
        return  extractClaims(token, Claims::getSubject);
    }
    public Date extractExpiration(String token) {
        return  extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extracAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extracAllClaims(String token) {
        // Changed Syntax

        //return Jwts.parser().setSigningKey(Secret_Key).parseClaimsJws(token).getBody();
        return Jwts.parser().setSigningKey(Secret_Key).build().parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
//        System.out.println("KEY: " + Secret_Key.getEncoded());
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000) * 60 * 60 * 8))
                .signWith(SignatureAlgorithm.HS256, Secret_Key).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final  String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



}