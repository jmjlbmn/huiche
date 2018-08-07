package org.huiche.config;

import lombok.extern.slf4j.Slf4j;
import org.huiche.core.exception.HuiCheError;
import org.huiche.core.exception.HuiCheException;
import org.huiche.core.util.LogUtil;
import org.huiche.web.response.BaseResult;
import org.huiche.web.util.ResultUtil;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常处理
 *
 * @author Maning
 */
@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    /**
     * 捕获未处理异常
     *
     * @param e 异常
     * @return json
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult handleException(Exception e) {
        String msg = e.getLocalizedMessage();
        LogUtil.error(e);
        if (e instanceof HuiCheException) {
            return ResultUtil.fail(((HuiCheException) e).getCode(), msg);
        } else {
            return BaseResult.of(HuiCheError.ERROR).setMsg("系统错误" + (null == msg ? "" : ": " + msg));
        }

    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@NonNull HttpRequestMethodNotSupportedException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "不支持 [" + ex.getMethod() + "] 请求");
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleNoHandlerFoundException(@NonNull NoHandlerFoundException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "未能找到该地址,请确认地址正确");
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(@NonNull HttpMediaTypeNotSupportedException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        String contentType = "contentType";
        if (null != ex.getContentType()) {
            contentType = ex.getContentType().toString();
        }
        LogUtil.error(ex);
        return error(status, request, "请求不被支持 [" + contentType + "]");
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(@NonNull HttpMediaTypeNotAcceptableException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "返回响应时数据处理失败,如json转化等");
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingPathVariable(@NonNull MissingPathVariableException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "请求的路径参数不匹配: [" + ex.getVariableName() + "]");
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestParameter(@NonNull MissingServletRequestParameterException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "请求参数: " + ex.getParameterName() + "[" + ex.getParameterType() + "]" + "不匹配");
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "HttpMessage不可读 " + ex.getLocalizedMessage());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotWritable(@NonNull HttpMessageNotWritableException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "HttpMessage不可写 " + ex.getLocalizedMessage());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "方法参数无效 " + ex.getLocalizedMessage());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestPart(@NonNull MissingServletRequestPartException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "请求不完整 " + ex.getLocalizedMessage());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleBindException(@NonNull BindException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "请求参数验证不通过 " + ex.getLocalizedMessage());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(@NonNull AsyncRequestTimeoutException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "异步请求超时 " + ex.getLocalizedMessage());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleServletRequestBindingException(@NonNull ServletRequestBindingException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "请求绑定出错 " + ex.getLocalizedMessage());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        return error(status, request, "未处理的服务器内部错误,请稍后再试或联系工作人员 " + ex.getLocalizedMessage());
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleConversionNotSupported(@NonNull ConversionNotSupportedException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException ex, @Nullable HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        LogUtil.error(ex);
        StringBuilder msg = new StringBuilder("参数类型不匹配且转换失败");
        if (null != ex.getPropertyName()) {
            msg.append(" 属性:").append(ex.getPropertyName());
        }
        if (null != ex.getRequiredType()) {
            msg.append(" 需要是[ ").append(ex.getRequiredType().getSimpleName()).append(" ]类型");
        }
        Object value = ex.getValue();
        if (null != value) {
            msg.append(" 当前传入值为:[ ").append(value).append(" ](").append(value.getClass().getSimpleName()).append(" 类型)");
        }
        return error(status, request, msg.toString());
    }

    /**
     * 异常处理
     *
     * @param status  http状态
     * @param request 请求
     * @param msg     错误消息
     * @return 响应
     */
    private ResponseEntity<Object> error(HttpStatus status, WebRequest request, String msg) {
        return new ResponseEntity<>(new BaseResult().setCode(status.value()).setMsg(msg + "," + request.getDescription(false) + " (" + status.getReasonPhrase() + ")"), status);
    }
}
