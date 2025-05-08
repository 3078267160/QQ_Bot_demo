package com.qq_bot_demo.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qq_bot_demo.config.QQBotConfig;
import com.qq_bot_demo.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 官方机器人初始化
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QQBotInitializer implements CommandLineRunner {
    private final TokenService tokenService;
    private final QQBotConfig config;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; // 新增ObjectMapper

    @Override
    public void run(String... args) {
        try {
            String token = tokenService.getValidToken();
            //log.info("Token已获取:{}", token);
            log.info("有效Token: {}...", token.substring(0, 6));//截取6位token

            // 使用通用JSON节点解析
            JsonNode userInfo = restTemplate.exchange(
                    config.getApiBaseUrl() + "/users/@me",
                    HttpMethod.GET,
                    new HttpEntity<>(tokenService.createHeaders()),
                    JsonNode.class
            ).getBody();
            log.info("Bot信息解析成功: {},{}", userInfo.get("username").asText(),userInfo.get("union_openid").asText());
            // 将JSON节点转换为格式化的字符串
            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(userInfo);

            log.info("BOT启动成功啦~~~"); // 打印完整信息
        } catch (Exception e) {
            log.error("初始化失败", e);
            System.exit(1);
        }
    }
}
