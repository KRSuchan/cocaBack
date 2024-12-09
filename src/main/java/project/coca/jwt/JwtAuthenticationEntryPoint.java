package project.coca.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import project.coca.dto.response.common.ApiResponse;
import project.coca.dto.response.common.error.ErrorCode;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws ServletException, IOException {
        String exceptionType = (String) request.getAttribute("exception");

        if (exceptionType == null) {
            exceptionType = "unknown_error"; // 기본값 설정
        }

        ApiResponse<?> apiResponse = switch (exceptionType) {
            case "invalidSignature" -> ApiResponse.fail(ErrorCode.BAD_REQUEST, "invalid_signature");
            case "invalidJwt" -> ApiResponse.fail(ErrorCode.BAD_REQUEST, "invalid_jwt");
            case "unsupportedJwt" -> ApiResponse.fail(ErrorCode.BAD_REQUEST, "unsupported_jwt");
            case "expiredJwt" -> ApiResponse.fail(ErrorCode.UNAUTHORIZED, "expired_jwt");
            case "nullToken" -> ApiResponse.fail(ErrorCode.BAD_REQUEST, "null_token");
            default -> ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, exceptionType);
        };

        log.error("Authentication error occurred: {}", exceptionType, authException);

        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 표준 상태 코드 설정
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(apiResponse.toString());
        response.getWriter().flush();
    }
}