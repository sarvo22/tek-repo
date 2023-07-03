package com.tekfilo.account.transaction.jv.dto;

import com.tekfilo.account.transaction.jv.dto.JVDetailDto;
import com.tekfilo.account.transaction.jv.dto.JVMainDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JVRequestPayload {

    private JVMainDto main;
    private List<JVDetailDto> detail;
}
