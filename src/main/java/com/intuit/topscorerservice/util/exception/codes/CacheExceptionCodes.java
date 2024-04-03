package com.intuit.topscorerservice.util.exception.codes;

public enum CacheExceptionCodes implements ExceptionCodes{

    CACHE_ERROR("CAC_414", "Error while fetching Cache data"),
    CACHE_UPDATE_ERROR("CAC_415", "Error while updating Cache data") ;
    private String code;
    private String message;

    CacheExceptionCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
