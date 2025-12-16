package com.nmquys.springbootstore.scopes;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope   //tao ra cho moi user session
@Getter @Setter
@Slf4j
public class SessionScopedBean{
    private String userName;

    public SessionScopedBean()
    {
        log.info("SessionScopedBean initialized");
    }
}
