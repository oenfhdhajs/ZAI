package cn.z.zai.config;

import cn.z.zai.dto.Response;
import cn.z.zai.exception.BaseException;
import cn.z.zai.common.constant.ResponseCodeConstant;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;


@Slf4j
@Order(999)
@ResponseBody
@RestControllerAdvice
public class ExceptionHandlerConfiguration {


    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Response<?> dealMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("PARAM ERROR : {}",e.getMessage());
        return Response.fail(ResponseCodeConstant.ERROR_GLOBAL_PARAM.getCode(),e.getMessage());
    }

    /**
     * Check Param Exception
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Response<?> dealConstraintViolation(ConstraintViolationException exception) {
        StringBuilder message = new StringBuilder();
        for (final ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            message.append(((PathImpl) violation.getPropertyPath()).getLeafNode().getName())
                    .append(": ").append(violation.getMessage());
        }
        log.warn("PARAM ERROR : {}",message);
        return Response.fail(ResponseCodeConstant.ERROR_GLOBAL_PARAM.getCode(),message.toString());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Response<?> dealMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String message = "PARAM ERROR";
        if (exception.getBindingResult().getFieldError() != null) {
            message = exception.getBindingResult().getFieldError().getDefaultMessage();
        }
        log.warn("PARAM ERROR : {}",message);
        return Response.fail(ResponseCodeConstant.ERROR_GLOBAL_PARAM.getCode(),message);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Response<?> dealMethodArgumentNotValid(ValidationException exception) {
        String message = exception.getMessage();
       log.warn("PARAM ERROR : {}",message);
        return Response.fail(ResponseCodeConstant.ERROR_GLOBAL_PARAM.getCode(),message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Response<?> dealHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("request body invalid",exception);
        return Response.fail(ResponseCodeConstant.ERROR_GLOBAL_PARAM.getCode(),"missing body");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Response<?> dealIllegalArgumentException(IllegalArgumentException exception) {
        log.error("request body parse error : ",exception);
        return Response.fail(ResponseCodeConstant.ERROR_GLOBAL_PARAM.getCode(),ResponseCodeConstant.ERROR_GLOBAL_PARAM.getDesc());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Response<?> delBindException(BindException exception) {
        for (ObjectError fieldError : exception.getAllErrors()) {
            return Response.fail(ResponseCodeConstant.ERROR_GLOBAL_PARAM.getCode(),fieldError.getDefaultMessage());
        }
        log.warn("PARAM ERROR : {}",exception.getMessage());
        return Response.fail(ResponseCodeConstant.ERROR_GLOBAL_PARAM.getCode(),exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    public Response<?> dealHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return Response.fail(ResponseCodeConstant.ERROR_GLOBAL_METHOD_NOT_ALLOWED);
    }

    /**
     * Error Exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BaseException.class)
    public Response<?> dealErrorException(BaseException e) {
        log.warn("PARAM ERROR : {}",e.getMessage());
        return Response.fail(e.getCode(),e.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> dealException(Exception e) {
        log.error("Unknown Exception message {}", e.getMessage());
        log.error("Unknown Exception", e);
        return Response.fail(ResponseCodeConstant.ERROR_GLOBAL_DEFAULT);
    }


}
