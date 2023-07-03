package com.tekfilo.sps.email;


import com.fasterxml.jackson.databind.JsonNode;
import com.tekfilo.sps.config.BootupLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Service
public class EmailService {

    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public JavaMailSender javaMailSender;

    String NO_REPLY_EMAIL = "noreply@tekfilo.com";
    String ENQUIRY_EMAIL = "enquiry@tekfilo.com";

    @Autowired
    BootupLoader bootupLoader;

    @Async
    public void sendEmail(EmailEntity entity) {
        logger.error("Started to send email entity");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(entity.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(NO_REPLY_EMAIL));
            mimeMessageHelper.setTo(entity.getMailTo());
            mimeMessageHelper.setText(entity.getMailContent(), true);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            logger.error("Completed sending email from email-service");
        } catch (MessagingException e) {
            logger.error("Exception raised while dispatching email service " + e.getCause() == null ? " Unable to get proper error message as it returns null " : e.getCause().getMessage());
        }
    }

    @Async
    public void sendSignupemail(String name, String toEmail, String tokenUrl) throws Exception {
        EmailEntity entity = new EmailEntity();
        entity.setMailSubject("Welcome to Tekfilo!");
        entity.setMailFrom(NO_REPLY_EMAIL);
        entity.setMailTo(toEmail);
        entity.setMailContent(bootupLoader.getSignupTemplate().toString().replaceAll("@tekfilo_username", name).replaceAll("@tekfilo_tokenurl", tokenUrl));
        sendEmail(entity);
    }

    public void sendInviteuserMail(String toEmail, String tokenUrl, InviteForm inviteForm) {
        EmailEntity entity = new EmailEntity();
        entity.setMailSubject("Invitation Email");
        entity.setMailFrom(NO_REPLY_EMAIL);
        entity.setMailTo(toEmail);
        entity.setMailContent(
                bootupLoader.getInviteUserTemplate().toString().replaceAll("@confirmLink", tokenUrl)
                        .replaceAll("@senderName", inviteForm.getSenderName())
                        .replaceAll("@senderCompany", inviteForm.getSenderCompanyName())
                        .replaceAll("@toWhom", inviteForm.getEmail())
        );
        sendEmail(entity);
    }

    public void sendInquiryEmail(JsonNode jsonNode) {
        EmailEntity entity = new EmailEntity();
        entity.setMailTo(ENQUIRY_EMAIL);
        entity.setMailFrom(NO_REPLY_EMAIL);
        String name = jsonNode.get("first_name").textValue().concat(" ").concat(jsonNode.get("last_name").textValue());
        entity.setMailSubject("Inquiry Received from " + name);
        entity.setMailContent(
                bootupLoader.getInquiryTemplate().toString().replaceAll("@name", name)
                        .replaceAll("@email", jsonNode.get("email_id").textValue())
                        .replaceAll("@mobileno", jsonNode.get("phone_number").textValue())
                        .replaceAll("@message", jsonNode.get("message").textValue())
        );
        sendEmail(entity);
    }
}
