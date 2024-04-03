package com.intuit.topscorerservice.exception;

import com.intuit.topscorerservice.util.exception.codes.ExceptionCodes;

public class CacheInitializationException extends RuntimeException{
    public CacheInitializationException(final ExceptionCodes exception) {
        super(exception.getCode().concat(STR." : \{exception.getMessage()}"));
    }
}
