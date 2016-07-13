package com.juxinli.services;


import com.juxinli.tools.ReadExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/2.
 */
@Service
public class ExcelService {

    @Autowired
    private ReadExcel ExcelData;

    public Map<String, Object> getJsonStr(InputStream in, String fileName) throws Exception {

        Map<String, Object> json_map = new HashMap<>();
        List<String[]> excelData = ExcelData.getExcelData(in, fileName);

        Map<String, Integer> map = new HashMap<>();

        if (excelData.size() > 2) {
            String[] arr = excelData.get(1);
            boolean flag = true;
            for (int i = 0; i < arr.length; i++) {
                String str = arr[i];
                if (str == null) {
                    map.put("others", i);
                }
                //验证身份证
                else if (str.matches("[0-9*xX]{15}|[0-9*xX]{18}")) {
                    map.put("id_card", i);
                }
                //验证手机号
                else if (str.matches("1[0-9*]{10}|1.[0-9*]{8,10}E[0-9]{2}")) {
                    map.put("mobiles", i);
                }
                //验证姓名
                else if (str.matches("[\u4e00-\u9fa5]{2,4}") && flag) {
                    map.put("name", i);
                    flag = false;
                } else {
                    map.put("others", i);
                }
            }
        }
        json_map.put("excelData", excelData);
        json_map.put("excelMap", map);
        return json_map;
    }
}