package com.work.workorganization.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.work.workorganization.config.excel.ExcelCommonExportMergeStrategy;
import com.work.workorganization.config.excel.ExcelCommonExportWriterHandler;
import com.work.workorganization.config.excel.ExcelCommonExportCellWriterHandler;
import com.work.workorganization.pojo.UserInfo;
import com.work.workorganization.service.EasyExcelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

/**
 * -@Desc:    EasyExcel导出 服务接口实现类
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/16 15:16
 **/

@Service
@Slf4j
public class EasyExcelServiceImpl implements EasyExcelService {

    /**
     * 导出用户信息Excel
     */
    @Override
    public void exportUserInfoExcel(HttpServletResponse httpServletResponse) {
        try {
            List<UserInfo> userInfoList = createUserInfoList();
            String fileName = "用户信息表.xlsx";
            httpServletResponse.setContentType("application/vnd.ms-excel");
            fileName = URLEncoder.encode(fileName, "utf-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            EasyExcel.write(httpServletResponse.getOutputStream(), UserInfo.class)
                    .sheet("用户信息表")
                    .registerWriteHandler(getDefaultHorizontalCellStyleStrategy())
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .doWrite(userInfoList);
            log.info("导出用户信息Excel成功!");
        } catch (Exception e) {
            log.error("导出用户信息Excel失败!当前时间:【{}】,当前方法名:【{}】,失败原因:【{}】", LocalDateTime.now(), Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage());
            e.printStackTrace();
        }

    }


    /**
     * 导出用户信息Excel 【合并单元格】
     */
    @Override
    public void exportUserInfoExcelMerge(HttpServletResponse httpServletResponse) {
        try {
            //需要合并的列
            int[] mergeColumnIndex = {0, 1, 2, 3, 4, 6};
            //需要对比合并比较数据的列
            int[] compareColumnIndex = {0, 1, 2, 3, 4, 6};
            List<UserInfo> userInfoList = createUserInfoList();
            String fileName = "用户信息表.xlsx";
            httpServletResponse.setContentType("application/vnd.ms-excel");
            fileName = URLEncoder.encode(fileName, "utf-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            EasyExcel.write(httpServletResponse.getOutputStream(), UserInfo.class)
                    .sheet("用户信息表")
                    .registerWriteHandler(getDefaultHorizontalCellStyleStrategy())
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerWriteHandler(new ExcelCommonExportMergeStrategy(mergeColumnIndex, compareColumnIndex, 1, userInfoList.size()))
                    .doWrite(userInfoList);
            log.info("导出用户信息Excel成功!");
        } catch (Exception e) {
            log.error("导出用户信息Excel失败!当前时间:【{}】,当前方法名:【{}】,失败原因:【{}】", LocalDateTime.now(), Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 下载导入用户模板Excel
     */
    @Override
    public void downloadUserInfoTemplate(HttpServletResponse httpServletResponse) {
        try {
            //下拉框数据
            Map<Integer, String[]> dropDownDataMap = new HashMap<>();
            //性别下拉框数据
            String[] sexDropDataArr = {"男", "女"};
            dropDownDataMap.put(2, sexDropDataArr);
            //部门下拉框数据
            String[] departmentDropDataArr = {"开发部", "调研部", "人事部", "财务部"};
            dropDownDataMap.put(4, departmentDropDataArr);

            //列名批注信息
            Map<Integer, String> remarkDataMap = new HashMap<>();
            remarkDataMap.put(2, "请在下拉框中选择性别");
            remarkDataMap.put(4, "请在下拉框中选择部门");

            //设置导出文件名称
            String fileName = "用户信息表.xlsx";
            httpServletResponse.setContentType("application/vnd.ms-excel");
            fileName = URLEncoder.encode(fileName, "utf-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            EasyExcel.write(httpServletResponse.getOutputStream(), UserInfo.class)
                    .sheet("用户信息表")
                    .registerWriteHandler(getDefaultHorizontalCellStyleStrategy())
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerWriteHandler(new ExcelCommonExportWriterHandler(Collections.singletonList(6), Collections.singletonList(3), Arrays.asList(0, 1, 2, 4, 5), 1, dropDownDataMap))
                    .registerWriteHandler(new ExcelCommonExportCellWriterHandler(remarkDataMap))
                    .doWrite(new ArrayList<>());

            log.info("导出用户信息Excel成功!");
        } catch (Exception e) {
            log.error("导出用户信息Excel失败!当前时间:【{}】,当前方法名:【{}】,失败原因:【{}】", LocalDateTime.now(), Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 模拟用户信息
     */
    public List<UserInfo> createUserInfoList() {
        List<UserInfo> userInfoList = new ArrayList<>();

        UserInfo user1 = UserInfo.builder().userCode("A001").userName("张三").sex("男")
                .age(19).department("开发部").interest("唱歌").dataDate("2025-07-16").build();

        UserInfo user2 = UserInfo.builder().userCode("A001").userName("张三").sex("男")
                .age(19).department("开发部").interest("跳舞").dataDate("2025-07-16").build();

        UserInfo user3 = UserInfo.builder().userCode("B002").userName("李四").sex("男")
                .age(21).department("调研部").interest("学习").dataDate("2025-07-16").build();

        UserInfo user4 = UserInfo.builder().userCode("C003").userName("王五").sex("女")
                .age(24).department("人事部").interest("弹琴").dataDate("2025-07-16").build();

        UserInfo user5 = UserInfo.builder().userCode("B002").userName("李四").sex("男")
                .age(21).department("调研部").interest("画画").dataDate("2025-07-16").build();

        userInfoList.add(user1);
        userInfoList.add(user2);
        userInfoList.add(user3);
        userInfoList.add(user4);
        userInfoList.add(user5);
        return userInfoList;
    }

    /**
     * 默认表格样式
     */
    public HorizontalCellStyleStrategy getDefaultHorizontalCellStyleStrategy() {

        //表头样式
        WriteCellStyle headCellStyle = new WriteCellStyle();
        //自动换行
        headCellStyle.setWrapped(true);
        //水平对齐方式 居中
        headCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //垂直对齐方式 居中
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //内容样式
        WriteCellStyle contentCellStyle = new WriteCellStyle();
        //自动换行
        contentCellStyle.setWrapped(true);
        //水平对齐方式 居中
        contentCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //垂直对齐方式 居中
        contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //内容边框线条
        contentCellStyle.setBorderLeft(BorderStyle.THIN);
        contentCellStyle.setBorderRight(BorderStyle.THIN);
        contentCellStyle.setBorderTop(BorderStyle.THIN);
        contentCellStyle.setBorderBottom(BorderStyle.THIN);
        return new HorizontalCellStyleStrategy(headCellStyle, contentCellStyle);
    }

}
