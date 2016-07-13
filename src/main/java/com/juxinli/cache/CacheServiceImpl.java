package com.juxinli.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Map;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/8.
 */
public class CacheServiceImpl implements CacheService {

    @Override
    @Cacheable(value = "excelCache", key = "#sessionId")
    public Map<String, Object> save2ExcelCache(Map<String, Object> map, String sessionId) {
        return map;
    }

    @Override
    @Cacheable(value = "mongoCache", key = "#sessionId")
    public Map<String, Object> save2MongoCache(Map<String, Object> map, String sessionId) {
        return map;
    }

    @Override
    @CacheEvict(value = "excelCache", key = "#sessionId")
    public void clear2ExcelCache(String sessionId) {

    }

    @Override
    @CacheEvict(value = "mongoCache", key = "#sessionId")
    public void clear2MongoCache(String sessionId) {

    }
}
