package com.arajase3.fraudengineservice.domain;

//Browser User Agent Data
//https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/User-Agent
public record UserDataRequest(String id, String browser, String platform, String device, String ip ) {
}
