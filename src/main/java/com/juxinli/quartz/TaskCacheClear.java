package com.juxinli.quartz;

import com.juxinli.tools.HttpUtils;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/8.
 */
public class TaskCacheClear {

    public void cacheClear() {

        HttpUtils.doGet("http://localhost:8080/service/clear/clearAll.do");
        HttpUtils.doGet("http://localhost:8080/AutoExcel/service/clear/clearAll.do");

    }
}
