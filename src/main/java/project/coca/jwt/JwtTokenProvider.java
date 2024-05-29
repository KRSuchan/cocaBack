package project.coca.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final RedisTemplate<String, String> redisTemplate;
    private final Long DEFAULT_ACCESS_EXPIRATION_TIME = 1000L * 60 * 20; // 20분 (ms * 초 * 분 * 시간)
    private final Long DEFAULT_REFRESH_EXPIRATION_TIME = 1000L * 60 * 60 * 3; // 3시간 (ms * 초 * 분 * 시간)

    private final Key key;
    private UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            RedisTemplate<String, String> redisTemplate,
                            UserDetailsService userDetailsService) {
        this.redisTemplate = redisTemplate;
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + DEFAULT_ACCESS_EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + DEFAULT_REFRESH_EXPIRATION_TIME);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // redis에 저장
        redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                DEFAULT_REFRESH_EXPIRATION_TIME
        );

        return refreshToken;
    }

    /**
     * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체 생성해 Authentication 객체 반환
     */
    public Authentication getAuthentication(String token) {
        String userPrincipal = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + DEFAULT_REFRESH_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Access 토큰을 검증
     */
    public boolean validateToken(String token, HttpServletRequest request) throws JwtException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT Signature");
            request.setAttribute("exception", "invalidSignature");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT Token");
            request.setAttribute("exception", "invalidJwt");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
            request.setAttribute("exception", "expiredJwt");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Exception");
            request.setAttribute("exception", "unsupportedJwt");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty");
            request.setAttribute("exception", "claimsEmpty");
        }
        return false;
    }
}