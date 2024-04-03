package com.intuit.topscorerservice.exception;

import com.intuit.topscorerservice.util.exception.codes.ExceptionCodes;

public class DaoServiceException extends RuntimeException {
    public DaoServiceException(final ExceptionCodes exception) {
        super(exception.getCode().concat(STR." : \{exception.getMessage()}"));
    }
}
