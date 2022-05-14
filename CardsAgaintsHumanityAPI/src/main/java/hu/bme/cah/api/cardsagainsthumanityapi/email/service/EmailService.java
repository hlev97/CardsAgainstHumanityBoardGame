package hu.bme.cah.api.cardsagainsthumanityapi.email.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service to ensure that the backend is able to send emails
 */
@Service
@Slf4j
public class EmailService {
    /**
     * Injection of MailSender
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Method that sends emails with the given parameters
     * @param to recipient
     * @param subject subject
     * @param body content
     */
    public void sendEmail(String to, String subject, String body) {
        log.trace("In sendEmail method");
        log.info("Sending email...");
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("arieane5.alf@gmail.com");
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        try {
            log.trace("In try block");
            mailSender.send(msg);
            log.info("The sending was successful");
        } catch(MailException e) {
            log.trace("In catch block");
            log.error("The sending was unsuccessful");
        }

    }
}
