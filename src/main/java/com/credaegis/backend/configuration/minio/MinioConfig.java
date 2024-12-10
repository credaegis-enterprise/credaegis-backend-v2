package com.credaegis.backend.configuration.minio;


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String endpoint;

    @Value("${minio.access.key}")
    private String accessKey;

    @Value("${minio.secret.key}")
    private String secretKey;

    @Bean
    public MinioClient createMinioClient() {

        try {
            MinioClient minioClient = MinioClient.builder().
                    endpoint(endpoint).credentials(accessKey, secretKey).build();

            System.out.println("Minio Connection Successful");
            return minioClient;

        } catch (Exception e) {
            throw new RuntimeException("Connection with Minio could not be established");
        }

    }
}
