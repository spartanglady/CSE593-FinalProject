package com.arajase3.fraudlistenerservice.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

@Configuration
public class LocalStackConfig {
    private static final Region DEFAULT_REGION = Region.US_EAST_1;

    @Bean
    public KinesisAsyncClient amazonKinesisAsync() {
        return KinesisAsyncClient.builder()
                .region(DEFAULT_REGION)
                .endpointOverride(java.net.URI.create("http://localhost:4566"))
                .build();
    }

}
