package com.tekfilo.account.transaction.post;

import lombok.Data;

import java.util.List;

@Data
public class PostingDeleteDto {
    private List<Integer> voucherIdList;
    private List<String> voucherTypeList;
}
