package cn.z.zai.dto;

import cn.z.zai.common.constant.ResponseCodeConstant;
import cn.z.zai.common.enums.ResponseCodeEnum;
import cn.z.zai.config.i18n.I18nClass;
import cn.z.zai.config.i18n.I18nField;
import cn.z.zai.util.TimeUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 *
 */
@Data
@I18nClass
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -7918273712622117368L;

    private Integer code = 200;

    @I18nField
    private String message = "success";

    private String requestId = "";

    private Long timeStamp  = TimeUtil.currentEpochSecond();

    @I18nField
    private T result;

    public boolean isSuccess() {
    return this.code != null
        && this.code.equals(ResponseCodeConstant.SUCCESS_GLOBAL_DEFAULT.getCode());
    }

    public Response() {}

    public Response(T obj) {
        this.result = obj;
    }

    public Response(String message, T obj) {
        this.message = message;
        this.result = obj;
    }

    private Response(Integer code, String message, T obj) {
        this.code = code;
        this.message = message;
        this.result = obj;
    }

    public static <T> Response<T> success() {
        return new Response<T>();
    }

    public static <T> Response<T> success(T obj) {
        return new Response<T>(obj);
    }

    public static <T> Response<T> success(String message, T obj) {
        return new Response<T>(message, obj);
    }

    public static <T> Response<T> fail() {
        return new Response<>(ResponseCodeConstant.ERROR_GLOBAL_DEFAULT.getCode(),ResponseCodeConstant.ERROR_GLOBAL_DEFAULT.getDesc(),null);
    }

    public static <T> Response<T> fail(Integer code,String message) {
        return new Response<T>(code, message, null);
    }

    public static <T> Response<T> fail(ResponseCodeEnum responseCodeEnum) {
        return new Response<T>(responseCodeEnum.getCode(), responseCodeEnum.getDesc(), null);
    }


    public static <T> Response<T> fail(Integer code, String message, T obj) {
        return new Response<T>(code, message, obj);
    }

}
