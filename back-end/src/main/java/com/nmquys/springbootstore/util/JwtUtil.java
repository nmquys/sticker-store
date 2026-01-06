package com.nmquys.springbootstore.util;

import com.nmquys.springbootstore.constants.ApplicationConstants;
import com.nmquys.springbootstore.entity.Customer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


/**
 * Dùng để tạo JWT token khi đã login thành công
 * POST /api/v1/auth/login -> jwtUtil.generateJwtToken -> client nhận token
 */
@Component
@RequiredArgsConstructor
public class JwtUtil
{

    private final Environment env;

    public String generateJwtToken(Authentication authentication)
    {
        String jwt = "";

        //Lấy JWT sk
        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

        //tạo sk để sign token, verify token ở filter
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        Customer fetchedCustomer = (Customer) authentication.getPrincipal();

        //Build JWT token
        jwt = Jwts.builder()
                .issuer("Sticker Store")    //người phát hành
                .subject("JWT Token")       //loại token
                .claim("username", fetchedCustomer.getName())
                .claim("email", fetchedCustomer.getEmail())
                .claim("mobileNumber", fetchedCustomer.getMobileNumber())
                .claim("roles", authentication.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date((new java.util.Date()).getTime() + 24 * 60 * 60 * 1000))
                .signWith(secretKey).compact();
        return jwt;

        //Input: Authentication, Output: JWT dạng string
    }
}
