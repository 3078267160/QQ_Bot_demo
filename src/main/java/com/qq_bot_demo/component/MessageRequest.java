package com.qq_bot_demo.component;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qq_bot_demo.pojo.ArkMessage;
import com.qq_bot_demo.pojo.KeyBoard;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息构建实体类
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true) // 可选：支持链式set
public class MessageRequest {
    private String content;         //文本消息
    @JsonProperty("msg_type")
    private Integer msgType;       //消息类型： 0 文本，2 是 markdown，3 ark 消息，4 embed，7 media 富媒体
    private Object markdown;        //markdown消息
    private KeyBoard keyboard;        //消息按钮
    private ArkMessage ark;             //Ark消息
    private Object media;           //富媒体消息：图片、视频、音频、文件
    @JsonProperty("event_id")
    private String eventId;        //事件ID
    @JsonProperty("msg_id")
    private String msgId;          //消息ID
    @JsonProperty("msg_seq")
    private Integer msgSeq;        //消息序号
}
