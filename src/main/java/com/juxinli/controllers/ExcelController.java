package com.juxinli.controllers;

import com.juxinli.services.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/2.
 */
@Controller
@RequestMapping("/service")
public class ExcelController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ExcelService excelService;

    @RequestMapping(value = "/uploadExcel")
    public String getExcel(MultipartFile file1, HttpServletRequest request) throws IOException {

        Cache dataSourceCache = cacheManager.getCache("excelCache");

        String sessionId = request.getSession().getId();
        InputStream in = file1.getInputStream();
        String fileName = file1.getOriginalFilename();

        Map<String, Object> jsonStr;
        try {

            jsonStr = excelService.getJsonStr(in, fileName);
            dataSourceCache.put(sessionId, jsonStr);
        } catch (Exception e) {
            return "/index.jsp?fileName=不支持该文件，或未选择文件";
        }

        return "/index.jsp?fileName=" + fileName;
    }

}
