package com.work.workorganization;

import java.util.ArrayList;
import java.util.List;

public class NameExtractor {
    public static void main(String[] args) {
        String str = "张三转办李四";
        List<String> nameList = extractNames(str);
        System.out.println(nameList);
    }

    public static List<String> extractNames(String str) {
        List<String> nameList = new ArrayList<>();
        String[] nameArray = str.split("转办");
        for (String name : nameArray) {
            nameList.add(name.trim());
        }
        return nameList;
    }
}