package com.qq_bot_demo.controller;

import com.qq_bot_demo.config.Event;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.NamedParameterSpec;
import java.util.HexFormat;

//回调端口
@Slf4j
@RestController
@RequestMapping("/webhook")
public class QQBotCallbackController {

    @Value("${qq.bot.client-secret}")
    private String clientSecret; // 从配置读取机器人密钥
    @Autowired
    private Event event;
    /**
     * 处理所有回调请求（包括事件推送和验证）
     */
    @PostMapping
    public ResponseEntity<String> handleCallback(
            @RequestHeader("X-Signature-Ed25519") String signatureHeader,
            @RequestHeader("X-Signature-Timestamp") String timestampHeader,
            @RequestBody(required = false) byte[] rawBodyBytes) {

        try {
            // 1. 接收原始字节并解析为JSON
            String rawBody = (rawBodyBytes != null) ?
                    new String(rawBodyBytes, StandardCharsets.UTF_8) : "";
            JSONObject payload = new JSONObject(rawBody);

            // 2. 判断是否为验证请求（OPCODE 13）
            if (payload.getInt("op") == 13) {
                log.info("通过");
                JSONObject data = payload.getJSONObject("d");
                String plainToken = data.getString("plain_token");
                String eventTs = data.getString("event_ts");

                // 3. 生成签名
                String signature = generateValidationSignature(plainToken, eventTs, clientSecret);

                // 4. 构造验证响应（原始数据格式）
                JSONObject response = new JSONObject();
                response.put("plain_token", plainToken);
                response.put("signature", signature);

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.toString());
            }
            // 5. 如果是事件通知（OPCODE 0）
            if (payload.getInt("op") == 0) {
                boolean isValid = verifyRequestSignature(
                        clientSecret,
                        timestampHeader,
                        rawBody,
                        signatureHeader
                );
                if (isValid) {
                    // 处理业务逻辑（如消息事件）
                    log.info("收到有效事件: {}", payload);
                    event.event(payload);
                    return ResponseEntity.ok().build();
                } else {
                    log.warn("签名验证失败");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();

        } catch (Exception e) {
            log.error("处理回调异常", e);
            return ResponseEntity.internalServerError().body("SERVER_ERROR");
        }
    }


    // 专用验证签名生成方法
    private String generateValidationSignature(
            String plainToken,
            String eventTs,
            String clientSecret
    ) throws Exception {
        // 生成种子（与之前代码相同）
        byte[] seed = new byte[32];
        byte[] secretBytes = clientSecret.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < 32; i++) {
            seed[i] = secretBytes[i % secretBytes.length];
        }

        // 生成密钥对
        SecureRandom random = new FixedSeedRandom(seed);
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519");
        kpg.initialize(new NamedParameterSpec("Ed25519"), random);
        KeyPair keyPair = kpg.generateKeyPair();

        // 构造消息
        String message = eventTs + plainToken;
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        // 签名
        Signature sig = Signature.getInstance("Ed25519");
        sig.initSign(keyPair.getPrivate());
        sig.update(messageBytes);
        return HexFormat.of().formatHex(sig.sign());
    }

    // 签名验证方法（用于普通事件）
    private boolean verifyRequestSignature(
            String clientSecret,
            String timestamp,
            String rawBody,
            String signatureHeader
    ) throws Exception {
        PublicKey publicKey = generatePublicKey(clientSecret);
        byte[] message = (timestamp + rawBody).getBytes(StandardCharsets.UTF_8);
        byte[] signature = HexFormat.of().parseHex(signatureHeader);

        Signature sig = Signature.getInstance("Ed25519");
        sig.initVerify(publicKey);
        sig.update(message);
        return sig.verify(signature);
    }
    /**
     * 根据官方规范生成公钥
     */
    private PublicKey generatePublicKey(String secret) throws Exception {
        // 生成符合规范的种子（重复填充至32字节）
        byte[] seed = generateSeed(secret);
        log.debug("生成的种子: {}", HexFormat.of().formatHex(seed));

        // 使用确定性的随机数生成器
        SecureRandom random = new FixedSeedRandom(seed);

        // 生成密钥对
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519");
        kpg.initialize(new NamedParameterSpec("Ed25519"), random);
        KeyPair keyPair = kpg.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        log.debug("生成的公钥: {}", HexFormat.of().formatHex(publicKey.getEncoded()));


        return keyPair.getPublic();
    }
    /**
     * 生成符合官方要求的种子（长度=32）
     */
    private byte[] generateSeed(String secret) {
        byte[] seed = new byte[32];
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);

        // 严格按官方规范重复填充
        for (int i = 0; i < 32; i++) {
            seed[i] = secretBytes[i % secretBytes.length];
        }
        return seed;
    }
    /**
     * 固定种子的随机数生成器（确保相同secret生成相同密钥）
     */
    private static class FixedSeedRandom extends SecureRandom {
        private final byte[] seed;
        private int position = 0;

        FixedSeedRandom(byte[] seed) {
            this.seed = seed.clone();
        }

        @Override
        public void nextBytes(byte[] bytes) {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = seed[position % seed.length];
                position++;
            }
        }
    }
}
