package com.itdev.http.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = "com.itdev.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
}
