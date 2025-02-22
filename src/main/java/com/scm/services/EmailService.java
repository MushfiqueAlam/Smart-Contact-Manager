package com.scm.services;

public interface EmailService {
    void sendEmail(String to,String subject,String body);

    // we can add more method like
    void sendEmailwithhtml(String to,String subject,String body);
    
}
