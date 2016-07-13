package com.juxinli.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/8.
 */
@RestController
@RequestMapping("/service/clear/")
public class TaskClearController {

    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = "/clearAll", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public void clearCacheAll() {

        cacheManager.getCache("excelCache").clear();
        cacheManager.getCache("mongoCache").clear();

    }

}
