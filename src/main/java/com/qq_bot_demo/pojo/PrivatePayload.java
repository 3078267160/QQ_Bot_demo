package com.qq_bot_demo.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 私聊事件对象
 */
@Data
public class PrivatePayload implements Serializable {
    @JsonProperty("op")
    private Integer op;

    @JsonProperty("d")
    private D d;

    @JsonProperty("t")
    private String t;

    @JsonProperty("id")
    private String id;

    @Data
    public static class D {
        @JsonProperty("message_scene")
        private MessageScene messageScene;

        @JsonProperty("author")
        private Author author;

        @JsonProperty("message_type")
        private String messageType;

        @JsonProperty("id")
        private String messageId;

        @JsonProperty("content")
        private String content;

        @JsonProperty("timestamp")
        private Date timestamp;

        @Data
        public static class MessageScene {
            @JsonProperty("source")
            private String source;

            @JsonProperty("callback_data")
            private String callbackData;
        }

        @Data
        public static class Author {
            @JsonProperty("union_openid")
            private String unionOpenid;

            // 假设这是用户在平台上的唯一标识符
            @JsonProperty("user_openid")
            private String userOpenid;

            @JsonProperty("id")
            private String id;
        }
    }
}
