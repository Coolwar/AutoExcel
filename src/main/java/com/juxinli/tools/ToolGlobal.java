package com.juxinli.tools;

import java.util.regex.Pattern;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2015/9/16
 */
public class ToolGlobal {


    /**
     * trim
     * 替换中文空格的特殊字符为普通空格
     * 去掉字符串中前后的空格，并将中间多个连续的空格合并成一个
     * 去掉开头出现的空格
     *
     * @param string string
     * @return null
     */
    public static String trim(String string, String _default) {
        if (string == null) {
            return _default;
        }
        return trim(string);
    }

    public static String trim(String string) {
        if (string == null) {
            return null;
        }
        // 去掉特殊空格
        string = string.replace(" ", " ");
        // 并将中间多个连续的空格合并成一个
        String trim = Pattern.compile("[' ']+").matcher(string).replaceAll(" ").trim();
        //
        if (trim.startsWith(" ")) {
            trim = trim.substring(1);
        }
        return trim;
    }


    /**
     * is_empty
     * 判断是否为空
     * 如果参数为 null 或者参数为空返回 false
     *
     * @param string 需要判断的字符串
     * @return 布尔值
     */
    public static Boolean is_empty(String string) {
        if (string == null || ToolGlobal.trim(string).isEmpty()) {
            return true;
        }
        return false;
    }
}
