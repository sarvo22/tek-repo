package com.tekfilo.sps.sms;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SMSDto {
    private String fromPhoneNo;
    private String toPhoneNo;
}
