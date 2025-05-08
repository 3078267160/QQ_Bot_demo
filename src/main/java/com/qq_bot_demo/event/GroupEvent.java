package com.qq_bot_demo.event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.qq_bot_demo.component.MessageRequest;
import com.qq_bot_demo.component.SendMessagee;
import com.qq_bot_demo.pojo.GroupPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 群聊相关功能
 */
@Slf4j
@Component
public class GroupEvent {
    @Autowired
    private SendMessagee sendMessagee;
    public void groupMsg(GroupPayload payload) throws JsonProcessingException {
        //去除空格后接受的Content消息
        String rawMessage = payload.getD().getContent().trim();
        //消息测试
        if (rawMessage.equals("测试")){
            //消息构建
            MessageRequest request = MessageRequest.builder()
                    .msgType(0)
                    .content("你好啊~")
                    .eventId(payload.getId())
                    .msgId(payload.getD().getMessageId())
                    .msgSeq(1)
                    .build();
            //消息发送
            sendMessagee.sendGroupMsg(payload.getD().getGroupOpenid(),request);
        }
    }

}
