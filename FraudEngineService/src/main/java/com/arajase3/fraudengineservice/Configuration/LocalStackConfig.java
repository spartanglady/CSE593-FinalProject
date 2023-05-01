package com.arajase3.fraudengineservice.Configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesisAsync;
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsAsyncClient;

@Configuration
public class LocalStackConfig {
    private static final Region DEFAULT_REGION = Region.US_EAST_1;

    @Bean
    public AmazonKinesisAsync amazonKinesisAsync() {
        return AmazonKinesisAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .build();
    }

    @Bean
    @Primary
    public AmazonDynamoDBAsync amazonDynamoDBAsync() {
        return AmazonDynamoDBAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .build();
    }

    @Bean
    public SnsAsyncClient snsAsyncClient() {
        return SnsAsyncClient.builder()
                .region(DEFAULT_REGION)
                .endpointOverride(java.net.URI.create("http://localhost:4566"))
                .build();
    }

}
