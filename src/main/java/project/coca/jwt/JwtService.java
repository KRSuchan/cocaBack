package project.coca.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenDto reissueToken(String refreshToken) {
        // Refresh Token 검증
        jwtTokenProvider.validateToken(refreshToken);

        // Access Token 에서 User num을 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        // 토큰 재발행
        TokenDto tokenDto = new TokenDto(
                jwtTokenProvider.createAccessToken(authentication),
                jwtTokenProvider.createRefreshToken(authentication)
        );

        return tokenDto;
    }
}
