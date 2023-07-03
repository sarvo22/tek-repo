package com.tekfilo.sps.sms;

import com.tekfilo.sps.util.SPSResponse;
import com.twilio.Twilio;
import com.twilio.exception.AuthenticationException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/sps/sms")
public class SMSController {

    public static final String ACCOUNT_SID = "AC018efd3e44ac12bd8e3edbc2d5195730";
    public static final String AUTH_TOKEN = "f9fbd45fdd32656fb66fc646200f5559";


    @GetMapping("/testsps")
    public String test() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        final String baseUrl = "http://metals-api.com/api/latest?access_key=fndz4172efd3m7eqzm2eoj15ne40el203yo3sxn8wmrs2ykn1sjhc42yxjq1";
        try {
            URI uri = new URI(baseUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>("body", headers);

            String response = restTemplate.getForObject(baseUrl, String.class);
            System.out.println(response);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        return "success";
    }

    @PostMapping("/twilio/send")
    public ResponseEntity<SPSResponse> sendSMS(@RequestBody SMSDto smsDto) {
        SPSResponse spsResponse = new SPSResponse();
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(new PhoneNumber("+916369340912"),
                    new PhoneNumber("+16073005590"),
                    "Sample SMS Send from Twilio Account via Tekfilo.com?").create();
            spsResponse.setResponseOut(message.getSid());
            spsResponse.setStatus(100);
            spsResponse.setMessage("SMS Sent");
        } catch (AuthenticationException ex) {
            log.error(ex.getMessage());
            spsResponse.setStatus(101);
            spsResponse.setMessage(ex.getMessage());
        }
        return new ResponseEntity<SPSResponse>(spsResponse, HttpStatus.OK);
    }

}
