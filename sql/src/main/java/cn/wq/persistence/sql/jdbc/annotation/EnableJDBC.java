package cn.wq.persistence.sql.jdbc.annotation;

import cn.wq.persistence.sql.jdbc.utils.SpringContextUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @auther 万强
 * @date 2019/11/15 09:44
 * @desc 启用JDBC组件，将把SpringContextUtils放入容器进行管理
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {SpringContextUtils.class})
public @interface EnableJDBC {
}
 
