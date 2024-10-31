package com.work.workorganization;

public class replaceSubString {
    public static void main(String[] args) {
        String v1 = "张三,李四";
        String v2 = "张三";
        String replace = "王五";

        String newStr = replaceSubstring(v1, v2, replace);
        System.out.println(newStr);
    }


    public static String replaceSubstring(String v1, String v2, String replace) {
        if (v1 == null || v2 == null || replace == null) {
            return v1;
        }

        if (v1.contains(v2)) {
            return v1.replace(v2, replace);
        }
        return v1;
    }


}
