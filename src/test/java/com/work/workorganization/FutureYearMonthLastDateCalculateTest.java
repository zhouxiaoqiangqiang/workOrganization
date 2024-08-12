package com.work.workorganization;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
/**
  * -@Desc: 计算未来12个月的最后一天
  * -@Author: zhouzhiqiang
  * -@Date: 2024/8/12 10:21
 **/
public class FutureYearMonthLastDateCalculateTest {
    public static void main(String[] args) {
        int queryYear = 2023; // 替换为你需要的年份
        int queryMonth = 2;   // 替换为你需要的月份

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (int i = 1; i <= 12; i++) {
            // 计算当前循环的年份和月份
            YearMonth yearMonth = YearMonth.of(queryYear, queryMonth).plusMonths(i);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            // 打印结果
            System.out.println("Month: " + yearMonth.format(formatter) +
                    ", Last Day: " + lastDayOfMonth);
        }
    }
}