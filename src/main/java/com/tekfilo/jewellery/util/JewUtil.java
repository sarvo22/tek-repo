package com.tekfilo.jewellery.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class JewUtil {
    public static void setSuccessResponse(JewResponse scmResponse) {
        scmResponse.setStatus(100);
        scmResponse.setLangStatus("save_100");
        scmResponse.setMessage("Saved Successfully");
    }

    public static void setSuccessResponse(JewResponse scmResponse, Integer id) {
        scmResponse.setId(id);
        scmResponse.setStatus(100);
        scmResponse.setLangStatus("save_100");
        scmResponse.setMessage("Saved Successfully");
    }

    public static void setSuccessResponse(JewResponse scmResponse, Integer id, String message) {
        scmResponse.setId(id);
        scmResponse.setStatus(100);
        scmResponse.setLangStatus("save_100");
        scmResponse.setMessage(message);
    }

    public static void setErrorResponse(JewResponse scmResponse, Exception exception) {
        scmResponse.setStatus(101);
        scmResponse.setLangStatus("error_101");
        if (exception.getCause() instanceof SQLException) {
            SQLException e = (SQLException) exception.getCause().getCause();
            scmResponse.setMessage(e.getMessage());
        } else if (exception.getCause() instanceof DataIntegrityViolationException) {
            DataIntegrityViolationException e = (DataIntegrityViolationException) exception.getCause().getCause();
            scmResponse.setMessage(e.getMessage());
        } else if (exception.getCause() instanceof SQLGrammarException) {
            SQLGrammarException e = (SQLGrammarException) exception.getCause().getCause();
            scmResponse.setMessage(e.getMessage());
        } else if (exception.getCause() instanceof ConstraintViolationException) {
            scmResponse.setMessage(exception.getCause().getCause().getMessage());
        } else {
            scmResponse.setMessage(Optional.ofNullable(exception.getCause()).isPresent() ? exception.getCause().getMessage() : exception.getMessage());
        }
    }

    public static Double ifNullZero(Double number) {
        if (number == null) {
            return 0.00;
        }
        return number;
    }
}
