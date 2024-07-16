package com.work.workorganization;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CalculateQuarterlyData {
    public static void main(String[] args) {
        // 假设调用接口返回的数据
        Map<Integer, List<User>> data = getDynamicData();

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        // 构建月份范围
        List<Integer> allMonths = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        // 计算季度的数据
        Map<Integer, List<User>> quarterlyData = new HashMap<>();
        for (int i = 0; i < allMonths.size(); i += 3) {
            int quarter = i / 3 + 1;
            List<Integer> monthsToCalculate = allMonths.subList(i, i + 3);

            List<User> quarterData = monthsToCalculate.stream()
                    .flatMap(month -> data.getOrDefault(month, Collections.emptyList()).stream())
                    .collect(Collectors.toList());

            if (quarterData.size() >= 3) {
                quarterlyData.put(quarter, quarterData);
            }
        }

        System.out.println(quarterlyData);
    }

    // 模拟调用接口返回动态数据
    private static Map<Integer, List<User>> getDynamicData() {
        // 假设接口返回的数据
        Map<Integer, List<User>> data = new HashMap<>();

        // 添加示例数据（可以替换为实际调用接口返回的数据）
        List<User> januaryData = new ArrayList<>();
        januaryData.add(new User("John", 1));
        data.put(1, januaryData);

        List<User> februaryData = new ArrayList<>();
        februaryData.add(new User("Alice", 2));
        data.put(2, februaryData);

        List<User> marchData = new ArrayList<>();
        marchData.add(new User("Bob", 3));
        data.put(3, marchData);

        List<User> aprilData = new ArrayList<>();
        aprilData.add(new User("Tom", 4));
        data.put(4, aprilData);

        List<User> mayData = new ArrayList<>();
        mayData.add(new User("Kate", 5));
        data.put(5, mayData);

        List<User> juneData = new ArrayList<>();
        juneData.add(new User("Sam", 6));
        data.put(6, juneData);

        List<User> julyData = new ArrayList<>();
        julyData.add(new User("Emma", 7));
        data.put(7, julyData);

        return data;
    }

    // 假设 User 类有 name 和 month 两个属性
    static class User {
        private String name;
        private int month;

        public User(String name, int month) {
            this.name = name;
            this.month = month;
        }

        public String getName() {
            return name;
        }

        public int getMonth() {
            return month;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", month=" + month +
                    '}';
        }
    }
}