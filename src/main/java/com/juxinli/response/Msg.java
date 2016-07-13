package com.juxinli.response;


import com.juxinli.tools.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回消息对象
 * Created by chengsiqin on 2015/5/8.
 *
 * @author L
 */
public class Msg {

    private Map<String, Object> map;

    public Msg() {
        this.map = new HashMap<>();
    }

    public Msg(String str, Object obj) {
        this.map = new HashMap<>();
        this.map.put(str, obj);
    }

    public Msg append(String str, Object obj) {
        map.put(str, obj);
        return this;
    }

    public String toJson() {
        return Utils.object2Json(map);
    }
}
