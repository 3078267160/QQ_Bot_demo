package com.qq_bot_demo.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class KvItem {
    private String key;
    private String value;
    private List<ObjItem> obj;

    // 必须添加 getter
    public String getKey() { return key; }
    public String getValue() { return value; }
    public List<ObjItem> getObj() { return obj; }

    private KvItem(String key, String value, List<ObjItem> obj) {
        if ((value == null && obj == null) || (value != null && obj != null)) {
            throw new IllegalArgumentException("必须且只能包含 value 或 obj 其中一个字段");
        }
        this.key = key;
        this.value = value;
        this.obj = obj;
    }

    // 普通键值对构造
    public static KvItem createSimple(String key, String value) {
        return new KvItem(key, value, null);
    }

    // 数组结构构造
    public static KvItem createWithObj(String key, List<ObjItem> obj) {
        return new KvItem(key, null, obj);
    }

    // getter 方法
}
