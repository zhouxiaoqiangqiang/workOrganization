package com.work.workorganization;

import com.work.workorganization.utils.EmailUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * -@Desc:    测试发送邮件
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/4 17:44
 **/
public class SendEmailTest {
    public static void main(String[] args) {

        //html表格
        LinkedHashMap<String, String> tableColumnInfoMap = createTableColumnInfoMap();
        List<Map<String, String>> tableDataList = createTableDataList();
        String htmlTableContent = EmailUtils.generateMailHtmlTableContent(tableColumnInfoMap, tableDataList);
        System.out.println(htmlTableContent);

        //日期
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        //发送邮件
        try {
            EmailUtils.asyncSendMail(
                    Collections.singletonList("1525761613@qq.com"),
                    new Date(),
                    today+"测试邮件标题",//邮件主题标题
                    htmlTableContent,//邮件正文内容(支持html格式)
                    new ArrayList<>(),//附件列表
                    new ArrayList<>(),//抄送人邮箱列表
                    "高飞飞",//代发人姓名
                    "2531251725@qq.com"//代发人邮箱
            );

        } catch (Exception e) {
            System.out.println("发送邮件失败!");
            e.printStackTrace();
        }
    }

    /**
     * 生成表格 模拟列表字段信息
     */
    private static LinkedHashMap<String, String> createTableColumnInfoMap() {
        LinkedHashMap<String, String> tableColumnInfoMap = new LinkedHashMap<>();
        tableColumnInfoMap.put("tableName", "测试表名");
        tableColumnInfoMap.put("userName", "用户姓名");
        tableColumnInfoMap.put("userCode", "工号");
        tableColumnInfoMap.put("age", "年龄");
        tableColumnInfoMap.put("sex", "性别");
        tableColumnInfoMap.put("phone", "手机号");
        return tableColumnInfoMap;
    }

    /**
     * 生成表格 模拟数据
     */
    public static List<Map<String, String>> createTableDataList() {
        List<Map<String, String>> tableDataList = new ArrayList<>();
        Map<String, String> dataMap01 = new HashMap<>();
        dataMap01.put("userName", "张三");
        dataMap01.put("userCode", "A01");
        dataMap01.put("age", "14");
        dataMap01.put("sex", "男");
        dataMap01.put("phone", "13111111111");
        tableDataList.add(dataMap01);

        Map<String, String> dataMap02 = new HashMap<>();
        dataMap02.put("userName", "李四");
        dataMap02.put("userCode", "B02");
        dataMap02.put("age", "18");
        dataMap02.put("sex", "男");
        dataMap02.put("phone", "13222222222");
        tableDataList.add(dataMap02);

        Map<String, String> dataMap03 = new HashMap<>();
        dataMap03.put("userName", "王五");
        dataMap03.put("userCode", "C03");
        dataMap03.put("age", "21");
        dataMap03.put("sex", "女");
        dataMap03.put("phone", "13333333333");
        tableDataList.add(dataMap03);

        Map<String, String> dataMap04 = new HashMap<>();
        dataMap04.put("userName", "赵六");
        dataMap04.put("userCode", "D04");
        dataMap04.put("age", "30");
        dataMap04.put("sex", "男");
        dataMap04.put("phone", "13444444444");
        tableDataList.add(dataMap04);

        return tableDataList;
    }
}
