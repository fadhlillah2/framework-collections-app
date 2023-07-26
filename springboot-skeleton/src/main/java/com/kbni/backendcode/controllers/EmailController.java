package com.kbni.backendcode.controllers;

import com.kbni.backendcode.services.impl.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send")
    public String sendMail() {
        emailService.sendSimpleEmail("test@example.com", "Hello, this is a test email!", "Test Email");
        return "Email Sent!";
    }

    @GetMapping("/sendComplex")
    public String sendComplexMail() {
        String header = "Greetings!";
        String body = "This is a test email with an HTML template.";
        String attachmentPath = "static/test.txt";  // Path to the file you want to attach
        emailService.sendComplexEmail("test@example.com", header, body, "Test Email", attachmentPath);
        return "Complex email sent!";
    }
}
