package com.work.workorganization;

import com.work.workorganization.utils.PdfExportUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * -@Des   生成PDF测试
 * -@Author zhouzhiqiang
 * -@Date 2025/12/2 15:14
 **/

public class HtmlToPdfExampleTest {

    public static void main(String[] args) {
        try {
            System.out.println("开始生成PDF...");

            // 示例1：简单的HTML转PDF
            String simpleHtml = "<h3 style=\"text-align: center\">关于备案国君资管1739定向资产管理合同等终止的报告</h3>     <div style=\"margin-top: 15vh;line-height: 40px;\">         <p>中国证券投资基金业协会:</p>                  <p>国君资管1739定向资产管理合同、国泰海通私客尊享FOF7135号单一资产管理计划、国泰君安私客尊享FOF3021号单一资产管理计划、国泰君安私客尊享FOF5158号单一资产管理计划、国泰君安私客尊享FOF5332号单一资产管理计划、国泰君安私客尊享FOF5619号单一资产管理计划，符合法律法规规则、合同约定，现已符合终止情形，具体明细如下:</p>         <p style=\"text-indent: 2em\">  国君资管1739定向资产管理合同、终止日期:2025-11-26</p><p style=\"text-indent: 2em\">  国泰海通私客尊享FOF7135号单一资产管理计划、终止日期:2025-11-24</p><p style=\"text-indent: 2em\">  国泰君安私客尊享FOF5619号单一资产管理计划、终止日期:2025-11-24</p><p style=\"text-indent: 2em\">  国泰君安私客尊享FOF5332号单一资产管理计划、终止日期:2025-11-24</p><p style=\"text-indent: 2em\">  国泰君安私客尊享FOF3021号单一资产管理计划、终止日期:2025-11-25</p><p style=\"text-indent: 2em\">  国泰君安私客尊享FOF5158号单一资产管理计划、终止日期:2025-11-25</p> <br />         <p style=\"text-indent: 2em\">特此做终止备案!</p>     </div>          <br />     <p style=\"text-align: right;padding-left: 24px;\">日期：2025-12-02</p>";

            PdfExportUtil.htmlToPdfFile(simpleHtml, "simple_document.pdf");
            System.out.println("简单PDF已生成: simple_document.pdf");

//            // 示例2：带配置的HTML转PDF
//            Map<String, Object> config = new HashMap<>();
//            config.put("title", "测试报告");
//            config.put("author", "张三");
//            config.put("marginLeft", 50f);
//            config.put("marginRight", 50f);
//            config.put("marginTop", 80f);  // 给页眉留空间
//            config.put("marginBottom", 80f); // 给页脚留空间
//
//            String styledHtml = "<html>" +
//                               "<head>" +
//                               "<style>" +
//                               "h1 { color: #2c3e50; text-align: center; }" +
//                               "p { line-height: 1.6; margin: 10px 0; }" +
//                               ".important { color: #e74c3c; font-weight: bold; }" +
//                               "</style>" +
//                               "</head>" +
//                               "<body>" +
//                               "<h1>年度报告</h1>" +
//                               "<p>这是一份重要的年度报告。</p>" +
//                               "<p class='important'>重要内容：请注意查看！</p>" +
//                               "<p>详细内容...</p>" +
//                               "</body>" +
//                               "</html>";
//
//            PdfExportUtil.htmlToPdfFile(styledHtml, "styled_report.pdf", config);
//            System.out.println("带样式PDF已生成: styled_report.pdf");
//
//            // 示例3：生成带表格的PDF
//            Map<String, Object> tableData = new HashMap<>();
//            tableData.put("订单号", "ORD20231215001");
//            tableData.put("客户名称", "张三");
//            tableData.put("订单金额", "¥1,250.00");
//            tableData.put("下单时间", "2023-12-15 14:30:00");
//            tableData.put("订单状态", "已发货");
//            tableData.put("收货地址", "北京市朝阳区");
//            tableData.put("联系电话", "13800138000");
//
//            String tableHtml = PdfExportUtil.generateTableHtml("订单详情", tableData);
//            PdfExportUtil.htmlToPdfFile(tableHtml, "order_report.pdf");
//            System.out.println("订单报告PDF已生成: order_report.pdf");
//
//            // 示例4：使用传统元素构建PDF
//            Map<String, Object> data = new HashMap<>();
//            data.put("姓名", "李四");
//            data.put("年龄", "28");
//            data.put("职位", "软件工程师");
//            data.put("部门", "技术部");
//            data.put("入职日期", "2022-03-15");
//
//            byte[] pdfBytes = PdfExportUtil.createPdfWithElements(data);
//            // 保存到文件
//            java.nio.file.Files.write(
//                new File("employee_info.pdf").toPath(),
//                pdfBytes
//            );
//            System.out.println("员工信息PDF已生成: employee_info.pdf");
//
//            // 示例5：添加水印
//            byte[] originalPdf = PdfExportUtil.htmlToPdfBytes(simpleHtml);
//            byte[] watermarkedPdf = PdfExportUtil.addWatermark(originalPdf, "内部使用");
//
//            java.nio.file.Files.write(
//                new File("watermarked.pdf").toPath(),
//                watermarkedPdf
//            );
//            System.out.println("带水印PDF已生成: watermarked.pdf");
//
//            System.out.println("所有PDF文件生成完成！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}