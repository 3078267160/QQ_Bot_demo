package com.qq_bot_demo.service;



import com.qq_bot_demo.config.QQBotConfig;
import com.qq_bot_demo.pojo.dto.AccessTokenResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final RestTemplate restTemplate;
    private final QQBotConfig config;

    private volatile String accessToken;
    private volatile long expiresAt;
    private final CountDownLatch initLatch = new CountDownLatch(1);

    // TokenService.java 新增方法
    public HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "QQBot " + this.getValidToken());
        return headers;
    }

    @PostConstruct
    public void init() {
        Executors.newSingleThreadScheduledExecutor()
                .schedule(this::refreshToken, 1, TimeUnit.SECONDS);
    }

    public synchronized String getValidToken() {
        if (shouldRefresh()) {
            refreshToken();
        }
        return accessToken;
    }

    private boolean shouldRefresh() {
        return accessToken == null ||
                System.currentTimeMillis() > expiresAt - config.getTokenRefreshBufferSeconds() * 1000L;
    }

    private void refreshToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> request = Map.of(
                    "appId", config.getAppId(),
                    "clientSecret", config.getClientSecret()
            );

            // 使用try-with-resources确保响应流关闭
            ResponseEntity<AccessTokenResponse> response = restTemplate.postForEntity(
                    config.getAccessTokenUrl(),
                    new HttpEntity<>(request, headers),
                    AccessTokenResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                AccessTokenResponse res = response.getBody();
                this.accessToken = res.getAccessToken();
                this.expiresAt = System.currentTimeMillis() + res.getExpiresIn() * 1000;
            }
        } catch (Exception e) {
            log.error("Token刷新失败", e);
            throw new RuntimeException("Token刷新异常", e);
        }
    }
}
