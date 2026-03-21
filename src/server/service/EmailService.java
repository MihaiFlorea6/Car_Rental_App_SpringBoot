package com.carrental.server.service;

import com.carrental.server.model.EmailNotification;
import com.carrental.server.model.User;
import com.carrental.server.repository.EmailNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmailService {

    @Autowired
    private EmailNotificationRepository emailRepository;

    // NU mai avem JavaMailSender - doar salvăm în DB
    // @Autowired
    // private JavaMailSender mailSender;

    public EmailNotification sendEmail(User sender, User receiver, String subject, String message) {
        try {
            // Create email notification record
            EmailNotification notification = new EmailNotification(sender, receiver, subject, message);
            EmailNotification saved = emailRepository.save(notification);

            System.out.println("✉️ Email salvat în DB:");
            System.out.println("   De la: " + sender.getUsername());
            System.out.println("   Către: " + receiver.getUsername());
            System.out.println("   Subiect: " + subject);

            // OPȚIONAL: Poți trimite email real dacă ai SMTP configurat
            /*
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(receiver.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailMessage.setFrom("noreply@carrental.com");
            mailSender.send(mailMessage);
            System.out.println("✅ Email trimis și prin SMTP!");
            */

            return saved;
        } catch (Exception e) {
            System.err.println("❌ Eroare la salvarea email-ului: " + e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public List<EmailNotification> getReceivedEmails(Long userId) {
        return emailRepository.findByReceiverIdOrderBySentAtDesc(userId);
    }

    public List<EmailNotification> getSentEmails(Long userId) {
        return emailRepository.findBySenderIdOrderBySentAtDesc(userId);
    }

    public List<EmailNotification> getAllEmails() {
        return emailRepository.findAll();
    }
}