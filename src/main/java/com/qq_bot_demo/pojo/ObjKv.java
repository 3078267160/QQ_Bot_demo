package com.qq_bot_demo.pojo;

import lombok.Data;

@Data
public class ObjKv {
    private String key;
    private String value;

    public ObjKv(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // getter 方法
    // 必须添加 getter
    public String getKey() { return key; }
    public String getValue() { return value; }
}
