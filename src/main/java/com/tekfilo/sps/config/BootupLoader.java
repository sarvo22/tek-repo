package com.tekfilo.sps.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Configuration
public class BootupLoader {

    private static final StringBuilder signupTemplate = new StringBuilder();
    private static final StringBuilder inviteUserTemplate = new StringBuilder();

    private static final StringBuilder inquiryTemplate = new StringBuilder();

    private static final Logger logger = LoggerFactory.getLogger(BootupLoader.class);

    public StringBuilder getSignupTemplate() {
        return signupTemplate;
    }

    public StringBuilder getInquiryTemplate() {
        return inquiryTemplate;
    }

    public StringBuilder getInviteUserTemplate() {
        return inviteUserTemplate;
    }

    @Bean
    void loadSignupTemplate() {
        try {
            logger.info("Started to load Signup Html Template files");
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/signup-email-template.html");
            if (inputStream == null)
                throw new FileNotFoundException();
            String line;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                while ((line = br.readLine()) != null) {
                    signupTemplate.append(System.getProperty("line.separator"));
                    signupTemplate.append(line);
                }
            }
            logger.info("Completed to load Signup html Template files");
        } catch (Exception e) {
            logger.error("Unable to load Html Signup Template " + e.getCause() == null ? "Unable to get Proper error message as it returns null " : e.getCause().getMessage());
        }


    }

    @Bean
    void loadInviteUserTemplate() {
        try {
            logger.info("Started to load Signup Html Template files");
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/invite-user-template.html");
            if (inputStream == null)
                throw new FileNotFoundException();
            String line;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                while ((line = br.readLine()) != null) {
                    inviteUserTemplate.append(System.getProperty("line.separator"));
                    inviteUserTemplate.append(line);
                }
            }
            logger.info("Completed to load Signup html Template files");
        } catch (Exception e) {
            logger.error("Unable to load Html Signup Template " + e.getCause() == null ? "Unable to get Proper error message as it returns null " : e.getCause().getMessage());
        }
    }

    @Bean
    void loadInquiryTemplate() {
        try {
            logger.info("Started to load Inquiry Html Template files");
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/inquiry.html");
            if (inputStream == null)
                throw new FileNotFoundException();
            String line;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                while ((line = br.readLine()) != null) {
                    inquiryTemplate.append(System.getProperty("line.separator"));
                    inquiryTemplate.append(line);
                }
            }
            logger.info("Completed to load Inquiry html Template files");
        } catch (Exception e) {
            logger.error("Unable to load Html Inquiry Template " + e.getCause() == null ? "Unable to get Proper error message as it returns null " : e.getCause().getMessage());
        }


    }
}
