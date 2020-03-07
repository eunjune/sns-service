package com.github.prgrms.social.api.controller;

import com.amazonaws.AmazonServiceException;
import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.error.ServiceRuntimeException;
import com.github.prgrms.social.api.error.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.github.prgrms.social.api.model.api.response.ApiResult.ERROR;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {

    private ResponseEntity<?> newResponse(Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(throwable, status), headers, status);
    }

    // TODO REST API 처리 중 발생한 예외를 catch 하고, 로그를 남기고, ApiResult를 사용해 오류 응답을 전달

    @ExceptionHandler(value = {
            IllegalStateException.class,
            IllegalArgumentException.class,
            TypeMismatchException.class,
            MissingServletRequestParameterException.class,
            JSONException.class
    })
    public ResponseEntity<?> handleBadRequestException(Exception e){
        log.debug("Bad request exception occurred: {}", e.getMessage(), e);
        return newResponse(e,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<?> handleHttpMediaTypeException(Exception e) {
        return newResponse(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotAllowedException(Exception e) {
        return newResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {
            ServiceRuntimeException.class,
            NotFoundException.class,
            UnauthorizedException.class,
            AmazonServiceException.class
    })
    public ResponseEntity<?> handleServiceRuntimeException(Exception e){
        if(e instanceof NotFoundException)
            return newResponse(e, HttpStatus.NOT_FOUND);
        if(e instanceof UnauthorizedException)
            return newResponse(e,HttpStatus.UNAUTHORIZED);

        log.warn("Unexpected service exception occurred: {}", e.getMessage(), e);
        return newResponse(e,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
