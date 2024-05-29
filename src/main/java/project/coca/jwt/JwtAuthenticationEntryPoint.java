package project.coca.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import project.coca.dto.response.common.ApiResponse;
import project.coca.dto.response.common.error.ErrorCode;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException, IOException {
        String exceptionType = (String) request.getAttribute("exception");
        ApiResponse<?> apiResponse;
        if (exceptionType.equals("invalidSignature")) {
            apiResponse = ApiResponse.fail(ErrorCode.BAD_REQUEST, "invalid_signature");
        } else if (exceptionType.equals("invalidJwt")) {
            apiResponse = ApiResponse.fail(ErrorCode.BAD_REQUEST, "invalid_jwt");
        } else if (exceptionType.equals("unsupportedJwt")) {
            apiResponse = ApiResponse.fail(ErrorCode.BAD_REQUEST, "unsupported_jwt");
        } else if (exceptionType.equals("expiredJwt")) {
            apiResponse = ApiResponse.fail(ErrorCode.UNAUTHORIZED, "expired_jwt");
        } else {
            apiResponse = ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, exceptionType);
        }
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(apiResponse.toString());
        response.getWriter().flush();
    }
}