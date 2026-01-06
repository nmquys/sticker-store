package com.nmquys.springbootstore.security;

import com.nmquys.springbootstore.entity.Customer;
import com.nmquys.springbootstore.entity.Role;
import com.nmquys.springbootstore.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Class này dùng để tự implement logic xác thực username/password
 * Thay thế DaoAuthenticationProvider mặc định
 * Dùng cho dev/test/local Không cho prod
 */

@Profile("!prod")
@Component
@RequiredArgsConstructor
public class StoreNonProdUsernamePwdAuthenticationProvider implements AuthenticationProvider
{

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        //Lấy username và password khi login từ UsernamePasswordAuthenticationToken(username, password)
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        //tìm user trong DB
        Customer customer = customerRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        "User details not found for the user: " + username)
        );

        //Lấy roles của user
        Set<Role> roles = customer.getRoles();
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
        return new UsernamePasswordAuthenticationToken(
                customer,           //principal
                null,               //credentials
                authorities);       //roles
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
        //Provider này chỉ xử lý loại token login username/password
    }
}
