package com.tekfilo.inventory.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class TekfiloUtils {
    
    public static void setErrorResponse(InventoryResponse inventoryResponse, Exception exception) {
        inventoryResponse.setStatus(101);
        inventoryResponse.setLangStatus("error_101");
        if (exception.getCause() instanceof SQLException) {
            SQLException e = (SQLException) exception.getCause().getCause();
            inventoryResponse.setMessage(e.getMessage());
        } else if (exception.getCause() instanceof DataIntegrityViolationException) {
            DataIntegrityViolationException e = (DataIntegrityViolationException) exception.getCause().getCause();
            inventoryResponse.setMessage(e.getMessage());
        } else if (exception.getCause() instanceof SQLGrammarException) {
            SQLGrammarException e = (SQLGrammarException) exception.getCause().getCause();
            inventoryResponse.setMessage(e.getMessage());
        } else if (exception.getCause() instanceof ConstraintViolationException) {
            inventoryResponse.setMessage(exception.getCause().getCause().getMessage());
        } else {
            inventoryResponse.setMessage(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
        }
    }
}
