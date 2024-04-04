package com.intuit.topscorerservice.util.exception.codes;

import lombok.Getter;

@Getter
public enum DaoExceptionCodes implements ExceptionCodes {

    DAO_ERROR("DAO_414", "Error while fetching DB data"),
    DAO_UPDATE_ERROR("DAO_415", "Error while updating DB data");

    private String code;
    private String message;

    DaoExceptionCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return STR."\{code} : \{message}";
    }
}
