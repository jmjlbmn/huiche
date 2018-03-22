package org.huiche.config;

import org.huiche.core.exception.BaseException;
import org.huiche.core.response.BaseResult;
import org.huiche.core.util.ResultUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Maning
 */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public BaseResult handleException(BaseException e) {
        return ResultUtil.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult handleException(Exception e) {
        String msg = e.getLocalizedMessage();
        return ResultUtil.fail("系统错误" + (null == msg ? "" : ": " + msg));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return error(status, request, "不支持 [" + ex.getMethod() + "] 请求");
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return error(status, request, "未能找到该地址,请确认地址正确");
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return error(status, request, "不支持 [" + ex.getContentType() + "] 格式的请求");
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return error(status, request, "返回响应时数据处理失败,如json转化等");
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return error(status, request, "请求的路径参数不匹配: [" + ex.getVariableName() + "]");
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return error(status, request, "请求参数: " + ex.getParameterName() + "[" + ex.getParameterType() + "]" + "不匹配");
    }

    private ResponseEntity<Object> error(HttpStatus status, WebRequest request, String msg) {
        return new ResponseEntity<>(new BaseResult().setCode(status.value()).setMsg(msg + "," + request.getDescription(false) + "(" + status.getReasonPhrase() + ")"), status);
    }
}
