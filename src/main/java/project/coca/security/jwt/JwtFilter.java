package project.coca.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 헤더(Authorization)에 있는 토큰을 꺼내 이상이 없는 경우 SecurityContext에 저장
 * Request 이전에 작동
 */

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                log.warn("Invalid Authorization header format");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Invalid Authorization header\"}");
                return;
            }
            token = token.substring(7);

            if (jwtTokenProvider.validateToken(token, request)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                log.info("Authentication success for user: {}", auth.getName());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (RedisConnectionFailureException e) {
            log.error("Redis connection failure: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Redis connection failure\"}");
            return;
        } catch (Exception e) {
            log.error("Unexpected error during JWT processing: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid or missing JWT token\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }


}
