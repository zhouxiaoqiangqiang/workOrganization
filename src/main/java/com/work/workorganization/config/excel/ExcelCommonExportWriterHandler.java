package com.work.workorganization.config.excel;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * -@Desc:   导出Excel通用处理器
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/16 13:32
 *
 * 【说明】
 *          1.支持导出时,设置单元格格式(日期、数值、文本)
 *          2.支持导出时,设置下拉框
 **/
@Data
@Component
@NoArgsConstructor
public class ExcelCommonExportWriterHandler implements SheetWriteHandler {

    private List<Integer> dateColumnList;//表格中 日期的 列数

    private List<Integer> numberColumnList;//表格中 数值的 列数

    private List<Integer> textColumnList;//表格中 文本的 列数

    private Integer dataStartColumn;//表格中 数据开始列数

    private Map<Integer, String[]> dropDownDataMap;//下拉框数据  key为列数 value为下拉框数据

    public ExcelCommonExportWriterHandler(List<Integer> dateColumnList, List<Integer> numberColumnList,
                                          List<Integer> textColumnList, Integer dataStartColumn,
                                          Map<Integer, String[]> dropDownDataMap) {
        this.dateColumnList = dateColumnList;
        this.numberColumnList = numberColumnList;
        this.textColumnList = textColumnList;
        this.dataStartColumn = dataStartColumn;
        this.dropDownDataMap = dropDownDataMap;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

        //获取工作簿sheet页
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        //创建日期单元格格式样式
        CellStyle dateCellStyle = workbook.createCellStyle();
        short dateFormat = workbook.createDataFormat().getFormat("yyyy/MM/dd");
        dateCellStyle.setDataFormat(dateFormat);

        //配置单元格格式校验
        DataValidationHelper helper = sheet.getDataValidationHelper();

        //日期类型格式 校验
        for (Integer dateColumn : dateColumnList) {

            //设置单元格验证生效范围 (首行、末行、起始列、结束列)
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(dataStartColumn, 65536, dateColumn, dateColumn);

            //设置日期格式校验方式
            /* Date(1990,1,1)时为Excel日期函数,能成功解析到 如写成Date(1990,01,01)则解析不到 */
            DataValidationConstraint dateConstraint = helper.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN, "Date(1990,1,1)", "Date(2099,12,31)", "yyyy/MM/dd");
            //创建验证对象
            DataValidation dateValidation = helper.createValidation(dateConstraint, cellRangeAddressList);
            //设置验证错误信息
            dateValidation.createErrorBox("日期格式错误", "请输入正确的日期格式:[yyyy/MM/dd],范围:[1990/01/01到2099/12/31]");
            //开启错误提示
            dateValidation.setShowErrorBox(true);
            //工作簿绑定验证对象
            sheet.addValidationData(dateValidation);
            //设置日期默认单元格格式
            sheet.setDefaultColumnStyle(dateColumn, dateCellStyle);
        }
        //对日期的列 设置日期单元格格式
        for (Row row : sheet) {
            for (Integer dateColumn : dateColumnList) {
                Cell dateCell = row.createCell(dateColumn);
                dateCell.setCellStyle(dateCellStyle);
            }
        }

        //数值类型 校验
        for (Integer numberColumn : numberColumnList) {
            //数值校验
            DataValidationConstraint numberConstraint = helper.createNumericConstraint(DataValidationConstraint.ValidationType.DECIMAL, DataValidationConstraint.OperatorType.BETWEEN, "-1e100", "1e100");
            //设置单元格验证生效范围 (首行、末行、起始列、结束列)
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(dataStartColumn, 65536, numberColumn, numberColumn);
            //创建验证对象
            DataValidation numberValidation = helper.createValidation(numberConstraint, cellRangeAddressList);
            //设置验证错误信息
            numberValidation.createErrorBox("数值格式错误", "请输入正确的数值格式");
            //开启错误提示
            numberValidation.setShowErrorBox(true);
            //工作簿绑定验证对象
            sheet.addValidationData(numberValidation);
        }

        //创建文本单元格格式样式
        CellStyle textCellStyle = workbook.createCellStyle();
        textCellStyle.setDataFormat((short) 49);
        SXSSFSheet sxssfSheet = (SXSSFSheet) sheet;
        //文本格式 设置
        for (Integer textColumn : textColumnList) {
            //设置为文本格式
            sxssfSheet.setDefaultColumnStyle(textColumn, textCellStyle);
        }

        //处理下拉框数据
        if (CollectionUtil.isNotEmpty(dropDownDataMap)) {
            for (Map.Entry<Integer, String[]> entry : dropDownDataMap.entrySet()) {
                //列索引
                Integer columnIndex = entry.getKey();
                //下拉框数据
                String[] dropDownData = entry.getValue();
                //设置单元格验证生效范围 (首行、末行、起始列、结束列)
                CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(dataStartColumn, 65536, columnIndex, columnIndex);
                //设置下拉框数据
                DataValidationConstraint constraint = helper.createExplicitListConstraint(dropDownData);
                //创建验证对象
                DataValidation validation = helper.createValidation(constraint, cellRangeAddressList);

                //组织输入非下拉框选项信息
                //设置验证错误
                validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
                //开启错误提示
                validation.setShowErrorBox(true);
                //设置错误信息
                validation.createErrorBox("下拉框选项错误", "请选择下拉框中的选项");
                //设置填写说明
                validation.createPromptBox("填写说明", "请选择下拉框中的选项,无法输入下拉框外的选项");
                //工作簿绑定验证对象
                sheet.addValidationData(validation);
            }
        }
    }

}
