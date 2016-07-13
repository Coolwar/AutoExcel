package com.juxinli.cache;

import java.util.Map;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/8.
 */
public interface CacheService {

    Map<String, Object> save2ExcelCache(Map<String, Object> map, String sessionId);

    Map<String, Object> save2MongoCache(Map<String, Object> map, String sessionId);

    //清除掉指定key的缓存
    void clear2ExcelCache(String sessionId);

    //清除掉指定key的缓存
    void clear2MongoCache(String sessionId);

}
