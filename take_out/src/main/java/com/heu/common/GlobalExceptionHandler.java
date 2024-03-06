package com.heu.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.heu.exception.BaseException;
import com.heu.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice (annotations = {Controller.class, RestController.class})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){

        log.error(exception.getMessage());
        if(exception.getMessage().contains("Duplicate entry")){
            String[] split = exception.getMessage().split(" ");
            String msg = split[2] + "已存在，请换一个账号注册";
            return R.error(msg);
        }
        return R.error("未知错误");
    }


    @ExceptionHandler(CustomException.class)
    public R<String> CustomExceptionHandler(CustomException exception){
        log.error(exception.getMessage());
        return R.error(exception.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public R<String> ExceptionHandler(BaseException exception){
        log.error(exception.getMessage());
        return R.error(exception.getMessage());
    }
}
