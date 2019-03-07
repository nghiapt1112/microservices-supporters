package com.nghiatut.mss.support.notification.model;

import com.nghia.libraries.commons.mss.infrustructure.domain.AbstractObject;
import org.apache.commons.collections4.CollectionUtils;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.List;

public class Email extends AbstractObject{
    private static final String CONTENT_TYPE = "application/x-any";

    private InternetAddress from;
    private InternetAddress to;
    private List<String> cc;
    private String subject;
    private List<Attachment> attachments;
    private Object[] htmlVars;
    private String mailContent;
    private String contentType;


    public Email() {
    }

    public Email(String from, String to) {
        try {
            this.from = new InternetAddress(from);
            this.to = new InternetAddress(to);
        } catch (AddressException e) {
            throw new EmailException("E002", "Invalid email address");
        }

    }

    public InternetAddress getFrom() {
        return from;
    }

    public InternetAddress getTo() {
        return to;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Object[] getHtmlVars() {
        return htmlVars;
    }

    public void setHtmlVars(Object[] htmlVars) {
        this.htmlVars = htmlVars;
    }

    public boolean hasAttachment() {
        return CollectionUtils.isNotEmpty(this.attachments);
    }

    public Multipart multipart() {
        Multipart multipart = new MimeMultipart();
        MimeBodyPart bodyPart = null;

        for (Attachment file : this.attachments) {
            try {
                bodyPart = new MimeBodyPart();
                bodyPart.setFileName(file.getName());
                bodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(file.getData(), CONTENT_TYPE)));

                multipart.addBodyPart(bodyPart);
                return multipart;
            } catch (MessagingException e) {
                throw new EmailException("E003", "Email attachment error");
            }
        }
        return null;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setTo(InternetAddress to) {
        this.to = to;
    }
}


class Attachment extends AbstractObject {
    private byte[] data;
    private String name;

    public byte[] getData() {
        return data;
    }

    public String getName() {
        return name;
    }
}
