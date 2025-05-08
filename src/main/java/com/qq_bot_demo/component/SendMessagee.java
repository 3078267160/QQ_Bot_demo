package com.qq_bot_demo.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qq_bot_demo.pojo.Media;
import com.qq_bot_demo.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 群消息发送
 * 仅限于群聊  私聊可以参考封装
 */
@Slf4j
@Component
public class SendMessagee {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;

    public void sendGroupMsg(String GroupOpenId , MessageRequest request)throws JsonProcessingException {
        // 构造请求头
        HttpHeaders headers = tokenService.createHeaders(); // 使用TokenService中的方法创建请求头
        // 构造请求体
        ObjectMapper objectMapper = new ObjectMapper();
        // 增加日志记录
        log.debug("发送群聊请求头: {}", headers);
        log.debug("发送群聊请求体: {}", objectMapper.writeValueAsString(request));

        HttpEntity<MessageRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.sgroup.qq.com/v2/groups/" + GroupOpenId + "/messages",
                requestEntity,
                String.class
        );
        // 解析响应（使用 Jackson）
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, String> responseBody = objectMapper.readValue(response.getBody(), Map.class);
            log.info("消息ID: {}", responseBody.get("id"));
            log.info("发送时间: {}", responseBody.get("timestamp"));
            log.info("发送信息成功: {}",request);
        } else {
            log.error("发送群聊信息失败: {}", response.getStatusCode());
        }
    }

    /**
     * 富媒体校验
     * @param GroupOpenId
     * @param fileType
     * @param url
     * @param srvSendMsg
     * @return
     * @throws JsonProcessingException
     */
    public Media media(String GroupOpenId, int fileType, String url, boolean srvSendMsg)throws JsonProcessingException{
        //媒体类型：1 图片，2 视频，3 语音，4 文件（暂不开放）
        //资源格式要求
        //图片：png/jpg，视频：mp4，语音：silk
        // 构造请求头
        HttpHeaders headers = tokenService.createHeaders(); // 使用TokenService中的方法创建请求头
        // 构造请求体
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("file_type", fileType);
        requestBody.put("url", url);
        requestBody.put("srv_send_msg", srvSendMsg);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.sgroup.qq.com/v2/groups/" + GroupOpenId + "/files",
                requestEntity,
                String.class
        );
        // 解析响应（使用 Jackson）
        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readValue(response.getBody(), Media.class);
        } else {
            return null;
        }
    }
}
