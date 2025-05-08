package com.qq_bot_demo.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GuildPayload {
    @JsonProperty("op")
    private Integer op;
    @JsonProperty("d")
    private D d;
    @JsonProperty("s")
    private Integer s;
    @JsonProperty("t")
    private String t;
    @JsonProperty("id")
    private String id;

    @Data
    public static class D{
        //在频道内的序列号
        @JsonProperty("seq_in_channel")
        private String seqInChannel;
        //发送者的信息
        @JsonProperty("author")
        private Author author;
        //被提及用户的列表
        @JsonProperty("mentions")
        private List<Mention> mentions;
        //频道ID
        @JsonProperty("guild_id")
        private String guildId;
        //成员信息
        @JsonProperty("member")
        private Member member;
        //消息ID
        @JsonProperty("id")
        private String id;
        //子频道ID
        @JsonProperty("channel_id")
        private String channelId;
        //消息内容
        @JsonProperty("content")
        private String content;
        //序列号
        @JsonProperty("seq")
        private int seq;
        //时间戳，记录消息发送的时间
        @JsonProperty("timestamp")
        private String timestamp;

        @Data
        public static class Author {
            //用户的OpenID
            @JsonProperty("union_openid")
            private String unionOpenid;
            //标识是否为机器人账号
            @JsonProperty("bot")
            private boolean bot;
            //用户ID
            @JsonProperty("id")
            private String id;
            //用户头像URL
            @JsonProperty("avatar")
            private String avatar;
            //用户名
            @JsonProperty("username")
            private String username;
        }
        @Data
        public static class Mention{
            //标识是否为机器人账号
            @JsonProperty("bot")
            private boolean bot;
            //用户ID
            @JsonProperty("id")
            private String id;
            //用户头像URL
            @JsonProperty("avatar")
            private String avatar;
            //用户名
            @JsonProperty("username")
            private String username;
        }
        @Data
        public static class Member{
            //成员昵称
            @JsonProperty("nick")
            private String nick;
            //加入时间
            @JsonProperty("joined_at")
            private String joinedAt;
            //角色列表
            @JsonProperty("roles")
            private List<String> roles;
        }
    }
}
