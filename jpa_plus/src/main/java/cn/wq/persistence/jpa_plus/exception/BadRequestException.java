package cn.wq.persistence.jpa_plus.exception;

import org.springframework.http.HttpStatus;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/7 11:28
 * @desc 错误请求异常
 */
public class BadRequestException extends AbstractHttpException {

  public BadRequestException(String msg) {
    super(msg);
    setHttpStatus(HttpStatus.BAD_REQUEST);
  }
}
