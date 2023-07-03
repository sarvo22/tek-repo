package com.tekfilo.account.upload;

import com.tekfilo.account.util.AccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/account/upload")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @PostMapping("/update")
    public ResponseEntity<AccountResponse> updateUploadedDocumentsUrl(@RequestBody DocumentDto documentDto) {
        AccountResponse response = new AccountResponse();
        try {
            uploadService.updateUploadedDocumentUrls(documentDto);
            response.setStatus(100);
            response.setLangStatus("modify_100");
            response.setMessage("Document Uploaded");
        } catch (Exception e) {
            response.setId(null);
            response.setStatus(101);
            response.setLangStatus("error_101");
            response.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
            log.error("Server error on Document Upload");
        }
        return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
    }
}
