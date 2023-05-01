package com.arajase3.fraudengineservice.Service;

import com.arajase3.fraudengineservice.domain.UserDataRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ProcessUserData {

    @Autowired
    private SnsAsyncClient snsAsyncClient;

    private final Map<String, UserDataRequest> users = new HashMap<>();

    public void AnalyzeUserData(String message) throws JsonProcessingException {
        var userData = new ObjectMapper().readValue(message, UserDataRequest.class);

        if (users.get(userData.id()) == null) {
            users.put(userData.id(), userData);
        } else {
            var existingUserData = users.get(userData.id());
            var isIpSame = existingUserData.ip().equals(userData.ip());
            var isPlatformSame = existingUserData.platform().equals(userData.platform());
            var isBrowserSame = existingUserData.browser().equals(userData.browser());

            if (!isIpSame && !isPlatformSame && !isBrowserSame) {
                log.info("CALLED THE USER");
                publishMessage("CALLED THE USER:"+userData.id(), "arn:aws:sns:us-east-1:000000000000:sev1-phone");
            }

            if (!isIpSame && !isPlatformSame) {
                log.info("MESSAGED THE USER");
                publishMessage("MESSAGED THE USER:"+userData.id(), "arn:aws:sns:us-east-1:000000000000:sev2-sms");
            }

            if (!isIpSame) {
                log.info("EMAILED THE USER");
                publishMessage("EMAILED THE USER:"+userData.id(), "arn:aws:sns:us-east-1:000000000000:sev3-email");
            }
        }

    }

    private void publishMessage(String message, String topicArn) {
        PublishRequest request = PublishRequest.builder()
                .message(message)
                .topicArn(topicArn)
                .build();

        snsAsyncClient.publish(request).join();
    }
}
