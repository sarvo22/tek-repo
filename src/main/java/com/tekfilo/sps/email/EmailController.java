package com.tekfilo.sps.email;

import com.fasterxml.jackson.databind.JsonNode;
import com.tekfilo.sps.util.SPSResponse;
import com.tekfilo.sps.util.SPSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/sps/email")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    @Value("${tekfilo.application.endpoint}")
    private String tekfiloApplicationEndPointUrl;

    @Value("${tekfilo.application.confirmpage.endpoint}")
    private String tekfiloApplicationConfirmpageEndpoint;

    @GetMapping
    public String entryPoint() {
        return "Email service working";
    }


    /**
     * Signup Email will take template from @signup-email-template.html file
     *
     * @return Response as @{@link String}
     */
    @PostMapping("/signup")
    public ResponseEntity<SPSResponse> sendSignupConfirmation(@RequestBody Map<String, Object> data) {
        final String toEmail = (String) data.get("email");
        final String url = (String) data.get("url");
        logger.info("Sending Signup email for " + toEmail);

        SPSResponse response = new SPSResponse();
        try {
            emailService.sendSignupemail(toEmail.split("@")[0], toEmail, url);
            response.setStatus(100);
            response.setMessage("Email Send successfully to " + toEmail);
        } catch (Exception ex) {
            response.setStatus(101);
            response.setMessage("Exception raised while sending email");
            logger.error("Exception raised while sending signup email to " + toEmail + "::" + ex.getCause() == null ? " Unable to fetch exact error message " : ex.getCause().getMessage());
        }
        logger.info("Signup Email dispatched successfully for " + toEmail);
        return new ResponseEntity<SPSResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/inviteuser")
    public ResponseEntity<SPSResponse> sendInvite(@RequestBody InviteForm inviteForm,
                                                  HttpServletRequest request) {
        logger.info("Sending Invitation email to {} " + inviteForm.getEmail());
        SPSResponse response = new SPSResponse();
        try {
            final String encodedUrl = SPSUtil.encode(inviteForm);
            logger.info("Encoded URI {} " + encodedUrl);
            logger.info("Decoded URI {} " + SPSUtil.decode(encodedUrl));
            final String confirmLink = tekfiloApplicationEndPointUrl.concat(tekfiloApplicationConfirmpageEndpoint).concat("/").concat(encodedUrl);
            logger.info("Final Url Received {} " + confirmLink);
            emailService.sendInviteuserMail(inviteForm.getEmail(), confirmLink, inviteForm);
            response.setStatus(100);
            response.setMessage("Email Send successfully to " + inviteForm.getEmail());
        } catch (Exception ex) {
            response.setStatus(101);
            response.setMessage("Exception raised while sending email");
            logger.error("Exception raised while sending signup email to " + inviteForm.getEmail() + "::" + ex.getCause() == null ? " Unable to fetch exact error message " : ex.getCause().getMessage());
        }
        logger.info("Invitation Email dispatched successfully for " + inviteForm.getEmail());
        return new ResponseEntity<SPSResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/sendinqueryemail")
    public ResponseEntity<String> sendEmail(@RequestBody JsonNode jsonNode) {
        logger.info("received email request from inquery form");
        String response = "Success";
        emailService.sendInquiryEmail(jsonNode);
        logger.info("Email dispatched successfully");
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

}
