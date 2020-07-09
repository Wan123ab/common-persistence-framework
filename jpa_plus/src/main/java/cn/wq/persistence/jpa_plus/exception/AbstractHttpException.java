package cn.wq.persistence.jpa_plus.exception;

import org.springframework.http.HttpStatus;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/7 11:28
 * @desc Http异常基类
 */
public abstract class AbstractHttpException extends RuntimeException{

    private static final long serialVersionUID = -1713129594004951820L;

    protected HttpStatus httpStatus;

    public AbstractHttpException(String msg){
        super(msg);
    }

    public AbstractHttpException(String msg, Exception e){
        super(msg,e);
    }

    protected void setHttpStatus(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
 
