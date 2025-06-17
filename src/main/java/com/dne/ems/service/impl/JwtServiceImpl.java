package com.dne.ems.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dne.ems.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        // 使用 instanceof 模式匹配 (Java 16+) 来简化代码
        if (userDetails instanceof com.dne.ems.security.CustomUserDetails customUserDetails) {
            Map<String, Object> extraClaims = new HashMap<>();

            // 将所有需要暴露给前端的用户信息放入 "claims"
            // 注意：我们不应放入密码等敏感信息
            extraClaims.put("id", customUserDetails.getId());
            extraClaims.put("name", customUserDetails.getName());
            extraClaims.put("email", customUserDetails.getUsername()); // getUsername() 返回的是 email
            extraClaims.put("role", customUserDetails.getRole());
            extraClaims.put("phone", customUserDetails.getPhone());
            extraClaims.put("status", customUserDetails.getStatus());
            extraClaims.put("region", customUserDetails.getRegion());
            extraClaims.put("skills", customUserDetails.getSkills());
            
            // 使用重载的方法生成带有额外信息的 Token
            return generateToken(extraClaims, userDetails);
        }

        // 如果不是我们的 CustomUserDetails 类型，则生成一个只包含基本信息的 Token
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 1 day
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}