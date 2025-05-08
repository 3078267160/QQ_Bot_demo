package com.qq_bot_demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * bot配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "qq.bot")
public class QQBotConfig {
    private String appId;
    private String clientSecret;
    private String accessTokenUrl = "https://bots.qq.com/app/getAppAccessToken";
    //private String apiBaseUrl = "https://api.sgroup.qq.com";  //正式地址
    private String apiBaseUrl = "https://sandbox.api.sgroup.qq.com";  //沙箱地址
    private int tokenRefreshBufferSeconds = 60;
}
