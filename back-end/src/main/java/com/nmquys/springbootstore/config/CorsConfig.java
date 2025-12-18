package com.nmquys.springbootstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig     //fix the CORS
{
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://stickerstore:5173"));   //domain's name to communicate
        config.setAllowedMethods(Collections.singletonList("*")); //cho phép mọi HTTP methods
        config.setAllowedHeaders(Arrays.asList("Content-Type"));    //cho phép fe gửi request với header content-type
        config.setAllowCredentials(true);   //cho phép gửi cookie, JWT token

        //đăng ký CORS filter cho toàn bộ API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
