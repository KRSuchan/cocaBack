package project.coca.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
class S3ServiceTest {
    @Autowired
    private S3Service s3Service;
    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

//    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );

    @Test
    void testS3Service() throws IOException {
        File imageFile = new File("C:\\Users\\lsc48\\OneDrive\\바탕 화면\\human_icon_test.jpg");
            FileInputStream fileInputStream = new FileInputStream(imageFile);
        MockMultipartFile multipartFile = new MockMultipartFile(
                "testImageFile",
                imageFile.getName(),
                MediaType.IMAGE_JPEG_VALUE,
                fileInputStream
        );
        String memberId = "testId1234";
        ResponseEntity<?> uploadedResponseEntity = s3Service.uploadImage(multipartFile, memberId + multipartFile.getOriginalFilename());
        System.out.println("==============================================");
        ResponseEntity<?> downloadedResponseEntity = s3Service.downloadImage(memberId + multipartFile.getOriginalFilename());
        System.out.println("==============================================");
        System.out.println(uploadedResponseEntity.getBody());
        System.out.println(downloadedResponseEntity.getBody());
    }

}