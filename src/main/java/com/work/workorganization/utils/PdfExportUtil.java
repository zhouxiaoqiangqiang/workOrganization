package com.work.workorganization.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * iText 5 (开源版) PDF生成工具类
 * 支持HTML转PDF，使用LGPL/MPL许可证
 */
public class PdfExportUtil {
    
    // 默认中文字体（使用itext-asian包中的字体）
    private static BaseFont DEFAULT_CHINESE_FONT;
    
    static {
        try {
            // 初始化中文字体
            DEFAULT_CHINESE_FONT = BaseFont.createFont(
                "STSong-Light", 
                "UniGB-UCS2-H", 
                BaseFont.NOT_EMBEDDED
            );
        } catch (Exception e) {
            try {
                // 备选字体方案
                DEFAULT_CHINESE_FONT = BaseFont.createFont();
            } catch (Exception ex) {
                throw new RuntimeException("字体初始化失败", ex);
            }
        }
    }
    
    /**
     * 默认配置
     */
    private static final Map<String, Object> DEFAULT_CONFIG = new HashMap<>();
    
    static {
        DEFAULT_CONFIG.put("pageSize", PageSize.A4);
        DEFAULT_CONFIG.put("marginLeft", 36f);    // 0.5英寸 = 36点
        DEFAULT_CONFIG.put("marginRight", 36f);
        DEFAULT_CONFIG.put("marginTop", 36f);
        DEFAULT_CONFIG.put("marginBottom", 36f);
        DEFAULT_CONFIG.put("author", "PDF Export Tool");
        DEFAULT_CONFIG.put("creator", "OpenSourcePdfExportUtil");
    }
    
    /**
     * 将HTML字符串转换为PDF字节数组
     */
    public static byte[] htmlToPdfBytes(String htmlContent) {
        return htmlToPdfBytes(htmlContent, DEFAULT_CONFIG);
    }
    
    /**
     * 将HTML字符串转换为PDF字节数组（带配置）
     */
    public static byte[] htmlToPdfBytes(String htmlContent, Map<String, Object> config) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            htmlToPdfStream(htmlContent, outputStream, config);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("PDF生成失败", e);
        }
    }
    
    /**
     * 将HTML字符串转换为PDF文件
     */
    public static void htmlToPdfFile(String htmlContent, String filePath) {
        htmlToPdfFile(htmlContent, filePath, DEFAULT_CONFIG);
    }
    
    /**
     * 将HTML字符串转换为PDF文件（带配置）
     */
    public static void htmlToPdfFile(String htmlContent, String filePath, Map<String, Object> config) {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            htmlToPdfStream(htmlContent, outputStream, config);
        } catch (IOException e) {
            throw new RuntimeException("PDF文件生成失败", e);
        }
    }
    
    /**
     * 将HTML字符串写入PDF输出流
     */
    public static void htmlToPdfStream(String htmlContent, OutputStream outputStream, 
                                       Map<String, Object> config) {
        Document document = null;
        PdfWriter writer = null;
        
        try {
            // 创建文档对象
            document = createDocument(config);
            
            // 创建PDF写入器
            writer = PdfWriter.getInstance(document, outputStream);
            
            // 设置文档属性
            setDocumentProperties(document, config);
            
            // 打开文档
            document.open();
            
            // 设置中文字体
            Font chineseFont = new Font(DEFAULT_CHINESE_FONT, 12, Font.NORMAL);
            
            // 使用XML Worker转换HTML
            convertHtmlToPdf(document, writer, htmlContent, chineseFont, config);
            
        } catch (Exception e) {
            throw new RuntimeException("PDF转换失败", e);
        } finally {
            if (document != null) {
                document.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }
    
    /**
     * 创建文档对象
     */
    private static Document createDocument(Map<String, Object> config) {
        Rectangle pageSize = (Rectangle) config.getOrDefault("pageSize", PageSize.A4);
        Float marginLeft = (Float) config.getOrDefault("marginLeft", 36f);
        Float marginRight = (Float) config.getOrDefault("marginRight", 36f);
        Float marginTop = (Float) config.getOrDefault("marginTop", 36f);
        Float marginBottom = (Float) config.getOrDefault("marginBottom", 36f);
        
        return new Document(pageSize, marginLeft, marginRight, marginTop, marginBottom);
    }
    
    /**
     * 设置文档属性
     */
    private static void setDocumentProperties(Document document, Map<String, Object> config) {
        String title = (String) config.getOrDefault("title", "Generated PDF");
        String author = (String) config.getOrDefault("author", "PDF Export Tool");
        String creator = (String) config.getOrDefault("creator", "OpenSourcePdfExportUtil");
        
        document.addTitle(title);
        document.addAuthor(author);
        document.addCreator(creator);
        document.addCreationDate();
    }
    
    /**
     * 使用XML Worker转换HTML为PDF
     */
    private static void convertHtmlToPdf(Document document, PdfWriter writer, 
                                        String htmlContent, Font defaultFont, 
                                        Map<String, Object> config) throws Exception {
        
        // 创建CSS解析器
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        
        // 加载CSS文件（如果有）
        String css = (String) config.get("css");
        if (css != null && !css.isEmpty()) {
            CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(css.getBytes()));
            cssResolver.addCss(cssFile);
        }
        
        // 创建HTML管道上下文
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        
        // 设置CSS应用器
        CssAppliers cssAppliers = new CssAppliersImpl(new ChineseFontProvider());
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.setCssAppliers(cssAppliers);
        
        // 创建管道
        PdfWriterPipeline pdfPipeline = new PdfWriterPipeline(document, writer);
        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdfPipeline);
        CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        
        // 创建XML Worker
        XMLWorker worker = new XMLWorker(cssPipeline, true);
        XMLParser parser = new XMLParser(worker);
        
        // 解析HTML内容
        try (InputStream htmlStream = new ByteArrayInputStream(
                htmlContent.getBytes(StandardCharsets.UTF_8))) {
            parser.parse(htmlStream, StandardCharsets.UTF_8);
        }
    }
    
    /**
     * 自定义字体提供器，支持中文
     */
    public static class ChineseFontProvider extends FontFactoryImp {
        @Override
        public Font getFont(String fontname, String encoding, boolean embedded, 
                           float size, int style, BaseColor color) {
            
            try {
                // 使用中文字体
                BaseFont baseFont = DEFAULT_CHINESE_FONT;
                
                // 根据样式设置
                int fontStyle = Font.NORMAL;
                if (style == Font.BOLD) {
                    fontStyle = Font.BOLD;
                } else if (style == Font.ITALIC) {
                    fontStyle = Font.ITALIC;
                } else if (style == (Font.BOLD | Font.ITALIC)) {
                    fontStyle = Font.BOLDITALIC;
                }
                
                return new Font(baseFont, size, fontStyle, color);
                
            } catch (Exception e) {
                // 失败时返回默认字体
                return super.getFont(fontname, encoding, embedded, size, style, color);
            }
        }
    }
    
    /**
     * 使用传统的iText元素构建PDF（更精确的控制）
     */
    public static byte[] createPdfWithElements(Map<String, Object> data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            
            document.open();
            
            // 设置中文字体
            Font titleFont = new Font(DEFAULT_CHINESE_FONT, 16, Font.BOLD);
            Font normalFont = new Font(DEFAULT_CHINESE_FONT, 12, Font.NORMAL);
            Font boldFont = new Font(DEFAULT_CHINESE_FONT, 12, Font.BOLD);
            
            // 添加标题
            Paragraph title = new Paragraph("PDF 报告", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // 添加内容
            if (data != null && !data.isEmpty()) {
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    Paragraph p = new Paragraph();
                    p.setFont(boldFont);
                    p.add(entry.getKey() + ": ");
                    p.setFont(normalFont);
                    p.add(String.valueOf(entry.getValue()));
                    document.add(p);
                }
            }
            
            document.close();
            writer.close();
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("创建PDF失败", e);
        }
    }
    
    /**
     * 合并多个PDF
     */
    public static byte[] mergePdfs(byte[]... pdfBytes) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, outputStream);
            
            document.open();
            
            for (byte[] pdfByte : pdfBytes) {
                PdfReader reader = new PdfReader(pdfByte);
                int pages = reader.getNumberOfPages();
                
                for (int i = 1; i <= pages; i++) {
                    PdfImportedPage page = copy.getImportedPage(reader, i);
                    copy.addPage(page);
                }
                
                reader.close();
            }
            
            document.close();
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("合并PDF失败", e);
        }
    }
    
    /**
     * 为PDF添加水印
     */
    public static byte[] addWatermark(byte[] pdfBytes, String watermarkText) {
        try {
            PdfReader reader = new PdfReader(pdfBytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, outputStream);
            
            // 获取PDF页数
            int pageCount = reader.getNumberOfPages();
            
            // 设置水印字体
            Font watermarkFont = new Font(DEFAULT_CHINESE_FONT, 60, Font.NORMAL, 
                                         new BaseColor(200, 200, 200));
            
            // 为每一页添加水印
            for (int i = 1; i <= pageCount; i++) {
                PdfContentByte content = stamper.getUnderContent(i);
                
                // 创建水印
                ColumnText.showTextAligned(
                    content,
                    Element.ALIGN_CENTER,
                    new Phrase(watermarkText, watermarkFont),
                    297.5f, 420, 45  // 位置和角度
                );
            }
            
            stamper.close();
            reader.close();
            
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("添加水印失败", e);
        }
    }
    
    /**
     * 从PDF提取文本
     */
    public static String extractTextFromPdf(byte[] pdfBytes) {
        StringBuilder text = new StringBuilder();
        
        try {
            PdfReader reader = new PdfReader(pdfBytes);
            int pages = reader.getNumberOfPages();
            
            for (int i = 1; i <= pages; i++) {
                text.append(PdfTextExtractor.getTextFromPage(reader, i));
                text.append("\n");
            }
            
            reader.close();
            return text.toString();
            
        } catch (Exception e) {
            throw new RuntimeException("提取PDF文本失败", e);
        }
    }
    
    /**
     * 生成包含表格的HTML模板
     */
    public static String generateTableHtml(String title, Map<String, Object> data) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>")
            .append("<html>")
            .append("<head>")
            .append("<meta charset=\"UTF-8\">")
            .append("<style>")
            .append("body { font-family: SimSun; margin: 20px; }")
            .append("h1 { text-align: center; color: #333; }")
            .append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }")
            .append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }")
            .append("th { background-color: #f2f2f2; font-weight: bold; }")
            .append("tr:nth-child(even) { background-color: #f9f9f9; }")
            .append(".footer { margin-top: 30px; text-align: center; color: #666; font-size: 12px; }")
            .append("</style>")
            .append("</head>")
            .append("<body>");
        
        html.append("<h1>").append(title).append("</h1>");
        
        if (data != null && !data.isEmpty()) {
            html.append("<table>")
                .append("<thead><tr><th>字段</th><th>值</th></tr></thead>")
                .append("<tbody>");
            
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                html.append("<tr>")
                    .append("<td>").append(entry.getKey()).append("</td>")
                    .append("<td>").append(entry.getValue()).append("</td>")
                    .append("</tr>");
            }
            
            html.append("</tbody></table>");
        }
        
        html.append("<div class=\"footer\">")
            .append("生成时间: ").append(new java.util.Date())
            .append("</div>");
        
        html.append("</body></html>");
        
        return html.toString();
    }
}