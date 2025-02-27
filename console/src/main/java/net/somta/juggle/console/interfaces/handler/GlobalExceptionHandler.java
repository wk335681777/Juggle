package net.somta.juggle.console.interfaces.handler;

import net.somta.core.exception.BizException;
import net.somta.core.protocol.ResponseDataResult;
import net.somta.juggle.console.domain.flow.definition.enums.FlowDefinitionErrorEnum;
import net.somta.juggle.console.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 * @author husong
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 参数绑定错误
     *
     * @param ex
     * @return
     */
   /* @ExceptionHandler(BindException.class)
    public ResponseDataResult handleBindException(BindException ex) {
        StringJoiner sj = new StringJoiner(";");
        ex.getBindingResult().getFieldErrors().forEach(x -> sj.add(x.getDefaultMessage()));
        return handleBizException(new BizException(WebErrorEnum.ARGUMENT_BIND_ERROR, sj.toString()));
    }*/


    /**
     * 字段校验不通过异常
     *
     * @param ex
     * @return
     */
    /*@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDataResult handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringJoiner sj = new StringJoiner(";");
        ex.getBindingResult().getFieldErrors().forEach(x -> sj.add(x.getDefaultMessage()));
        return handleBizException(new BizException(WebErrorEnum.ARGUMENT_VALID_ERROR, sj.toString()));
    }*/

    /**
     * Controller参数绑定错误
     *
     * @param ex
     * @return
     */
   /* @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseDataResult handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return handleSysException(new SysException(WebErrorEnum.REQUEST_ARGUMENT_ERROR,ex.getMessage()));
    }*/

    /**
     * 处理方法不支持异常
     *
     * @param ex
     * @return
     */
    /*@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseDataResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return handleSysException(new SysException(WebErrorEnum.REQUEST_METHOD_NOT_SUPPORT_ERROR));
    }*/

    /**
     * 处理自定义的openfeign异常
     * @param ex
     * @return
     */
    /*@ExceptionHandler(value = FeignBizException.class)
    public ResponseDataResult handleFeignBizException(FeignBizException ex) {
        loger.error(ex.getErrorCode() + ":" + ex.getMessage(), ex);
        return ResponseDataResult.setErrorResponseResult(ex.getErrorCode(),ex.getMessage());
    }*/

    /**
     * 处理自定义业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler(BizException.class)
    public ResponseDataResult handleBizException(BizException ex) {
        long errorCode = ex.getErrorCode();
        return ResponseDataResult.setErrorResponseResult(errorCode,ex.getErrorMsg());
    }

    /**
     * 处理自定义系统异常
     * @param ex
     * @return
     */
   /* @ExceptionHandler(SysException.class)
    public ResponseDataResult handleSysException(SysException ex) {
        if(ex.getThrowable() != null){
            loger.error(ex.getMessage(), ex);
        }
        return ResponseDataResult.setErrorResponseResult(ex.getErrorCode(),ex.getErrorMsg());
    }*/

    /**
     * 其他未知异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseDataResult handleException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        long errorCode = 1;
        /*if(applicationId != null){
            errorCode = (applicationId * 1000000L) + 999999;
        }*/
        return ResponseDataResult.setErrorResponseResult(errorCode,"系统异常");
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        // 通过异常的错误码和错误信息，获取相应的错误枚举
        FlowDefinitionErrorEnum errorEnum = FlowDefinitionErrorEnum.valueOf(ex.getMessage());  // 获取对应的枚举
        Map<String, Object> response = new HashMap<>();
        response.put("code", errorEnum.getErrorCode());  // 返回业务错误码
        response.put("message", errorEnum.getErrorMsg());  // 返回业务错误信息
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  // 返回 HTTP 400 错误
    }


    }
