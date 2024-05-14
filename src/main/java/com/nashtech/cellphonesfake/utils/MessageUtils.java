package com.nashtech.cellphonesfake.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.MissingResourceException;

public class MessageUtils {
    private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);

    public static String getMessage(String errorCode, Object... var2) {
        String message;
        try {
            message = errorCode;
            log.info(message);
        } catch (MissingResourceException ex) {
            // case message_code is not defined.
            message = errorCode;
        }
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, var2);
        return formattingTuple.getMessage();
    }
}
