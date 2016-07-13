package com.juxinli.controllers;

import com.juxinli.response.Msg;
import com.juxinli.services.SvaeExcel2MongoService;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/7.
 */
@RestController
@RequestMapping("/service/save/")
public class SaveExcel2MongoController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private SvaeExcel2MongoService svaeExcel2MongoService;

    @RequestMapping(value = "/saveExcel2Mongo", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public String saveExcel(HttpServletRequest request) throws Exception {

        String sessionId = request.getSession().getId();
        Cache mongoCache = cacheManager.getCache("mongoCache");
        Cache.ValueWrapper valueWrapper = mongoCache.get(sessionId);
        if (valueWrapper == null)
            return new Msg("error", "请先生成mongo数据").toJson();

        List<DBObject> list = (List<DBObject>) valueWrapper.get();
        if (list == null || list.size() == 0) {
            return new Msg("error", "请先生成mongo数据").toJson();
        }

        return svaeExcel2MongoService.saveExcel2Mongo(list);
    }

}
