package com.nashtech.cellphonesfake.utils;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;


public class MessageUtils {
    private MessageUtils(){}

    public static String getMessage(String errorCode, Object... var2) {
        String message;
        message = errorCode;
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, var2);
        return formattingTuple.getMessage();
    }
}
