package com.ren.common.utils.vo;

import com.ren.common.utils.enums.ExceptionEnum;
import com.ren.common.utils.exception.LyException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 返回给客户端的对象, 其包含出错原因, 状态码
 */
@Data
public class ExceptionResult {

    private final int status;
    private final String message;
    private final long timeStamp;

    public ExceptionResult(LyException lyException) {
        ExceptionEnum exceptionEnum = lyException.getExceptionEnum();
        this.status = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
        this.timeStamp = System.currentTimeMillis();
    }
}
