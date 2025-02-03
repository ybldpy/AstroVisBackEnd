package com.astrovis.astrovisbackend.handler;


import com.astrovis.astrovisbackend.commons.Response;
import com.astrovis.astrovisbackend.commons.ResponseConstants;
import com.astrovis.astrovisbackend.exception.IllegalParamException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RequestExceptionHandler {

    // 处理 Bean Validation 异常，如 @Valid 校验失败
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public Response handleValidationException(MethodArgumentNotValidException ex) {
        // 获取第一个校验错误
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return Response.error(ResponseConstants.CODE_BAD_REQUEST,errorMessage);
    }



    // 处理类型转换异常（如传入的参数类型不匹配）
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Response handleTypeMismatchException(HttpMessageNotReadableException ex) {
        String badRequest = "Bad request";
        return Response.error(ResponseConstants.CODE_BAD_REQUEST,badRequest);
    }

    // 处理其他 Bad Request 错误
    @ExceptionHandler(IllegalParamException.class)
    @ResponseBody
    public Response handleIllegalParamsException(IllegalParamException ex) {
        return Response.error(ResponseConstants.CODE_BAD_REQUEST,ex.getMessage());
    }


    // 处理所有其他异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleGeneralException(Exception ex) {
        return new Response(500,ex.getMessage(),null);
    }
}

