package org.example.springsec.springsec.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
public class JwtService {

    private final String secretKey ;
    public JwtService(@Value("${spring.security.private}") String secretKey){
        this.secretKey = secretKey;
    }


    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }//generic method to extract any claim from token (get username, get role, etc)  --------> Claims::getExpiration || Claims::getSubject

    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        claims.put("roles",userDetails.getAuthorities());

        return generateToken(claims,userDetails);
    }

    public String generateToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails
    ){
        JwtBuilder builder = Jwts.builder();
        for(Map.Entry<String,Object> entry : extraClaims.entrySet()){
            builder.claim(entry.getKey(),entry.getValue());
        }//if you want to add extra claims
        log.info("Token generated for user: {}",getSignIngKey().getAlgorithm());
        return builder
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000 * 60*60 * 24)))
                .signWith(getSignIngKey(),Jwts.SIG.HS256)
                .compact();
    } ///build the jwt token of specific user

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token); ////get username from token
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token); ////// check if token is expired and username is same
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); ////check if token is expired
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);///extract expiration date from token
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignIngKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }// get all claims from token (payload)

    private SecretKey getSignIngKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    } //decript the secret key
}
