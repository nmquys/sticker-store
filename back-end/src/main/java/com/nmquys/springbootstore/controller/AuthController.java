package com.nmquys.springbootstore.controller;

import com.nmquys.springbootstore.dto.*;
import com.nmquys.springbootstore.entity.Customer;
import com.nmquys.springbootstore.entity.Role;
import com.nmquys.springbootstore.repository.CustomerRepository;
import com.nmquys.springbootstore.repository.RoleRepository;
import com.nmquys.springbootstore.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController
{

    private final AuthenticationManager authenticationManager;          //thực hiện xác thực username và password
    private final CustomerRepository customerRepository;                //truy cập DB bảng customer
    private final RoleRepository roleRepository;                        //lấy role
    private final PasswordEncoder passwordEncoder;                      //Hash password (BCrypt)
    private final CompromisedPasswordChecker compromisedPasswordChecker;//Kiểm tra password có bị lộ không
    private final JwtUtil jwtUtil;                                      //Sinh JWT token

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> apiLogin(@RequestBody
    LoginRequestDto loginRequestDto)
    {
        try
        {
            //B1: Authenticate
            Authentication authentication = authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(loginRequestDto.username(),
                    loginRequestDto.password()));
            var userDto = new UserDto();

            //B2: Lấy user đã đăng nhập
            var loggedInUser = (Customer) authentication.getPrincipal();

            //B3: Map sang DTO
            BeanUtils.copyProperties(loggedInUser, userDto);

            //gán roles
            userDto.setRoles(authentication.getAuthorities().stream().map(
                    GrantedAuthority::getAuthority).collect(Collectors.joining(",")));

            //gán adddress nếu có
            if (loggedInUser.getAddress() != null)
            {
                AddressDto addressDto = new AddressDto();
                BeanUtils.copyProperties(loggedInUser.getAddress(), addressDto);
                userDto.setAddress(addressDto);
            }

            //B4: Generate JWT chứa Username, Roles, Expiration time
            String jwtToken = jwtUtil.generateJwtToken(authentication);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(),
                            userDto, jwtToken));
        }
        catch (BadCredentialsException ex)
        {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        }
        catch (AuthenticationException ex)
        {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Authentication failed");
        }
        catch (Exception ex)
        {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto)
    {
        //B1: Validate Password
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(registerRequestDto.getPassword());
        if(decision.isCompromised())
        {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("password", "Choose a strong password"));
        }

        //B2: Check trùng email
        Optional<Customer> existingCustomer =  customerRepository.findByEmailOrMobileNumber
                (registerRequestDto.getEmail(),registerRequestDto.getMobileNumber());
        if(existingCustomer.isPresent())
        {
            Map<String, String> errors = new HashMap<>();
            Customer customer = existingCustomer.get();

            if (customer.getEmail().equalsIgnoreCase(registerRequestDto.getEmail()))
            {
                errors.put("email", "Email is already registered");
            }
            if (customer.getMobileNumber().equals(registerRequestDto.getMobileNumber()))
            {
                errors.put("mobileNumber", "Mobile number is already registered");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        //B3: Tạo customer mới
        Customer customer = new Customer();
        BeanUtils.copyProperties(registerRequestDto, customer);
        customer.setPasswordHash(passwordEncoder.encode(registerRequestDto.getPassword())); //hash password
        roleRepository.findByName("ROLE_USER").ifPresent(role -> customer.setRoles(Set.of(role)));  //gán role mặc định là USER

        //B4: save và response
        customerRepository.save(customer);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Registration successful");
    }

    //hàm này tránh lập code khi login
    private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status,
            String message)
    {
        return ResponseEntity
                .status(status)
                .body(new LoginResponseDto(message, null, null));
    }
}
