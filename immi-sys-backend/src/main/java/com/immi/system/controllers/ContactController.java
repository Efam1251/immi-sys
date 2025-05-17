package com.immi.system.controllers;

import com.immi.system.models.ContactForm;
import com.immi.system.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    
    @Autowired
    private EmailService emailService;
    
    @PostMapping("/email-contact")
    public ResponseEntity<String> sendEmail(@RequestBody ContactForm contactForm) {
        try {
            emailService.sendContactEmail(contactForm);
            return ResponseEntity.ok("Message sent successfully!");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message.");
        }
    }

}