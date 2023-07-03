package com.tekfilo.sps.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FileResponseMessage {
    private String responseMessage;
    private int status;
    private String newFileName;
}
