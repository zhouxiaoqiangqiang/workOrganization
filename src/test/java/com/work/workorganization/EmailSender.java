package com.work.workorganization;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class EmailSender {

    public void sendEmailWithAttachments(String fileInfoJsonStr, String toEmail) {
        // 解析 fileInfoJsonStr
        try {
            JSONArray jsonArray = new JSONArray(fileInfoJsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String downloadUrl = jsonObject.getString("downloadUrl");

                // 下载文件保存到本地
                File downloadedFile = downloadFileFromUrl(downloadUrl);

                System.out.println("downloadedFile = " + downloadedFile.length());
                // 发送邮件
               // sendEmailWithAttachment(downloadedFile, toEmail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据url下载文件成 File
     */
    private File downloadFileFromUrl(String downloadUrl) {
        File file = null;

        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);
                file = new File(fileName);

                InputStream inputStream = httpURLConnection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
            } else {
                System.out.println("HTTP response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    private void sendEmailWithAttachment(File attachment, String toEmail) {
        Properties props = new Properties();
        // 设置邮件服务器
        props.put("mail.smtp.host", "10.182.31.170");
        // 设置是否需要验证
        props.put("mail.smtp.auth", "true");

        // 创建一个 Session 实例
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your_email_address", "your_email_password");
            }
        });

        try {
            // 创建邮件消息
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("your_email_address"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Email with Attachment");

            // 创建附件部分
            BodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(attachment.getName());

            // 创建消息体
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please see the attachments.");

            // 组合附件和消息体
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachmentBodyPart);
            multipart.addBodyPart(messageBodyPart);

            // 设置消息内容
            message.setContent(multipart);

            // 发送邮件
            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String fileInfoJsonStr = "[{\"fileName\":\"测试图片1.docx\",\"createTime\":\"2024-03-22 18:06:40\",\"downloadUrl\":\"https://pic.rmb.bdstatic.com/bjh/914b8c0f9814b14c5fedeec7ec6615df5813.jpeg\",\"userName\":\"葛燕\",\"userId\":\"021665\"},{\"fileName\":\"测试图片2.xlsx\",\"createTime\":\"2024-03-22 18:06:45\",\"downloadUrl\":\"https://inews.gtimg.com/om_bt/OjPq2cnMN_-ivDKjxpCZ2kk_ab8YC5VMnL-0pQ21fUvd4AA/1000\",\"userName\":\"葛燕\",\"userId\":\"021665\"}]";
        String toEmail = "1525761613@qq.com";

        EmailSender emailSender = new EmailSender();
        emailSender.sendEmailWithAttachments(fileInfoJsonStr, toEmail);
    }
}