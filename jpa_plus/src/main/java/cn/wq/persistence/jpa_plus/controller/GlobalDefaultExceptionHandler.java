package cn.wq.persistence.jpa_plus.controller;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.jpa_plus.exception.AbstractHttpException;
import cn.wq.persistence.jpa_plus.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.event.Level.ERROR;
import static org.slf4j.event.Level.WARN;
import static org.springframework.http.HttpStatus.*;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/7 11:28
 * @desc 全局默认异常处理器
 */
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    //处理系统内置的Exception
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Map<String, Object>> exception(HttpServletRequest request, Throwable ex) {
        return handleError(request, INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeException.class})
    public ResponseEntity<Map<String, Object>> badRequest(HttpServletRequest request,
                                                          ServletException ex) {
        return handleError(request, BAD_REQUEST, ex, WARN);
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Map<String, Object>> restTemplateException(HttpServletRequest request,
                                                                     HttpStatusCodeException ex) {
        return handleError(request, ex.getStatusCode(), ex);
    }

    //处理自定义Exception
    @ExceptionHandler({AbstractHttpException.class})
    public ResponseEntity<Map<String, Object>> badRequest(HttpServletRequest request, AbstractHttpException ex) {
        return handleError(request, ex.getHttpStatus(), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
            HttpServletRequest request, MethodArgumentNotValidException ex) {
        final Optional<ObjectError> firstError = ex.getBindingResult().getAllErrors().stream().findFirst();
        if (firstError.isPresent()) {
            final String firstErrorMessage = firstError.get().getDefaultMessage();
            return handleError(request, BAD_REQUEST, new BadRequestException(firstErrorMessage));
        }
        return handleError(request, BAD_REQUEST, ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {
        return handleError(request, BAD_REQUEST, new BadRequestException(ex.getMessage()));
    }

    /**
     * 处理异常错误信息
     *
     * @param request
     * @param status
     * @param ex
     * @return
     */
    private ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request, HttpStatus status, Throwable ex) {
        return handleError(request, status, ex, ERROR);
    }

    /**
     * 处理异常错误信息
     *
     * @param request
     * @param status
     * @param ex
     * @param logLevel
     * @return
     */
    private ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request, HttpStatus status, Throwable ex, Level logLevel) {
        String message = ex.getMessage();
        printLog(message, ex, logLevel);

        Map<String, Object> errorAttributes = new HashMap<>();
        boolean errorHandled = false;

        if (ex instanceof HttpStatusCodeException) {
            try {
                //try to extract the original error info if it is thrown from apollo programs, e.g. admin service
                errorAttributes = JsonUtils.json2map(((HttpStatusCodeException) ex).getResponseBodyAsString());
                status = ((HttpStatusCodeException) ex).getStatusCode();
                errorHandled = true;
            } catch (Throwable th) {
                //ignore
            }
        }

        if (!errorHandled) {
            errorAttributes.put("status", status.value());
            errorAttributes.put("message", message);
            errorAttributes.put("timestamp",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            errorAttributes.put("exception", ex.getClass().getName());

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(errorAttributes, headers, status);
    }

    //打印日志, 其中logLevel为日志级别: ERROR/WARN/DEBUG/INFO/TRACE
    private void printLog(String message, Throwable ex, Level logLevel) {
        switch (logLevel) {
            case ERROR:
                logger.error(message, ex);
                break;
            case WARN:
                logger.warn(message, ex);
                break;
            case DEBUG:
                logger.debug(message, ex);
                break;
            case INFO:
                logger.info(message, ex);
                break;
            case TRACE:
                logger.trace(message, ex);
                break;
        }

    }

}
