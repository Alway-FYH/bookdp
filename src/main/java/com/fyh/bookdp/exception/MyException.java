package com.fyh.bookdp.exception;

import com.fyh.bookdp.enums.ResultEnum;


/**
 * unchecked 不用处理的异常，交给JVN处理,继承RuntimeException
 * checked 需要自己处理的，继承Exception
 */
public class MyException extends RuntimeException {
    public MyException(String error) {
        super(error);
    }
    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
    }
}
