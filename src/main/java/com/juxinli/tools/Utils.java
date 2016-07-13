package com.juxinli.tools;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author wendong
 * @email wendong@juxinli.com
 * @date 2016/3/2.
 */
public class Utils {

    private static Gson gson = new Gson();

    /**
     * object转json字符串
     * @param object object
     * @return str
     */
    public static String object2Json(Object object) {
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 带星号身份证转对应正则表达式
     * @param num 身份证
     * @return 正则表达式
     */
    public static String IDStar2Regex(String num){
        if (num == null)
            return null;
        StringBuilder sb = new StringBuilder("^");
        for (int i = 0; i < num.length(); i++) {
            if (num.charAt(i) == '*'){
                sb.append("[0-9*]");
            }else if (num.charAt(i) == 'X' || num.charAt(i) == 'x'){
                sb.append("[Xx*]");
            }else {
                sb.append("[")
                        .append(num.charAt(i))
                        .append("*]");
            }
        }
        String res = sb.append("$").toString();
        if (res.endsWith("[0-9*]$")){
            res = res.substring(0, res.length()-6).concat("[0-9Xx*]");
        }
        return res;
    }

    /** 身份证相似度阈值 */
    private final static int DEFAULT_ID_SIMILAR_THRESHOLD = 6;

    /**
     * 身份证相似度计算
     * @param cur 当前身份证
     * @param fresh 新身份证
     * @return true：相似
     */
    public static boolean idCardSimilarity(String cur, String fresh){
        if (cur == null || fresh == null)
            return false;
        int starCount = 0;
        for (int i = 0; i < cur.length(); i++) {
            if (cur.charAt(i) == '*' ||
                    fresh.charAt(i) == '*')
                starCount++;
        }
        return starCount <= DEFAULT_ID_SIMILAR_THRESHOLD;
    }

    /**
     * 身份证信息合并
     * @param cur 当前身份证
     * @param fresh 新身份证
     * @return 合并后结果
     */
    public static String starMerge(String cur, String fresh){
        if (cur == null || fresh == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cur.length(); i++) {
            if (cur.charAt(i) != '*')
                sb.append(cur.charAt(i));
            else
                sb.append(fresh.charAt(i));
        }
        return sb.toString();
    }

    /**
     * 号码信息合并
     * @param cur 当前号码
     * @param fresh 新号码
     * @return 合并后结果
     */
    public static String mobileMerge(String cur, String fresh){
        if (cur == null || fresh == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cur.length(); i++) {
            if (cur.charAt(i) != '*')
                sb.append(cur.charAt(i));
            else
                sb.append(fresh.charAt(i));
        }
        return sb.toString();
    }

    /**
     * list做并集
     * @param cur 当前list集合
     * @param fresh 新list集合
     * @return 合并后集合
     */
    public static BasicDBList listMerge(BasicDBList cur,
                                        BasicDBList fresh){
        if (cur == null && fresh == null)
            return new BasicDBList();
        if (cur == null)
            return fresh;
        if (fresh == null)
            return cur;
        Set<String> mergeSet = new HashSet<String>();
        for (Object cv : cur)
            mergeSet.add((String) cv);
        for (Object fv : fresh)
            mergeSet.add((String) fv);
        BasicDBList res = new BasicDBList();
        for (String rv : mergeSet)
            res.add(rv);
        return res;
    }

    /**
     * map信息合并
     * @param cur 当前map集合
     * @param fresh 新map集合
     * @return 合并后结果
     */
    public static BasicDBObject mapMerge(BasicDBObject cur,
                                         BasicDBObject fresh){
        if (cur == null && fresh == null)
            return new BasicDBObject();
        if (cur == null)
            return fresh;
        if (fresh == null)
            return cur;
        cur.putAll(fresh.toMap());
        return cur;
    }


    /**
     * 电话号码合并
     * @param cur 当前电话号码集合
     * @param fresh 新增电话集合
     * @return 合并后电话集合
     */
    public static BasicDBList mobileMerge(BasicDBList cur,
                                          BasicDBList fresh){
        if (cur == null && fresh == null)
            return new BasicDBList();
        if (cur == null)
            return fresh;
        if (fresh == null)
            return cur;
        Set<String> mobiSet = new HashSet<String>();
        boolean onOff;
        for (Object fmobi: fresh){
            onOff = true;
            for (Object cmobi: cur){
                if (mobileSimilarity((String)cmobi, (String)fmobi)){
                    onOff = false;
                    mobiSet.add(starMerge((String)cmobi, (String)fmobi));
                }else
                    mobiSet.add((String)cmobi);
            }
            if (onOff)
                mobiSet.add((String)fmobi);
        }
        BasicDBList res = new BasicDBList();
        for (String mobi: mobiSet)
            res.add(mobi);
        return res;
    }

    /**
     * 判断电话是否相似
     * @param cur 当前电话
     * @param fresh 新增电话
     * @return 是否相似
     */
    public static boolean mobileSimilarity(String cur, String fresh){
        if (cur == null || fresh == null)
            return true;
        Pattern mobiPattern = Pattern.compile(mobileStar2Regex(fresh));
        return mobiPattern.matcher(cur).matches();
    }

    /**
     * 带星号电话转正则
     * @param mobile 电话号码
     * @return 正则表达式
     */
    public static String mobileStar2Regex(String mobile){
        if (mobile == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mobile.length(); i++) {
            if (mobile.charAt(i) == '*')
                sb.append("[0-9*]");
            else
                sb.append("[")
                        .append(mobile.charAt(i))
                        .append("*]");
        }
        return sb.append("$").toString();
    }

}
