package com.work.workorganization;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ca {
    public static void main(String[] args) {
        // 假设调用接口返回的数据
        List<Map<String, Object>> dataList = getDynamicData();
// 计算季度的数据
        Map<Integer, List<Map<String, Object>>> quarterlyData = new HashMap<>();
        for (int i = 0; i < dataList.size(); i += 3) {
            int quarter = i / 3 + 1;

            // 判断是否有足够的月份数据可供计算
            if (i + 3 <= dataList.size()) {
                List<Map<String, Object>> monthsToCalculate = dataList.subList(i, i + 3);

                List<Map<String, Object>> quarterData = new ArrayList<>();
                for (Map<String, Object> monthData : monthsToCalculate) {

                        quarterData.add(monthData);

                }

                if (quarterData.size() >= 3) {
                    quarterlyData.put(quarter, quarterData);
                }
            }
        }



        System.out.println(quarterlyData);
        // 假设dataMap是你的数据结构，包含4个季度的数据
        Map<Integer, List<Map<String, Object>>> dataMap = quarterlyData;

        Map<String, Object> resultQuarterData = new HashMap<>();
        Map<Integer, Map<String, Object>> resultQuarterDataMap = new HashMap<>();
        dataMap.forEach((quarter, quarterDataList) -> {
            double averageRentedArea = quarterDataList.stream()
                    .mapToDouble(data -> (double) data.get("rentedArea"))
                    .average()
                    .orElse(0.0);
            resultQuarterData.put("quarterAverageRentedArea", averageRentedArea);

            double averageAllAbleLeaseAbleArea = quarterDataList.stream()
                    .mapToDouble(data -> (double) data.get("allAbleLeaseAbleArea"))
                    .average()
                    .orElse(0.0);
            resultQuarterData.put("quarterAverageAllAbleLeaseAbleArea", averageAllAbleLeaseAbleArea);


            //季度所有已租面积
            double totalRentedArea = quarterDataList.stream()
                    .mapToDouble(data -> (double) data.get("rentedArea"))
                    .sum();
            //季度所有可租面积
            double totalAllAbleLeaseAbleArea = quarterDataList.stream()
                    .mapToDouble(data -> (double) data.get("allAbleLeaseAbleArea"))
                    .sum();

            double quarterlyLeaseRate = totalRentedArea / totalAllAbleLeaseAbleArea *100;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String quarterlyLeaseRateResult = decimalFormat.format(quarterlyLeaseRate);
            resultQuarterData.put("quarterAverageRentedRate", quarterlyLeaseRateResult);


            List<Map<String, Object>> quarterNewContracts = quarterDataList.stream()
                    .flatMap(data -> ((List<Map<String, Object>>) data.get("countMonthNewAddContractInfoList")).stream())
                    .collect(Collectors.toList());
            resultQuarterData.put("quarterNewContracts", quarterNewContracts);


            List<Map<String, Object>> quarterAllEffectiveContracts = quarterDataList.stream()
                    .flatMap(data -> ((List<Map<String, Object>>) data.get("countedContractInfoList")).stream())
                    .collect(Collectors.toList());
            resultQuarterData.put("quarterAllEffectiveContracts", quarterAllEffectiveContracts);

            resultQuarterDataMap.put(quarter, resultQuarterData);

            // 打印结果或进行其他操作
            System.out.println("Quarter: " + quarter);
            System.out.println("Average Rented Area: " + averageRentedArea);
            System.out.println("Average All Able Lease Able Area: " + averageAllAbleLeaseAbleArea);
            System.out.println("Quarter New Contracts: " + quarterNewContracts);
            System.out.println("Quarter ALL Effective Contracts: " + quarterAllEffectiveContracts);
            System.out.println("averageRentedRate = " + quarterlyLeaseRateResult);
        });

        System.out.println("resultQuarterDataMap = " + resultQuarterDataMap);
    }

    // 模拟调用接口返回动态数据
    private static List<Map<String, Object>> getDynamicData() {
        // 假设接口返回的数据
        List<Map<String, Object>> data = new ArrayList<>();

        // 添加示例数据（可以替换为实际调用接口返回的数据）
        for (int i = 0; i < 12; i++) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("rentedRated", "0.95");
            monthData.put("month", i + 1);
            monthData.put("rentedArea", 1000.00);
            monthData.put("allAbleLeaseAbleArea", 1200.00);
            monthData.put("countMonthNewAddContractInfoList", Arrays.asList(i+1));
            monthData.put("countedContractInfoList", Arrays.asList(i+1));
            data.add(monthData);
        }

        return data;
    }
}