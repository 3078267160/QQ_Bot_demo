package com.qq_bot_demo.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 事件对象
 * @param <T>
 */
@Data
public class Payload<T> implements Serializable {
    /**
     * 0	Dispatch	Receive	服务端进行消息推送
     * 1	Heartbeat	Send/Receive	客户端或服务端发送心跳
     * 2	Identify	Send	客户端发送鉴权
     * 6	Resume	Send	客户端恢复连接
     * 7	Reconnect	Receive	服务端通知客户端重新连接
     * 9	Invalid Session	Receive	当 identify 或 resume 的时候，如果参数有错，服务端会返回该消息
     * 10	Hello	Receive	当客户端与网关建立 ws 连接之后，网关下发的第一条消息
     * 11	Heartbeat ACK	Receive/Reply	当发送心跳成功之后，就会收到该消息
     * 12	HTTP Callback ACK	Reply	仅用于 http 回调模式的回包，代表机器人收到了平台推送的数据
     */
    @JsonProperty("op")
    private Integer op;
    @JsonProperty("d")
    private T d;
    @JsonProperty("s")
    private Integer s;
    @JsonProperty("t")
    private String t;
    @JsonProperty("id")
    private String id;
}
