package com.nashtech.cellphonesfake.exception;

import com.nashtech.cellphonesfake.utils.MessageUtils;

public class UnAuthorizedException extends RuntimeException {
    private final String message;

    public UnAuthorizedException(String errorCode, Object... var2) {
        this.message = MessageUtils.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
