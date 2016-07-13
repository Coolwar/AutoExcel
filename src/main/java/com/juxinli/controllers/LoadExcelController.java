package com.juxinli.controllers;

import com.google.gson.JsonObject;
import com.juxinli.response.Msg;
import com.juxinli.services.LoadExcelService;
import com.juxinli.tools.ToolJson;
import com.juxinli.tools.Utils;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/3.
 */
@RestController
@RequestMapping("/service/get/")
public class LoadExcelController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private LoadExcelService loadExcelService;

    @RequestMapping(value = "/getExcel", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public String getExcel(HttpServletRequest request) throws Exception {

        Cache excelCache = cacheManager.getCache("excelCache");
        String sessionId = request.getSession().getId();

        Cache.ValueWrapper valueWrapper = excelCache.get(sessionId);
        if (valueWrapper == null)
            return new Msg("excelData", null).append("excelMap", null).append("successMsg", "请先上传数据").toJson();
        Map<String, Object> jsonStr = (Map<String, Object>) valueWrapper.get();
        if (jsonStr.size() == 0)
            return new Msg("excelData", null).append("excelMap", null).append("successMsg", "请先上传数据").toJson();

        return Utils.object2Json(jsonStr);
    }

    @RequestMapping(value = "/lookExcel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public String lookExcel(@RequestBody String jsonStr, HttpServletRequest request) throws Exception {

        ToolJson json = new ToolJson();
        JsonObject jsonObject = json.parser_json(jsonStr);
        String sessionId = request.getSession().getId();
        Cache excelCache = cacheManager.getCache("excelCache");

        String id_crad_idx = json.value("id_crad_idx", jsonObject, "id_card_idx", "");
        String name_idx = json.value("name_idx", jsonObject, "name_idx", "");

        Cache.ValueWrapper valueWrapper = excelCache.get(sessionId);
        if (valueWrapper == null)
            return new Msg("excelData", null).append("excelMap", null).append("successMsg", "请先上传数据").toJson();

        Map<String, Object> excelData = (Map<String, Object>) excelCache.get(sessionId).get();
        if (excelData == null || excelData.toString().equals("{}"))
            return new Msg("excelData", null).append("successMsg", "你没有上传数据").toJson();

        if (jsonObject.toString().equals("{}"))
            return new Msg("excelData", null).append("successMsg", "你没有标记数据").toJson();

        if (jsonObject.get("categories") == null)
            return new Msg("excelData", null).append("successMsg", "你没有选择分类").toJson();

        if (id_crad_idx.equals("") || name_idx.equals(""))
            return new Msg("excelData", null).append("successMsg", "至少标记姓名和身份证").toJson();

        List<DBObject> list = loadExcelService.updateExcel2Mongo(jsonObject, excelData);

        Cache mongoCache = cacheManager.getCache("mongoCache");
        mongoCache.put(sessionId, list);

        return new Msg("excelData", list).append("successMsg", "Excel数据转mongo成功").toJson();
    }
}
