package com.ren.common.utils.advice;

import com.ren.common.utils.exception.LyException;
import com.ren.common.utils.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 通用异常处理对象, 它会处理所有的 Controller
 */
@ControllerAdvice
public class CommonExceptionHandler  {

    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handleException(LyException lyException){
        ExceptionResult er = new ExceptionResult(lyException);
        return ResponseEntity.status(er.getStatus()).body(er);
    }
}
