package com.kbni.backendcode.services.impl;

import com.kbni.backendcode.exceptions.MailSendingException;
import com.kbni.backendcode.services.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

@Service
public class EmailService implements IEmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendSimpleEmail(String toEmail, String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@example.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        LOG.info("Mail Send...");
    }

    @Override
    public void sendComplexEmail(String toEmail, String header, String body, String subject, String attachmentPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("header", header);
            context.setVariable("body", body);

            String html = templateEngine.process("email-template", context);

            helper.setFrom("noreply@example.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(html, true);

            if (attachmentPath != null) {
                File file = new ClassPathResource(attachmentPath).getFile();
                helper.addAttachment(file.getName(), file);
            }

            mailSender.send(message);
            LOG.info("Complex mail has been sent");
        } catch (MessagingException | IOException e) {
            throw new MailSendingException("Error while sending mail", e);
        }
    }

}
