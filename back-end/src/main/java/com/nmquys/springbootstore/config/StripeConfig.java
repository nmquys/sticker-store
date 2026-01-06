package com.nmquys.springbootstore.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:stripe.properties")  //nạp file từ resources, đưa key value vào env
public class StripeConfig {

    @Value("${stripe.apiKey}")  //@Value để inject giá trị
    private String apiKey;

    @PostConstruct  //chạy 1 lần duy nhất khi app start
    public void init() {
        Stripe.apiKey = apiKey;
    }

    //app start -> load stripe.properties -> inject stripe.apiKey vào apiKey -> init()
}
