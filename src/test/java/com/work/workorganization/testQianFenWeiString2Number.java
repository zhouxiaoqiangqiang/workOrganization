package com.work.workorganization;

import java.text.NumberFormat;
import java.text.ParseException;

public class testQianFenWeiString2Number {
    public static void main(String[] args) {
        String numberStr = "1,234.56"; // 带千分位的数字字符串
        String numberStr2 = "0"; // 带千分位的数字字符串
        String numberStr3 = "-1,000,000.01"; // 带千分位的数字字符串
        String numberStr4 = "-2,0,,.00,000.01"; // 带千分位的数字字符串
        double number = parseNumberWithSeparator(numberStr);
        System.out.println(number);
        double number2 = parseNumberWithSeparator(numberStr2);
        System.out.println(number2);
        double number3 = parseNumberWithSeparator(numberStr3);
        System.out.println(number3);
        double number4 = parseNumberWithSeparator(numberStr4);
        System.out.println(number4);
    }

    public static double parseNumberWithSeparator(String numberStr) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        try {
            Number number = numberFormat.parse(numberStr);
            return number.doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0.0; // 解析失败时返回默认值
        }
    }
}

