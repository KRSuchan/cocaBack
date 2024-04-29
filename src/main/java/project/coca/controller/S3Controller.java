package project.coca.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.coca.service.S3Service;

import java.io.IOException;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile multipartFile, @RequestParam String key) {
        try {
            return s3Service.uploadImage(multipartFile, key);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("이미지 업로드 중 오류가 발생했습니다");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadImage(@RequestParam String key) {
        return s3Service.downloadImage(key);
    }
}
