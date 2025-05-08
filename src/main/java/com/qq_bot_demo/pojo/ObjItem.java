package com.qq_bot_demo.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ObjItem {
    @JsonProperty("obj_kv")
    private List<ObjKv> objKv;

    public ObjItem(List<ObjKv> objKv) {
        this.objKv = objKv;
    }

    // getter 方法
    // 必须添加 getter
    public List<ObjKv> getObjKv() { return objKv; }
}
