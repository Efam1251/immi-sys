package com.immi.system.services;

import com.immi.system.models.ContactForm;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendContactEmail(ContactForm contactForm) throws MessagingException {
        // Create the email message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Set the email parameters
        helper.setTo(senderEmail);
        helper.setSubject("Acevedo Consulting Customer Message."); // Use the passed subject
        helper.setText(contactForm.getMessage() + "\n" + "My email is " + contactForm.getEmail());     // Use the passed message
        
        mailSender.send(mimeMessage);
    }
}
