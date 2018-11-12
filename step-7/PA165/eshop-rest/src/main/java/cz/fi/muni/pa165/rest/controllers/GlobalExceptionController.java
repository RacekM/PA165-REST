package cz.fi.muni.pa165.rest.controllers;

import cz.fi.muni.pa165.rest.ApiError;
import cz.fi.muni.pa165.rest.exceptions.ResourceAlreadyExistingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.LinkedList;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ApiError handleException(ResourceAlreadyExistingException ex){
        ApiError error = new ApiError(Arrays.asList("the requested resource already exists", "other error information"));
        return error;
    }

}
