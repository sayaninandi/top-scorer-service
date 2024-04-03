package com.intuit.topscorerservice.exception;

import com.intuit.topscorerservice.util.exception.codes.ExceptionCodes;

public class CacheUpdateFailureException extends RuntimeException {
    public CacheUpdateFailureException(final ExceptionCodes exception) {
        super(exception.getCode().concat(STR." : \{exception.getMessage()}"));
    }
}
