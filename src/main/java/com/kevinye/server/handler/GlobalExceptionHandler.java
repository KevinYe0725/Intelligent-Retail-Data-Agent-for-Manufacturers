package com.kevinye.server.handler;

import com.kevinye.pojo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(RuntimeException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

//    //SQl异常
//    @ExceptionHandler
//    public Result exceptionHandle(SQLIntegrityConstraintViolationException ex){
//        String message = ex.getMessage();
//        if(message.contains("Duplicate entry")){
//            String[] s = message.split(" ");
//            String username = s[2];
//            String msg =username+ MessageConstant.ALREADY_EXISTS;
//            return Result.error(msg);
//        }else {
//            return Result.error(MessageConstant.UNKNOWN_ERROR);
//        }
//    }


}
