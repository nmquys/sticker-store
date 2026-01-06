package com.nmquys.springbootstore.config;

import com.nmquys.springbootstore.entity.Customer;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String>   //ghi lại ai cập nhật bản ghi trong database
{

    @Override
    public Optional<String> getCurrentAuditor()
    {
        //authentication đại diên cho user đang login, SecurityContextHolde chứa thông tin đăng nhập hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //nếu chưa đăng nhập, token ko hợp lệ, user ẩn danh
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser"))
        {
            return Optional.of("Anonymous user");
        }

        Object principal = authentication.getPrincipal();
        String username;

        //nếu user là customer thì lấy email làm auditor
        if (principal instanceof Customer customer) {
            username = customer.getEmail();
        }
        else
        {
            username = principal.toString(); // fallback
        }

        return Optional.of(username);
    }
}
