package com.nmquys.springbootstore.security;

import com.nmquys.springbootstore.filter.JWTTokenValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * class dùng để:
 *  Phân quyền API theo role
 *  Cấu hình Spring Security Filter Chain
 *  Tích hợp JWT filter
 *  Cấu hình CORS, CSRF, Password, AuthenticationManager
 */

@Configuration
@EnableWebSecurity  //Cho phép cấu hình Spring Security
@RequiredArgsConstructor
public class StoreSecurityConfig
{

    private final List<String> publicPaths;

    @Value("${stickerstore.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception
    {
        return http.csrf(csrfConfig -> csrfConfig.disable())    //disable CSRF
                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))  //enable CORS
                .authorizeHttpRequests((requests) ->
                        {
                            //authorization rules
                            //public APIs
                            publicPaths.forEach(path -> requests.requestMatchers(path).permitAll());

                            //Admin APIs
                            requests.requestMatchers("/api/v1/admin/**").hasRole("ADMIN");

                            //Actuator
                            requests.requestMatchers("/stickerstore/actuator/**").hasRole("OPS_ENG");

                            //Swagger
                            requests.requestMatchers("/swagger-ui.html", "/swagger-ui/**",
                            "/v3/api-docs/**").hasAnyRole("DEV_ENG","QA_ENG");

                            //default rule (mặc định phải login)
                            requests.anyRequest().hasAnyRole("USER", "ADMIN");
                        }
                )
                .addFilterBefore(new JWTTokenValidatorFilter(publicPaths), BasicAuthenticationFilter.class) //Gắn JWT filter chạy trc BasicAuth, set Authentication từ JWT
                .formLogin(withDefaults())
                .httpBasic(withDefaults()).build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
             AuthenticationProvider authenticationProvider)
    {
        var providerManager = new ProviderManager(authenticationProvider);
        return providerManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


    //Kiểm tra password có bị leak dùng khi register
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker()
    {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }


    //Cấu hình CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        config.setAllowedMethods(Collections.singletonList("*"));   //cho phép mọi methods
        config.setAllowedHeaders(Collections.singletonList("*"));   //cho phép mọi headers
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);    //Áp dụng toàn bộ API
        return source;
    }

}
