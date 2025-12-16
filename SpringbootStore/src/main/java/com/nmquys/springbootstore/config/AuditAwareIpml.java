package com.nmquys.springbootstore.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
public class AuditAwareIpml implements AuditorAware<String>
{
    @Override
    public Optional<String> getCurrentAuditor()
    {
        return Optional.of("Anonymous user");
    }
}
