//package com.kbni.backendcode.services.impl;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessagePreparator;
//
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.modelmapper.internal.bytebuddy.matcher.ElementMatchers.any;
//
//@SpringBootTest
//public class EmailServiceTest {
//
//    @Mock
//    private JavaMailSender mailSender;
//
//    @InjectMocks
//    private EmailService emailService;
//
//    @Test
//    public void testSendComplexEmail() {
//        String toEmail = "test@example.com";
//        String header = "Greetings!";
//        String body = "This is a test email with an HTML template.";
//        String subject = "Test Email";
//        String attachmentPath = "static/test.txt";
//
//        emailService.sendComplexEmail(toEmail, header, body, subject, attachmentPath);
//
//        verify(mailSender, times(1)).send((MimeMessagePreparator) any());
//    }
//}
