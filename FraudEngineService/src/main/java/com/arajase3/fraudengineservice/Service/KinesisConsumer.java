package com.arajase3.fraudengineservice.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Slf4j
@Component
public class KinesisConsumer {
    @Bean
    public Consumer<byte[]> kinesisConsumerStream(final ProcessUserData processUserData) {
        return message -> {
            var messageString = new String(message, StandardCharsets.UTF_8);
            try {
                processUserData.AnalyzeUserData(messageString);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
