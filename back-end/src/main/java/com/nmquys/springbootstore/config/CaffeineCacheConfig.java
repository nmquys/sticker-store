package com.nmquys.springbootstore.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

//dùng để cấu hình cache trong bộ nhớ bằng thư viện caffeineCache
@Configuration
public class CaffeineCacheConfig
{

    @Bean
    public CacheManager caffeineCacheManager()
    {
        //tạo cache product
        CaffeineCache productsCache = new CaffeineCache("products",
                Caffeine.newBuilder()
                        .expireAfterWrite(30, TimeUnit.MINUTES) //cache hết hạn sau 30p kể từ lúc ghi
                        .maximumSize(1000)
                        .build());

        //tạo cache roles
        CaffeineCache rolesCache = new CaffeineCache("roles",
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.DAYS)
                        .maximumSize(1)
                        .build());

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(productsCache, rolesCache));
        return manager;
    }
}
