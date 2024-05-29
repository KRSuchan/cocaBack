package project.coca.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.coca.dto.response.common.ApiResponse;
import project.coca.dto.response.common.success.ResponseCode;
import project.coca.jwt.JwtService;
import project.coca.jwt.TokenDto;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/jwt")
public class JwtController {
    private final JwtService jwtService;

    @PostMapping("/reissue")
    public ApiResponse<TokenDto> reissue(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        log.info("reissue token {}", token);
        return ApiResponse.response(ResponseCode.OK, jwtService.reissueToken(token, request));
    }

}
