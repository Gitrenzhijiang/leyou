package com.ren.common.utils.exception;

import com.ren.common.utils.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 控制层通用异常对象
 */
@Data
@AllArgsConstructor
public class LyException extends RuntimeException {
    private ExceptionEnum exceptionEnum;

}
