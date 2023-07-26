package com.kbni.backendcode.services;

import org.springframework.stereotype.Service;

@Service
public interface IEmailService {

    public void sendSimpleEmail(String toEmail, String body, String subject);

    public void sendComplexEmail(String toEmail, String header, String body, String subject, String attachmentPath);
}
