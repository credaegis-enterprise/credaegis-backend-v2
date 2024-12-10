package com.credaegis.backend.configuration.minio;


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {

    @Value("${minio.url}")
    private String endpoint;

    @Value("${minio.access.key}")
    private String accessKey;

    @Value("${minio.secret.key}")
    private String secretKey;

    @Value("${minio.approvals.bucket.name}")
    private String approvalBucket;

    @Value("${minio.certificate.bucket.name}")
    private String certificateBucket;

    @Value("${minio.profile.bucket.name}")
    private String profileBucket;

    @Bean
    public MinioClient createMinioClient() {

        try {
            MinioClient minioClient = MinioClient.builder().
                    endpoint(endpoint).credentials(accessKey, secretKey).build();


            minioClient.listBuckets();
            createIfNotExists(approvalBucket,minioClient);
            createIfNotExists(certificateBucket,minioClient);
            createIfNotExists(profileBucket,minioClient);

            log.info("Successful connection to minio established and bucket are initialized");
            return minioClient;

        } catch (Exception e) {
            log.error("Connection with minio failed");
            throw new RuntimeException("Connection with Minio could not be established");
        }

    }

     void createIfNotExists(String bucketName, MinioClient minioClient) {
        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                log.info("{} Bucket found", bucketName);
            } else {
                log.info("{} Bucket created", bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error("Error creating buckets");
            throw new RuntimeException("Error creating buckets");
        }

    }
}
