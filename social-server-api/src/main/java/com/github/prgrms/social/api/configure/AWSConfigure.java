package com.github.prgrms.social.api.configure;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.prgrms.social.api.aws.S3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AWSConfigure {

    @Value("${cloud.aws.credentials.accessKey}") private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}") private String secretKey;
    @Value("${cloud.aws.region}") private String region;

    @Value("${cloud.aws.s3.url}") private String url;
    @Value("${cloud.aws.s3.bucketName}") private String bucketName;

    @Bean
    // S3 Conection
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder.standard()
                                    .withRegion(Regions.fromName(region))
                                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                                    .build();
    }

    @Bean
    public S3Client s3Client(AmazonS3 amazonS3) {
        return new S3Client(amazonS3, url, bucketName);
    }

}
