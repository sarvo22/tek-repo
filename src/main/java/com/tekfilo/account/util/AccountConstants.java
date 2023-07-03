package com.tekfilo.account.util;

import java.util.Arrays;
import java.util.List;

public interface AccountConstants {

    String DEBIT = "D";
    String CREDIT = "C";
    int IN = 1;
    int OUT = -1;
    String CREDIT_TYPE = "CREDIT";
    String DEBIT_TYPE = "DEBIT";
    String BSA = "BSA";
    String NORMAL_ACCOUNT = "NORMAL";
    List<String> accountMappingList = Arrays.asList("NORMAL", "MEMO");
    String UNPAID = "UNPAID";
    String DRAFT = "DRAFT";
    String POSTED = "POSTED";
}
