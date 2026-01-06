package com.nmquys.springbootstore.filter;

import com.nmquys.springbootstore.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**Filter này dùng để:
    Chặn mọi req
    Lấy JWT từ header
    Verify token
    Set Authentication vào SecurityContext
 **/
@RequiredArgsConstructor
public class JWTTokenValidatorFilter extends OncePerRequestFilter //filter chỉ chạy 1 lần duy nhất
{
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> publicPaths;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        String authHeader = request.getHeader(ApplicationConstants.JWT_HEADER); //lấy header
        if (null != authHeader)
        {
            try
            {
                // Extract the JWT token
                String jwt = authHeader.substring(7); // Remove 'Bearer ' prefix
                Environment env = getEnvironment();
                if (null != env)
                {
                    //Lấy JWT sk từ env hoặc dùng default
                    String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                            ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

                    //tạo sk
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    if (null != secretKey)
                    {
                        Claims claims = Jwts.parser()
                                .verifyWith(secretKey)
                                .build()
                                .parseSignedClaims(jwt)
                                .getPayload();

                        //Lấy thông tin từ JWT
                        String username = String.valueOf(claims.get("email"));
                        String roles = String.valueOf(claims.get("roles"));

                        //Tạo Authentication
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                AuthorityUtils.commaSeparatedStringToAuthorityList(roles)
                        );

                        //Sau đó gắn vào SecurityContext nhận diện user đã login
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
            catch (ExpiredJwtException exception) //nếu token hết hạn
            {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token Expired");
                return;
            }
            catch (Exception exception) //nếu token không hợp lệ
            {
                throw new BadCredentialsException("Invalid Token received!");
            }
        }
        filterChain.doFilter(request, response);
    }

    //Bỏ qua các public APIs
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException
    {
        String path = request.getRequestURI();
        return publicPaths.stream().anyMatch(publicPath ->
                pathMatcher.match(publicPath, path));
    }
}
