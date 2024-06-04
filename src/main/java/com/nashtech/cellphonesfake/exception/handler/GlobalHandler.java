package com.nashtech.cellphonesfake.exception.handler;


import com.nashtech.cellphonesfake.exception.BadRequestException;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.exception.UnAuthorizedException;
import com.nashtech.cellphonesfake.view.ErrorVm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalHandler extends ResponseEntityExceptionHandler {
    private static final String ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorVm> handleNotFoundException(NotFoundException exception, WebRequest request) {
        String message = exception.getMessage();
        ErrorVm errorVm = new ErrorVm(HttpStatus.NOT_FOUND.toString(), "Not Found", message);
        log.warn(ERROR_LOG_FORMAT, this.getServletPath(request), 404, message);
        log.debug(exception.toString());
        return new ResponseEntity<>(errorVm, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorVm> handleBadRequestException(BadRequestException exception, WebRequest request) {
        String message = exception.getMessage();
        ErrorVm errorVm = new ErrorVm(HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST.getReasonPhrase(), message);
        return ResponseEntity.badRequest().body(errorVm);
    }


    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorVm> handleBadRequestException(UnAuthorizedException exception, WebRequest request) {
        String message = exception.getMessage();
        ErrorVm errorVm = new ErrorVm(HttpStatus.FORBIDDEN.toString(), HttpStatus.FORBIDDEN.getReasonPhrase(), message);
        return ResponseEntity.badRequest().body(errorVm);
    }

    private String getServletPath(WebRequest webRequest) {
        ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
        return servletRequest.getRequest().getServletPath();
    }
}
