package com.qq_bot_demo.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ArkMessage {
    @JsonProperty("template_id")
    private int templateId;
    private List<KvItem> kv;

    // 必须添加 getter
    public int getTemplateId() { return templateId; }
    public List<KvItem> getKv() { return kv; }

    // 构造函数、getter、setter
    public ArkMessage(int templateId, List<KvItem> kv) {
        this.templateId = templateId;
        this.kv = kv;
    }
}

