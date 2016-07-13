package com.juxinli.services;

import com.google.gson.JsonObject;
import com.juxinli.tools.ToolJson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/4.
 */
@Service
public class LoadExcelService {

    public List<DBObject> updateExcel2Mongo(JsonObject jsonObject, Map<String, Object> map) {

        List<String[]> excelData = (List<String[]>) map.get("excelData");
        List<DBObject> list = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        ToolJson json = new ToolJson();
        String id_crad_idx = json.value("id_crad_idx", jsonObject, "id_card_idx", "");
        String name_idx = json.value("name_idx", jsonObject, "name_idx", "");
        String debt_idx = json.value("debt_idx", jsonObject, "debt_idx", "");
        String mobiles_idx = json.value("mobiles_idx", jsonObject, "mobiles_idx", "");
        String sources = json.value("sources", jsonObject, "sources", "");
        String categories = json.value("categories", jsonObject, "categories", "");
        String clear_idx = json.value("clear_idx", jsonObject, "clear_idx", "");

        int id_card_int = -1, name_int = -1, debt_int = -1;
        List<Integer> mob_list = new ArrayList<>();
        List<Integer> clears_list = new ArrayList<>();
        //加载others映射表
        Map<Integer, String> mapOthers = new HashMap<>();
        for (int i = 0; i < excelData.get(0).length; i++) {
            String s = excelData.get(0)[i];
            if (StringUtils.isNotBlank(s))
                mapOthers.put(i, s);
        }

        //---------------------------------------------------//

        if (StringUtils.isNotBlank(id_crad_idx) && id_crad_idx.matches("\\d+")) {
            id_card_int = Integer.parseInt(id_crad_idx);
            clears_list.add(id_card_int);
        }
        if (StringUtils.isNotBlank(name_idx) && name_idx.matches("\\d+")) {
            name_int = Integer.parseInt(name_idx);
            clears_list.add(name_int);
        }
        if (StringUtils.isNotBlank(debt_idx) && debt_idx.matches("\\d+")) {
            debt_int = Integer.parseInt(debt_idx);
            clears_list.add(debt_int);
        }
        if (StringUtils.isNotBlank(mobiles_idx) && mobiles_idx.matches("[\\d+,?]+")) {
            for (String s : mobiles_idx.split(",")) {
                mob_list.add(Integer.parseInt(s));
                clears_list.add(Integer.parseInt(s));
            }
        }
        if (StringUtils.isNotBlank(clear_idx) && clear_idx.matches("[\\d+,?]+")) {
            for (String s : clear_idx.split(",")) {
                clears_list.add(Integer.parseInt(s));
            }
        }

        int[] clears = new int[clears_list.size()];
        for (int i = 0; i < clears_list.size(); i++) {
            clears[i] = clears_list.get(i);
        }

        for (String[] arr : excelData) {

            BasicDBObject dbo = new BasicDBObject();
            //验证身份证
            if (id_card_int != -1) {
                if (id_card_int < arr.length) {
                    String id_card = arr[id_card_int];
                    if (StringUtils.isNotBlank(id_card)) {
                        id_card = id_card.replaceAll("[^0-9Xx*]", "");
                        if (id_card.matches("[0-9xX\\*]{15}|[0-9xX\\*]{18}"))
                            dbo.put("id_card", id_card);
                        else continue;
                    } else continue;
                }
            }
            //验证姓名
            if (name_int != -1)
                if (name_int < arr.length)
                    dbo.put("name", arr[Integer.parseInt(name_idx)]);
            //验证欠款
            if (debt_int != -1) {
                if (debt_int < arr.length) {
                    String debt = arr[Integer.parseInt(debt_idx)];
                    if (StringUtils.isNotBlank(debt))
                        dbo.put("debt", debt);
                }
            }
            //验证手机号
            BasicDBList mobiles_list = new BasicDBList();
            for (Integer aMob_list : mob_list) {
                if (aMob_list < arr.length) {
                    String s = arr[aMob_list];
                    if (StringUtils.isNotBlank(s)) {
                        String[] split = s.split(" |\\\\|/|,|，|。|、|\r\n|\\+");
                        for (String mobile : split) {
                            if (StringUtils.isNotBlank(mobile) && mobile.matches("[0-9*-]{7,11}"))
                                mobiles_list.add(mobile.replaceAll("[^0-9]", ""));
                            if (StringUtils.isNotBlank(mobile) && mobile.matches("1.[0-9*]{6,10}E[0-9]{2}")) {
                                mobiles_list.add(df.format(Double.parseDouble(mobile)));
                            }
                        }
                    }
                }
                dbo.put("mobiles", mobiles_list);
            }
            //验证数据来源
            if (StringUtils.isNotBlank(sources)) {
                BasicDBList objects = new BasicDBList();
                objects.add(sources);
                dbo.put("sources", objects);
            } else {
                BasicDBList objects = new BasicDBList();
                objects.add(categories);
                dbo.put("sources", objects);
            }
            //验证数据分类
            if (StringUtils.isNotBlank(categories)) {
                BasicDBList objects = new BasicDBList();
                objects.add(categories);
                dbo.put("categories", objects);
            }
            //其他数据
            DBObject others = new BasicDBObject();
            others.put("是否存在失信", "是");
            for (int i = 0; i < mapOthers.entrySet().size(); i++) {
                if (!ArrayUtils.contains(clears, i))
                    if (StringUtils.isNotBlank(arr[i]) && !Objects.equals(arr[i], "null"))
                        others.put(mapOthers.get(i), arr[i]);
            }
            dbo.put("others", others);
            list.add(dbo);
        }

        return list;
    }

    public void saveList2Mongo(List<DBObject> list) {

    }

}
