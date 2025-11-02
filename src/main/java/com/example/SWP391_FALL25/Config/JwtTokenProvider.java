package com.example.SWP391_FALL25.Config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
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

    @Value("${jwt.refreshExpiration}")
    private long jwtRefreshExpiration;

    public String generateAccessToken(String phone,String role){
        Date now =new Date();
        Date expiryDate=new Date(now.getTime()+jwtExpiration);

        return Jwts.builder()
                .setSubject(phone)
                .claim("role",role)
                .claim("type","access")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }

    public String generateRefreshToken(String phone){
        Date now=new Date();
        Date expiryDate=new Date(now.getTime()+jwtRefreshExpiration);

        return Jwts.builder()
                .setSubject(phone)
                .claim("type","refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }

    public Claims getClaims(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public String getPhoneFromToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token,String expectedType){
        try{
            Claims claims = getClaims(token);
            String type=claims.get("type",String.class);
            return expectedType.equals(type) && claims.getExpiration().after(new Date());
        }catch (Exception e){
            return false;
        }
    }
}
