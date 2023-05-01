package com.arajase3.demoappserver.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.time.Duration;

@RestController
@Slf4j
public class SseController {
    private final SqsAsyncClient sqsAsyncClient;
    private final Sinks.Many<String> sinkPhone;
    private final Sinks.Many<String> sinkEmail;
    private final Sinks.Many<String> sinkSms;

    public SseController(SqsAsyncClient sqsClient) {
        this.sqsAsyncClient = sqsClient;
        this.sinkPhone = Sinks.many().multicast().onBackpressureBuffer();
        this.sinkEmail = Sinks.many().multicast().onBackpressureBuffer();
        this.sinkSms = Sinks.many().multicast().onBackpressureBuffer();
        String QUEUE_URL_PHONE = "http://localhost:4566/000000000000/sev1-phone";
        subscribeToSqs(QUEUE_URL_PHONE, sinkPhone);
        String QUEUE_URL_EMAIL = "http://localhost:4566/000000000000/sev3-email";
        subscribeToSqs(QUEUE_URL_EMAIL, sinkEmail);
        String QUEUE_URL_SMS = "http://localhost:4566/000000000000/sev2-sms";
        subscribeToSqs(QUEUE_URL_SMS, sinkSms);
    }


    @GetMapping(value = "/phone", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> events() {
        return sinkPhone.asFlux();
    }

    @GetMapping(value = "/email", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> events2() {
        return sinkEmail.asFlux();
    }

    @GetMapping(value = "/sms", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> events3() {
        return sinkSms.asFlux();
    }

    private void subscribeToSqs(String queueUrl, Sinks.Many<String> sink) {

        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .waitTimeSeconds(20)
                .build();

        sqsAsyncClient.receiveMessage(request).thenAccept(response -> {
            for (Message message : response.messages()) {
                log.info("Event: " + message.body());
                sink.tryEmitNext("Event: " + message.body());
                deleteMessageFromSqs(queueUrl, message);
            }
            subscribeToSqs(queueUrl, sink);
        });
    }

    private void deleteMessageFromSqs(String queueUrl, Message message) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build();

        sqsAsyncClient.deleteMessage(deleteRequest)
                .exceptionally(e -> {
                    System.err.println("Failed to delete message: " + e.getMessage());
                    return null;
                });
    }
}
