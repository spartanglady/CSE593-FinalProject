package com.arajase3.fraudlistenerservice.controller;

import com.arajase3.fraudlistenerservice.dto.UserDataRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

@RestController
@Slf4j
public class UserDataController {

    @Autowired
    private KinesisAsyncClient kinesis;

    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<String>> putUserData(@PathVariable String id, @RequestBody UserDataRequest userDataRequest) throws JsonProcessingException {
        PutRecordRequest putRecordRequest = PutRecordRequest.builder()
                .partitionKey(id)
                .streamName("user-data-stream")
                .data(SdkBytes.fromUtf8String(new ObjectMapper().writeValueAsString(userDataRequest)))
                .build();

        return Mono.fromFuture(kinesis.putRecord(putRecordRequest)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }
}

