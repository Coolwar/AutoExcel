/**
 *
 */
package com.juxinli.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author larry
 * @version 创建时间 2013-6-14 下午3:56:48
 * @email larry.lv.word@gmail.com
 */
@SuppressWarnings("unused")
public class ToolJson {
    private static Logger logger = Logger.getLogger(ToolJson.class.getName());
    private String PAGE = "";// 记录当前的 page 的 key
    private static String AQL_SEPARATOR = ".";
    private JsonParser jsonparser = new JsonParser();

    public static void main(String[] args) throws IOException, ParseException {
    }

    public JsonObject[] get_jsons_safety(Map<String, String[]> parameterMap, String page) {
        PAGE = page;
        String[] strings = parameterMap.get(page);
        if (strings == null || strings.length == 0) {
            strings = new String[]{};
            logger.warn("数据源".concat(page).concat("页面为空 "));
            return new JsonObject[0];
        }
        JsonObject[] json_objects = new JsonObject[strings.length];
        for (int i = 0; i < strings.length; i++) {
            json_objects[i] = parser_json(strings[i]);
        }
        return json_objects;
    }

    public JsonObject get_json_safety(Map<String, String[]> parameterMap, String page) {
        String defaule_value = "{}";
        PAGE = page;
        String[] strings = parameterMap.get(page);
        if (strings != null && strings.length >= 1) {
            defaule_value = strings[0];
        } else {
            logger.warn("数据源页面为空");
        }
        JsonObject json = parser_json(defaule_value);
        return json;
    }

    /**
     * 解析 js 方法中的 json 数据
     *
     * @param value
     * @return
     */
    public JsonObject parser_json(String value) {
        JsonObject json_object = null;

        if (ToolGlobal.is_empty(value) || value.equals("[]") || value.equals("timeout")) {
            logger.warn("待解析json数据为空");
            return null;
        }

        int start = value.indexOf("{");
        int end = value.lastIndexOf("}");

        if (start > -1 && end > -1) {
            try {
                json_object = jsonparser.parse(value.substring(start, end + 1)).getAsJsonObject();
            } catch (Exception e) {
                logger.warn("字符串转json失败", e);
            }
        }

        return json_object;
    }

    // ///////////////////////////////////////////////////////////////公有方法/////////////////////////////////////////////////////////////////////////////

    /**
     * 获取安全方法链的 jsonarray
     */
    public JsonArray array(String attrbute, JsonObject json, String aql) {
        logger.debug("#获取 json 节点组 safety ".concat(aql));
        JsonArray json_array = new JsonArray();
        StringTokenizer token = new StringTokenizer(aql, AQL_SEPARATOR);
        try {
            while (token.hasMoreElements()) {
                String args_string = (String) token.nextElement();
                if (token.hasMoreElements()) {
                    json = get_jsonobject_byjsonobject(json, args_string);
                } else {
                    json_array = json.get(args_string).getAsJsonArray();
                }
            }
        } catch (Exception e) {
            logger.warn("解析 json 节点".concat(attrbute).concat("错误:").concat(aql), e);
        }
        if (json_array == null) {
            json_array = new JsonArray();
        }
        return json_array;
    }

    /**
     * 获取安全方法链的值，方法中出现错误返回传入的 value
     */
    public String value(String attrbute, JsonObject json, String aql, String defaule_result) {
        logger.debug("#获取 json 节点 safety ".concat(aql));
        JsonObject json_object = json;
        StringTokenizer token = new StringTokenizer(aql, AQL_SEPARATOR);
        Object tmp_result = null;
        try {
            while (token.hasMoreElements()) {
                String args_string = (String) token.nextElement();
                if (token.hasMoreElements()) {
                    json_object = get_jsonobject_byjsonobject(json_object, args_string);
                } else {
                    tmp_result = json_object.get(args_string).getAsString();
                }
            }
        } catch (Exception e) {
            String message = "解析 json 节点".concat(attrbute).concat("错误:").concat(aql);
            logger.warn(message, e);
        }
        defaule_result = (tmp_result == null) ? defaule_result : ToolGlobal.trim(tmp_result.toString()).replaceAll("\"", "");
        return defaule_result;
    }

    /**
     * 获取方法链的值
     */
    public String value_with_error(String attrbute, JsonObject json, String aql, String defaule_result) {
        logger.debug("#获取 json 节点 safety ".concat(aql));
        JsonObject json_object = json;
        StringTokenizer token = new StringTokenizer(aql, AQL_SEPARATOR);
        Object tmp_result = null;
        try {
            while (token.hasMoreElements()) {
                String args_string = (String) token.nextElement();
                if (token.hasMoreElements()) {
                    json_object = get_jsonobject_byjsonobject(json_object, args_string);
                } else {
                    tmp_result = json_object.get(args_string).getAsString();
                }
            }
        } catch (Exception e) {
            logger.warn("解析 json 节点".concat(attrbute).concat("错误:").concat(aql), e);
        }
        defaule_result = (tmp_result == null) ? defaule_result : ToolGlobal.trim(tmp_result.toString()).replaceAll("\"", "");
        return defaule_result;
    }

    // ///////////////////////////////////////////////////////////////私有方法//////////////////////////////////////////////////////////////////////

    /**
     * 获取一个节点
     *
     * @param json_object json对象
     * @param args_string 取值
     * @return json对象
     * @throws ParseException
     */
    private JsonObject get_jsonobject_byjsonobject(JsonObject json_object, String args_string) throws ParseException {
        json_object = json_object.get(args_string).getAsJsonObject();
        return json_object;
    }

}