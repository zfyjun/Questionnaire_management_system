package com.example.ssk.utils;

import java.util.*;

/**
 * @author wangli
 * @create 2022-08-18 17:51
 */
public class  SortUtil {
    /**
     * 对List<Map<String, Object>>中int类型的字段排序
     * @param data 包含key为sort,value为desc,asc的map
     * @param yList 需要排序的list
     * @param type 需要排序的字段
     * @return 返回排序后的List<Map<String, Object>>
     */
    public static List<Map<String, Object>> sortMap(Map<String, Object> data, List<Map<String, Object>> yList, String type) {
        if (Objects.equals(data.get("sort"), "asc")) {
            yList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.get(type).toString())));
            return yList;
        } else {
            Collections.sort(yList, (o1, o2) -> Integer.parseInt(o2.get(type).toString()) - Integer.parseInt(o1.get(type).toString()));
        }
        return yList;
    }
    /**
     * 对List<Map<String, Object>>中double类型的字段排序
     * @param data 包含key为sort,value为desc,asc的map
     * @param yList 需要排序的list
     * @param type 需要排序的字段
     * @return 返回排序后的List<Map<String, Object>>
     */
    public static List<Map<String, Object>> sortDoubleMap(Map<String, Object> data, List<Map<String, Object>> yList, String type) {
        if (Objects.equals(data.get("sort"), "asc")) {
            yList.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.get(type).toString())));
            return yList;
        } else {
            Collections.sort(yList, (o1, o2) -> {
                Double a = Double.parseDouble(o2.get(type).toString());
                Double b = Double.parseDouble(o1.get(type).toString());
                if (a > b) {
                    return 1;
                }
                return -1;
            });
        }
        return yList;
    }

}
