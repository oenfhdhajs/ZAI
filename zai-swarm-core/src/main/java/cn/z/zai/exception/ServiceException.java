package cn.z.zai.exception;

import cn.z.zai.common.enums.ResponseCodeEnum;


public class ServiceException extends BaseException{

    public ServiceException(ResponseCodeEnum codeEnum) {
        super(codeEnum.getCode(), codeEnum.getDesc());
    }

    public ServiceException(String message) {
        super(message);
    }
}
