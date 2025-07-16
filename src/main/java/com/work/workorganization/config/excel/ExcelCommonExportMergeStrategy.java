package com.work.workorganization.config.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
  * -@Desc: 导出Excel通用合并单元格处理器
  * -@Author: zhouzhiqiang
  * -@Date: 2025/7/16 17:03
  *
  * 【说明】
  *          1.支持导出时,支持对值相同的单元格进行合并
  **/
@Data
@Component
@Scope("prototype")//多例
public class ExcelCommonExportMergeStrategy implements CellWriteHandler {

    private final int[] mergeColumnIndex;//需要合并列的列索引

    private final int[] compareColumnIndex;//需要比较的列索引

    private final int mergeRowIndex;//合并行开始的行索引

    private final int lastRowIndex;//最后一行数据的行索引

    private boolean initialized = false;//是否已初始化合并范围

    private final Map<Integer, MergeRange> mergeRanges = new HashMap<>();//合并范围

    //合并范围
    private static class MergeRange {
        int startRow;//合并开始行
        int endRow;//合并结束行

        MergeRange(int startRow, int endRow) {
            this.startRow = startRow;
            this.endRow = endRow;
        }
    }

    @SuppressWarnings("ALL")
    public ExcelCommonExportMergeStrategy(int[] mergeColumnIndex, int[] compareColumnIndex, int mergeRowIndex, int lastRowIndex) {
        this.mergeColumnIndex = mergeColumnIndex;
        this.compareColumnIndex = compareColumnIndex;
        this.mergeRowIndex = mergeRowIndex;
        this.lastRowIndex = lastRowIndex;
    }


    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

        //如果是表头则直接不处理合并
        if (isHead) {
            return;
        }

        //当前行
        int curRowIndex = cell.getRowIndex();
        //当前列
        int curColIndex = cell.getColumnIndex();

        //初始化合并范围
        if (!initialized) {
            for (int col : mergeColumnIndex) {
                mergeRanges.put(col, new MergeRange(curRowIndex, curColIndex));
            }
            initialized = true;
        }

        //如果是第一行数据,初始化合并范围
        if (curRowIndex ==1 && curRowIndex ==mergeRowIndex +1) {
            mergeRanges.put(curColIndex, new MergeRange(curRowIndex, curRowIndex));
            return;
        }

        //如果当前行大于等于合并行,则开始比较 反之则不用比较
        if (curRowIndex <= mergeRowIndex) {
            return;
        }

        //只处理需要合并的列
        if (!isMergeColumn(curColIndex)) {
            return;
        }

        //获取工作簿sheet
        Sheet sheet = writeSheetHolder.getSheet();
        //当前行数据
        Row currentRow = sheet.getRow(curRowIndex);
        //上一行数据
        Row preRow = sheet.getRow(curRowIndex - 1);

        //比较所有需要对比的列的值是否相等
        boolean isAllSame = compareColumnIsAllSame(currentRow, preRow);

        //获取当前单元格的合并范围
        MergeRange mergeRange = mergeRanges.get(curColIndex);

        //如果全部比对的列的值 都相等  则合并所有比对的列的单元格
        if (isAllSame) {
            //扩展当前合并范围
            mergeRange.endRow = curRowIndex;
        } else {
            //提交当前的合并范围
            if (mergeRange.startRow < mergeRange.endRow) {
                sheet.addMergedRegion(new CellRangeAddress(mergeRange.startRow, mergeRange.endRow, curColIndex, curColIndex));
            }
            //开始新的合并范围
            mergeRange.startRow = curRowIndex;
            mergeRange.endRow = curRowIndex;
        }
        mergeRanges.put(curColIndex, mergeRange);

        //是否为最后一个要处理合并的单元格
        boolean isLastMergeCell = isLastMergeCell(curRowIndex, curColIndex);

        //如果是最后一个要处理合并的单元格,则提交所有未提交的合并范围
        if (isLastMergeCell) {
            for (Map.Entry<Integer, MergeRange> entry : mergeRanges.entrySet()) {
                int colIndex = entry.getKey();
                MergeRange range = entry.getValue();
                if (range.startRow < range.endRow) {
                    sheet.addMergedRegion(new CellRangeAddress(range.startRow, range.endRow, colIndex, colIndex));
                }
            }
        }
    }

    /**
     * 判断是否为最后一个要处理合并的单元格
     * @param rowIndex  单元格行数索引
     * @param colIndex  单元格列数索引
     */
    private boolean isLastMergeCell(int rowIndex, int colIndex) {
        return rowIndex == lastRowIndex && colIndex == compareColumnIndex[compareColumnIndex.length - 1];
    }

    /**
     * 比较所有需要对比的列的值是否相等
     * @param currentRow    当前行数据
     * @param preRow        上一行数据
     */
    private boolean compareColumnIsAllSame(Row currentRow, Row preRow) {
        if (null == preRow && currentRow != null) {
            //如果上一行没数据 且当前行有数据 则对比结果为不相等
            return false;
        }
        if (null == currentRow && preRow != null) {
            //如果上一行有数据 且当前行没有数据 则对比结果为不相等
            return false;
        }
        if (currentRow != null) {
            for (int col : compareColumnIndex) {
                Cell currentCell = currentRow.getCell(col);
                Cell preCell = preRow.getCell(col);
                String currentValue = getCellValue(currentCell);
                String preValue = getCellValue(preCell);
                if (StringUtils.isNotBlank(currentValue) && StringUtils.isNotBlank(preValue)
                        &&!StringUtils.equals(currentValue, preValue)) {
                    //如果当前单元格和上一单元格的值不相等 则对比结果为不相等
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 判断是否需要合并
     * @param curColIndex 当前列索引
     */
    private boolean isMergeColumn(int curColIndex) {
        //遍历所需要合并的列 判断是否需要合并
        for (int col : mergeColumnIndex) {
            if (col == curColIndex) {
                return true;
            }
        }
        return false;
    }
    // 获取单元格的值，根据单元格类型返回相应的值
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case Cell.CELL_TYPE_BLANK:
            default:
                return "";
        }
    }
}
