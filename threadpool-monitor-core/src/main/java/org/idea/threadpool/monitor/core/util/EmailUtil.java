package org.idea.threadpool.monitor.core.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * @Author linhao
 * @Date created in 7:28 下午 2022/9/14
 */
public class EmailUtil {

    //发送的邮箱 内部代码只适用qq邮箱
    private static final String USER = "linhao997@qq.com";
    //授权密码 通过QQ邮箱设置->账户->POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务->开启POP3/SMTP服务获取
    private static final String PWD = "rpcgluvpuojjdhjj";


    public static void send(String subject, String content, String[] to) throws MessagingException {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.user", USER);
        props.put("mail.password", PWD);

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        BodyPart messageBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        // 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);
        if (to != null) {
            String toList = getMailList(to);
            InternetAddress[] iaToList = InternetAddress.parse(toList);
            // 收件人
            message.setRecipients(Message.RecipientType.TO, iaToList);
        }
        message.setSentDate(new Date());
        message.setSubject(subject);
        message.setText(content);
        //显示以html格式的文本内容
        messageBodyPart.setContent(content, "text/html;charset=utf-8");
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);
        // 发送邮件
        Transport.send(message);
    }

    private static String getMailList(String[] mailArray) {
        StringBuffer toList = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length < 2) {
            toList.append(mailArray[0]);
        } else {
            for (int i = 0; i < length; i++) {
                toList.append(mailArray[i]);
                if (i != (length - 1)) {
                    toList.append(",");
                }
            }
        }
        return toList.toString();
    }

}
