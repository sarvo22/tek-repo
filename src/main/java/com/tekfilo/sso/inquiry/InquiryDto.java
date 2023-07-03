package com.tekfilo.sso.inquiry;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class InquiryDto {
    private String first_name;
    private String last_name;
    private String phone_number;
    private String email_id;
    private String message;
}
