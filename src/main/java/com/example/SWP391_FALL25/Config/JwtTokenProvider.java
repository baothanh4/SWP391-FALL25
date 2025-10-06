package com.example.SWP391_FALL25.Config;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(String phone,String role){
        Date now =new Date();
        Date expiryDate=new Date(now.getTime()+jwtExpiration);

        return Jwts.builder()
                .setSubject(phone)
                .claim("role",role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }

    public String getPhoneFromToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}
