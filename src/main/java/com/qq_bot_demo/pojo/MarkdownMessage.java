package com.qq_bot_demo.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MarkdownMessage  {
    private String content;
    private final Integer msgType = 2;
    private Markdown markdown;

    /**
     * 构建空消息对象
     */
    public static MarkdownMessage  builder() {
        return new MarkdownMessage ();
    }

    /**
     * Markdown结构体
     */
    @Data
    public static class Markdown {
        /**
         * 模板ID（从QQ机器人平台申请）
         */
        @JsonProperty("custom_template_id")
        private String templateId;

        /**
         * 模板参数列表
         */
        @JsonProperty("params")
        private List<Param> params = new ArrayList<>();
    }

    /**
     * 模板参数项
     */
    @Data
    public static class Param {
        /**
         * 参数键（对应content中的<@key>）
         */
        @JsonProperty("key")
        private String key;

        /**
         * 参数值数组（支持多值参数）
         * 示例：按钮参数 ["按钮文字", "#颜色值", "链接"]
         */
        @JsonProperty("values")
        private List<String> values;
    }

    // 以下为链式构建方法
    public MarkdownMessage content(String content) {
        this.content = content;
        return this;
    }

    public MarkdownMessage templateId(String templateId) {
        if (this.markdown == null) {
            this.markdown = new Markdown();
        }
        this.markdown.setTemplateId(templateId);
        return this;
    }

    public MarkdownMessage addParam(String key, String... values) {
        if (this.markdown == null) {
            this.markdown = new Markdown();
        }
        Param param = new Param();
        param.setKey(key);
        param.setValues(List.of(values));
        this.markdown.getParams().add(param);
        return this;
    }
}

