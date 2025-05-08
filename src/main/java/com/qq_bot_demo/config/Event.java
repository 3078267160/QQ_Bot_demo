package com.qq_bot_demo.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qq_bot_demo.event.GroupEvent;
import com.qq_bot_demo.pojo.GroupPayload;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息接受与判定转发
 */
@Slf4j
@Component
public class Event {
    @Autowired
    private GroupEvent groupEvent;
    public void event(JSONObject object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            switch (object.getString("t")){
                //私聊事件
                case "C2C_MESSAGE_CREATE":
                    log.info("收到私聊请求：{}", object);
                    break;
                //群聊艾特事件
                case "GROUP_AT_MESSAGE_CREATE":
                    GroupPayload groupPayload = objectMapper.readValue(object.toString(), GroupPayload.class);
                    log.info("收到群聊请求：{}", object);
                    //转发到群聊接口进行功能判断处理方案   你也可以自定义更好的功能处理方案
                    groupEvent.groupMsg(groupPayload);
                    break;
                //频道消息事件
                case "MESSAGE_CREATE":
                    log.info("收到频道请求：{}", object);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("事件判定异常", e);
        }
    }
}
/**
 * GUILDS (1 << 0)
 *   - GUILD_CREATE           // 当机器人加入新guild时
 *   - GUILD_UPDATE           // 当guild资料发生变更时
 *   - GUILD_DELETE           // 当机器人退出guild时
 *   - CHANNEL_CREATE         // 当channel被创建时
 *   - CHANNEL_UPDATE         // 当channel被更新时
 *   - CHANNEL_DELETE         // 当channel被删除时
 *
 * GUILD_MEMBERS (1 << 1)
 *   - GUILD_MEMBER_ADD       // 当成员加入时
 *   - GUILD_MEMBER_UPDATE    // 当成员资料变更时
 *   - GUILD_MEMBER_REMOVE    // 当成员被移除时
 *
 * GUILD_MESSAGES (1 << 9)    // 消息事件，仅 *私域* 机器人能够设置此 intents。
 *   - MESSAGE_CREATE         // 发送消息事件，代表频道内的全部消息，而不只是 at 机器人的消息。内容与 AT_MESSAGE_CREATE 相同
 *   - MESSAGE_DELETE         // 删除（撤回）消息事件
 *
 * GUILD_MESSAGE_REACTIONS (1 << 10)
 *   - MESSAGE_REACTION_ADD    // 为消息添加表情表态
 *   - MESSAGE_REACTION_REMOVE // 为消息删除表情表态
 *
 * DIRECT_MESSAGE (1 << 12)
 *   - DIRECT_MESSAGE_CREATE   // 当收到用户发给机器人的私信消息时
 *   - DIRECT_MESSAGE_DELETE   // 删除（撤回）消息事件
 *
 *
 * GROUP_AND_C2C_EVENT (1 << 25)
 *   - C2C_MESSAGE_CREATE      // 用户单聊发消息给机器人时候
 *   - FRIEND_ADD              // 用户添加使用机器人
 *   - FRIEND_DEL              // 用户删除机器人
 *   - C2C_MSG_REJECT          // 用户在机器人资料卡手动关闭"主动消息"推送
 *   - C2C_MSG_RECEIVE         // 用户在机器人资料卡手动开启"主动消息"推送开关
 *   - GROUP_AT_MESSAGE_CREATE // 用户在群里@机器人时收到的消息
 *   - GROUP_ADD_ROBOT         // 机器人被添加到群聊
 *   - GROUP_DEL_ROBOT         // 机器人被移出群聊
 *   - GROUP_MSG_REJECT        // 群管理员主动在机器人资料页操作关闭通知
 *   - GROUP_MSG_RECEIVE       // 群管理员主动在机器人资料页操作开启通知
 *
 * INTERACTION (1 << 26)
 *   - INTERACTION_CREATE     // 互动事件创建时
 *
 * MESSAGE_AUDIT (1 << 27)
 *   - MESSAGE_AUDIT_PASS     // 消息审核通过
 *   - MESSAGE_AUDIT_REJECT   // 消息审核不通过
 *
 * FORUMS_EVENT (1 << 28)  // 论坛事件，仅 *私域* 机器人能够设置此 intents。
 *   - FORUM_THREAD_CREATE     // 当用户创建主题时
 *   - FORUM_THREAD_UPDATE     // 当用户更新主题时
 *   - FORUM_THREAD_DELETE     // 当用户删除主题时
 *   - FORUM_POST_CREATE       // 当用户创建帖子时
 *   - FORUM_POST_DELETE       // 当用户删除帖子时
 *   - FORUM_REPLY_CREATE      // 当用户回复评论时
 *   - FORUM_REPLY_DELETE      // 当用户回复评论时
 *   - FORUM_PUBLISH_AUDIT_RESULT      // 当用户发表审核通过时
 *
 * AUDIO_ACTION (1 << 29)
 *   - AUDIO_START             // 音频开始播放时
 *   - AUDIO_FINISH            // 音频播放结束时
 *   - AUDIO_ON_MIC            // 上麦时
 *   - AUDIO_OFF_MIC           // 下麦时
 *
 * PUBLIC_GUILD_MESSAGES (1 << 30) // 消息事件，此为公域的消息事件
 *   - AT_MESSAGE_CREATE       // 当收到@机器人的消息时
 *   - PUBLIC_MESSAGE_DELETE   // 当频道的消息被删除时
 */
