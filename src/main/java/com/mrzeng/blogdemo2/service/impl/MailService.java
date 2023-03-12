package com.mrzeng.blogdemo2.service.impl;

import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String mailUserName;
    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    //import org.thymeleaf.TemplateEngine;
    private TemplateEngine templateEngine;

    /*激活账号*/
    public void sendMailForActivationComment(String activationUrl, String email, String comment) {
        /*创建邮箱对象*/
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            /*设置邮件主题*/
            message.setSubject("MrZengBlog-感谢评论!");
            /*设置邮件发送者*/
            message.setFrom(mailUserName);
            /*设置邮件接受者 可以多个*/
            message.setTo(email);
            message.setTo("1164334031@qq.com");
            /*设置邮件抄送人 可以多个*/
            //message.setCc("");
            /*设置邮件隐秘抄送人 可以多个*/
            //message.setBCc("");
            /*设置邮件发送日期*/
            message.setSentDate(new Date());
            /*创建上下文环境*/
            //import org.thymeleaf.context.Context;
            Context context = new Context();
            context.setVariable("activationUrl", activationUrl);
            context.setVariable("comment", comment);
            String text = templateEngine.process("activation-account.html", context);
            /*设置邮件正文*/
            message.setText(text, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*邮件发送*/
        javaMailSender.send(mimeMessage);

    }
}
