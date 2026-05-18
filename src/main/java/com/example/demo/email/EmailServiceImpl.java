package com.example.demo.email;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final String sendGridApiKey;
    private final String from;

    public EmailServiceImpl(
            @Value("${app.mail.sendgrid.api-key:${SENDGRID_API_KEY:}}") String sendGridApiKey,
            @Value("${app.mail.from}") String from) {
        this.sendGridApiKey = sendGridApiKey;
        this.from = from;
    }

    @Override
    public boolean sendEmail(Email email) {
        LOGGER.info("EmailBody: {}", email.toString());
        return sendEmailTool(email.getContent(), email.getEmail(), email.getSubject());
    }

    public boolean sendEmailTool(String textMessage, String email, String subject) {
        try {
            if (sendGridApiKey == null || sendGridApiKey.isBlank()) {
                LOGGER.error("SENDGRID_API_KEY no esta configurada");
                return false;
            }

            com.sendgrid.helpers.mail.objects.Email fromEmail = new com.sendgrid.helpers.mail.objects.Email(from);
            com.sendgrid.helpers.mail.objects.Email toEmail = new com.sendgrid.helpers.mail.objects.Email(email);
            Content content = new Content("text/html", textMessage);
            Mail mail = new Mail(fromEmail, subject, toEmail, content);

            SendGrid sendGrid = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            int statusCode = response.getStatusCode();
            boolean send = statusCode >= 200 && statusCode < 300;

            if (!send) {
                LOGGER.error("Error enviando mail con SendGrid. status={}, body={}", statusCode, response.getBody());
                return false;
            }

            LOGGER.info("Mail enviado!");
            return true;
        } catch (IOException e) {
            LOGGER.error("Hubo un error de comunicacion con SendGrid", e);
        } catch (Exception e) {
            LOGGER.error("Hubo un error al enviar el mail con SendGrid", e);
        }

        return false;
    }

}
