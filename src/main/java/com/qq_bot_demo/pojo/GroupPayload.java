package com.qq_bot_demo.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群聊事件对象
 */
@Data
public class GroupPayload implements Serializable {
    @JsonProperty("op")
    private Integer op;
    @JsonProperty("d")
    private D d;
    @JsonProperty("s")
    private Integer s;
    @JsonProperty("t")
    private String t;
    @JsonProperty("id")
    private String id;//事件id

    @Data
    public static class D{
        @JsonProperty("group_openid")
        private String groupOpenid;//群ID
        @JsonProperty("message_scene")
        private message_scene messageScene;
        @JsonProperty("group_id")
        private String groupId;
        @JsonProperty("author")
        private Author author;
        @JsonProperty("message_type")
        private String messageType;
        @JsonProperty("id")
        private String messageId;//消息id
        @JsonProperty("content")//群消息内容
        private String content;
        @JsonProperty("timestamp")
        private Date timestamp;



        @Data
        public static class message_scene {
            @JsonProperty("source")
            private String source;
            @JsonProperty("callback_data")
            private String callbackData;
        }
        @Data
        public static class Author {
            @JsonProperty("union_openid")
            private String unionOpenid;
            @JsonProperty("id")
            private String id;
            @JsonProperty("member_openid")
            private String memberOpenid;//群员id
        }
    }
}
