package com.work.workorganization.config.excel;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * -@Desc:  导出Excel通用列名批注处理器
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/16 17:01
 *
 * 【说明】
 *          1.支持导出时,支持指定列设置批注信息
 **/
@Data
@Component
@NoArgsConstructor
public class ExcelCommonExportCellWriterHandler implements CellWriteHandler {
    private Map<Integer, String> remarkDataMap;

    public ExcelCommonExportCellWriterHandler(Map<Integer, String> remarkDataMap) {
        this.remarkDataMap = remarkDataMap;
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

        if (isHead) {
            if (CollectionUtil.isNotEmpty(remarkDataMap)) {
                //获取工作簿sheet
                Sheet sheet = writeSheetHolder.getSheet();
                //获取工作簿
                Workbook workbook = sheet.getWorkbook();
                //获取批注信息
                Drawing<?> drawingPatriarch = sheet.createDrawingPatriarch();
                //获取批注对象
                CreationHelper creationHelper = workbook.getCreationHelper();

                //列对应的索引
                int columnIndex = cell.getColumnIndex();

                //单元格列数对应的批注信息
                String remark = remarkDataMap.getOrDefault(columnIndex, null);

                //如果单元格列数刚好存在批注信息,则设置批注
                if (StringUtils.isNotBlank(remark)) {
                    //创建批注
                    ClientAnchor clientAnchor = creationHelper.createClientAnchor();
                    //设置批注列
                    clientAnchor.setCol1(columnIndex);
                    //设置批注行数
                    clientAnchor.setRow1(cell.getRowIndex());
                    //创建批注信息
                    Comment cellComment = drawingPatriarch.createCellComment(clientAnchor);
                    cellComment.setString(creationHelper.createRichTextString(remark));
                    cell.setCellComment(cellComment);
                }


            }

        }
    }
}
