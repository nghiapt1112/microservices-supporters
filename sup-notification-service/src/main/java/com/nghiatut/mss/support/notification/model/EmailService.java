package com.nghiatut.mss.support.notification.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

import static javax.mail.Message.RecipientType.TO;

@Service
public class EmailService {
    protected static final Logger MAIL_LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private Environment environment;

    @Autowired
    private JavaMailSender sender;

    @Value("${mail.content}")
    private String CONTENT_TYPE;

    @Async
    public void send(Email email) {
        try {
            MAIL_LOGGER.info("Sending email ...");
            sender.send(this.getMime(email));
        } catch (Exception e) {
            throw new EmailException(toStr("error.mail.send.code"), toStr("error.mail.send.msg"), e);
        }
    }

    private MimeMessagePreparator getMime(final Email mail) {
        return (mimeMsg) -> {
            mimeMsg.setFrom(mail.getFrom());
            mimeMsg.setRecipient(TO, mail.getTo());
            mimeMsg.setSubject(mail.getSubject());

            if (mail.hasAttachment()) {
                MimeBodyPart messageContent = new MimeBodyPart();
                messageContent.setContent(mail.getMailContent(), CONTENT_TYPE);

                Multipart multiPart = mail.multipart();
                multiPart.addBodyPart(messageContent, 0);

                mimeMsg.setContent(multiPart);
            } else {
                mimeMsg.setContent(mail.getMailContent(), CONTENT_TYPE);
            }
        };
    }

    private String toStr(String key) {
        String val = environment.getProperty(key);
        return val != null ? val : key;
    }

}
