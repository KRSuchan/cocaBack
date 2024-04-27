package project.coca.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
class S3ServiceTest {
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Test
    void testS3Service() {
        System.out.println(bucket);
    }

}