package com.work.workorganization;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChineseDateConverter {

    // 单个数字对应的汉字
    private static final String[] NUMBER_CN = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    // 月份的汉字表示
    private static final String[] MONTH_CN = {"", "一月", "二月", "三月", "四月", "五月", "六月",
                                              "七月", "八月", "九月", "十月", "十一月", "十二月"};

    // 日的汉字表示
    private static final String[] DAY_CN = {"", "一日", "二日", "三日", "四日", "五日", "六日", "七日", "八日", "九日",
                                            "十日", "十一日", "十二日", "十三日", "十四日", "十五日", "十六日", "十七日", "十八日", "十九日",
                                            "二十日", "二十一日", "二十二日", "二十三日", "二十四日", "二十五日", "二十六日", "二十七日", "二十八日", "二十九日",
                                            "三十日", "三十一日"};

    /**
     * 将日期转换为中文汉字格式
     *
     * @param date 日期
     * @return 中文汉字格式的日期字符串
     */
    public static String dateToChineseWords(Date date) {
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");

        String year = numberToChinese(sdfYear.format(date));
        String month = MONTH_CN[Integer.parseInt(sdfMonth.format(date))];
        String day = DAY_CN[Integer.parseInt(sdfDay.format(date))];

        return year + "年" + month + day;
    }

    /**
     * 将数字转换为中文大写
     *
     * @param number 数字字符串
     * @return 中文大写字符串
     */
    private static String numberToChinese(String number) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            int n = c - '0';
            sb.append(NUMBER_CN[n]);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws ParseException {
        List<String> dateList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int year = random.nextInt(2024 - 1900 + 1) + 1900;
            int month = random.nextInt(12) + 1;
            int day = random.nextInt(28) + 1;

            Date date = new Date(year - 1900, month - 1, day);
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
            dateList.add(dateStr);
        }
        for (String dateStr : dateList) {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            String chineseDate = ChineseDateConverter.dateToChineseWords(date);
            System.out.println("日期：【"+dateStr+"】转化为中文：【"+chineseDate+"】"); // 输出当前日期的中文汉字表示
        }
    }
}
