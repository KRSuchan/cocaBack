package project.coca.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpController {
    @GetMapping("/api/healthcheck")
    public String healthcheck() {
        return "OK";
    }
}
