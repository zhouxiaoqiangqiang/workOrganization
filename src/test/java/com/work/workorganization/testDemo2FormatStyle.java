package com.work.workorganization;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class testDemo2FormatStyle {
    public static void main(String[] args) {
        DateTimeFormatter formatter = null;
//        formatter= DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
//        String format1 = formatter.format(LocalDateTime.now());
//        System.out.println("format1 = " + format1);
        formatter= DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
        String format2 = formatter.format(ZonedDateTime.now());
        System.out.println("format2 = " + format2);
        formatter= DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
//
//        String format3 = formatter.format(LocalDateTime.now());
//        System.out.println("format3 = " + format3);
//        formatter= DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
//        String format4 = formatter.format(LocalDateTime.now());
//        System.out.println("format4 = " + format4);
    }
}
