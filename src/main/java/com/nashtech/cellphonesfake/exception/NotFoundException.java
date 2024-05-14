package com.nashtech.cellphonesfake.exception;
import com.nashtech.cellphonesfake.utils.MessageUtils;
import lombok.Setter;

@Setter
public class NotFoundException extends RuntimeException {
    private String message;

    public NotFoundException(String errorCode, Object... var2) {
        this.message = MessageUtils.getMessage(errorCode, var2);
    }
    @Override
    public String getMessage() {
        return message;
    }
}
