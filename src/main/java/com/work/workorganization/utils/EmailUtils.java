package com.work.workorganization.utils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * -@Desc:  邮件工具类
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/4 16:24
 **/
@Slf4j
public class EmailUtils {

    /**
     * 发送邮件
     *
     * @param mailAddressList         收件人邮箱列表
     * @param date                    发送日期
     * @param subject                 邮件主题标题
     * @param content                 邮件正文内容
     * @param fileList                附件列表
     * @param ccList                  抄送人邮箱列表
     * @param onBehalfUserName        邮件代发人姓名
     * @param onBehalfUserMailAddress 邮件代发人邮箱地址
     */
    public static void sendMail(List<String> mailAddressList, Date date, String subject, String content, List<File> fileList, List<String> ccList, String onBehalfUserName, String onBehalfUserMailAddress) throws Exception {

        //邮件配置
        Properties properties = new Properties();
        //设置邮件服务器协议
        properties.setProperty("mail.transport.protocol", "smtp");
        //设置邮件服务器主机
        properties.setProperty("mail.smtp.host", "smtp.163.com");
        //设置发件人邮箱端口
        properties.setProperty("mail.smtp.port", "25");
        //设置是否需要验证
        properties.setProperty("mail.smtp.auth", "true");

        //发送邮件公邮地址
        String publicMailAddress = "zhouxiaoqiangjava@163.com";
        //发送邮件公邮密码
        String publicMailPassword = "GTj77y3Xuw4tWPpJ";

        //创建邮件会话
        Session session = Session.getDefaultInstance(properties);
        session.setDebug(true);

        //创建邮件对象
        MimeMessage message = createMimeMessage(session, publicMailAddress, mailAddressList, date, subject, content, fileList, ccList, onBehalfUserName, onBehalfUserMailAddress);

        //发送邮件
        Transport transport = session.getTransport();
        log.info("===== 开始连接邮件服务器 =====");
        transport.connect(publicMailAddress, publicMailPassword);
        log.info("===== 连接邮件服务器成功 =====");
        log.info("===== 开始发送邮件 =====");
        transport.sendMessage(message, message.getAllRecipients());
        log.info("===== 邮件发送结束 =====");
        transport.close();
        log.info("===== 关闭邮件服务器连接 =====");

    }

    /**
     * 创建邮件对象
     */
    private static MimeMessage createMimeMessage(Session session, String publicMailAddress, List<String> mailAddressList, Date date, String subject, String content, List<File> fileList, List<String> ccList, String onBehalfUserName, String onBehalfUserMailAddress) throws Exception {
        //解决附件名字过长被截取的问题
        System.getProperties().setProperty("mail.mime.splitlongparameters", "false");

        //创建邮件对象
        MimeMessage message = new MimeMessage(session);

        //是否有代发邮件人
        if (StringUtils.isNotBlank(onBehalfUserName)) {
            //无代发邮箱的的情况下 仅以 代发邮件人名义代发  用公邮发送
            message.setFrom(new InternetAddress(publicMailAddress, onBehalfUserName));
            message.setHeader("Sender", onBehalfUserName);

            //有代发邮箱的情况下  用代发邮箱发送
            if (StringUtils.isNotBlank(onBehalfUserMailAddress)) {
                //设置代发件人回复邮箱 (即用户点击回信 会发给代发邮箱)
                message.setReplyTo(new InternetAddress[]{new InternetAddress(onBehalfUserMailAddress,onBehalfUserName)});
            }
        } else {
            //无代发邮箱的情况下 用公邮发送
            message.setFrom(new InternetAddress(publicMailAddress, "测试公共邮箱"));
        }

        //设置收件人
        for (String sendMailAddress : mailAddressList) {

            try {
                message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(sendMailAddress));
            } catch (AddressException e) {
                log.error("Illegal email address: {}", sendMailAddress);
            }
        }
        //设置抄送人
        for (String ccMailAddress : ccList) {
            try {
                message.addRecipient(MimeMessage.RecipientType.CC, new InternetAddress(ccMailAddress));
            } catch (AddressException e) {
                log.error("Illegal email address: {}", ccMailAddress);
            }
        }

        //邮件主题
        message.setSubject(subject, "UTF-8");
        //邮件发送时间
        message.setSentDate(date);
        //设置邮件内容格式 为 混合模式
        MimeMultipart mimeMultipart = new MimeMultipart("mixed");
        message.setContent(mimeMultipart);
        //邮件正文内容 (支持html格式)
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(content, "text/html;charset=UTF-8");
        //组装内容
        mimeMultipart.addBodyPart(htmlBodyPart);
        //添加附件
        fileList.forEach(file -> {
            if (null == file || !file.exists()) {
                return;
            }
            try {
                //设置相关文件
                MimeBodyPart filePart = new MimeBodyPart();
                FileDataSource fileDataSource = new FileDataSource(file);
                DataHandler dataHandler = new DataHandler(fileDataSource);
                //文件处理
                filePart.setDataHandler(dataHandler);
                //附件名称, 防止中文乱码
                filePart.setFileName(MimeUtility.encodeText(file.getName()));
                //放入正文 (有先后顺序,所以在正文之后)
                mimeMultipart.addBodyPart(filePart);
            } catch (Exception e) {
                log.error("发送邮件时添加附件失败!附件名称:{},原因:{}", file.getName(), e.getMessage());
            }
        });

        //保存设置
        message.saveChanges();
        log.info("===== 邮件消息对象创建成功 =====");
        return message;
    }

    /**
     * 异步多线程发送邮件
     *
     * @param mailAddressList         收件人邮箱列表
     * @param date                    发送日期
     * @param subject                 邮件主题标题
     * @param content                 邮件正文内容
     * @param fileList                附件列表
     * @param ccList                  抄送人邮箱列表
     * @param onBehalfUserName        邮件代发人姓名
     * @param onBehalfUserMailAddress 邮件代发人邮箱地址
     */
    public static void asyncSendMail(List<String> mailAddressList, Date date, String subject, String content, List<File> fileList, List<String> ccList, String onBehalfUserName, String onBehalfUserMailAddress) {
        if (CollectionUtil.isEmpty(mailAddressList)) {
            return;
        }
        //多线程发送邮件
        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(() -> {
            try {
                sendMail(mailAddressList, date, subject, content, fileList, ccList, onBehalfUserName, onBehalfUserMailAddress);
            } catch (Exception e) {
                log.error("===== 发送邮件失败 =====");
                e.printStackTrace();
            }
        });
        //释放资源
        executorService.shutdown();

    }

    /**
     * 生成件html格式的正文内容
     *
     * @param tableColumnInfoMap 表格字段信息
     * @param tableDataList      表格数据
     *                           <p>
     *                           【注意】 表格字段信息 中的key 必须要求和表格数据中的key保持一致 否则无法获取到对应key列中的值
     */
    public static String generateMailHtmlTableContent(LinkedHashMap<String, String> tableColumnInfoMap, List<Map<String, String>> tableDataList) {
        if (CollectionUtil.isEmpty(tableColumnInfoMap)) {
            log.error("生成件html格式的正文内容失败!表格字段信息为空!");
        }
        StringBuilder htmlBuilder = new StringBuilder();

        // 开始HTML文档
        htmlBuilder.append("<html>")
                .append("<head>")
                .append("<style>")
                .append("table {border-collapse: collapse; width: 100%;}")
                .append("th, td {border: 1px solid black; text-align: left; padding: 8px;}")
                .append("</style>")
                .append("</head>")
                .append("<body>");

        //表格名称
        String tableName = tableColumnInfoMap.getOrDefault("tableName", null);
        if (StringUtils.isNotBlank(tableName)) {
            htmlBuilder.append("<h2>").append(tableName).append("</h2>");
            //拼接完表名之后移除掉,防止拼到表头列名中去
            tableColumnInfoMap.remove("tableName");
        }
        // 开始拼接表格
        htmlBuilder.append("<table border ='1' style='width:100%;height:auto;'>")
                .append("<div class=\"container\">")
                .append("<table>")
                .append("<tr style=\"background-color: #428BCA; color:#ffffff\";>");

        // 添加表头
        tableColumnInfoMap.values().forEach(
                columnTitle -> htmlBuilder.append("<th>").append(columnTitle).append("</th>")
        );

        // 添加表格内容
        tableDataList.forEach(rowData -> {
            htmlBuilder.append("<tr>");
            tableColumnInfoMap.keySet().forEach(columnKey -> {
                String dataValue = rowData.get(columnKey);
                htmlBuilder.append("<td>").append(StringUtils.isNotBlank(dataValue) ? dataValue : "").append("</td>");
            });
            htmlBuilder.append("</tr>");
        });

        // 结束表格和HTML文档
        htmlBuilder.append("</table>")
                .append("</div>")
                .append("</body>")
                .append("</html>");

        return htmlBuilder.toString();
    }
}
