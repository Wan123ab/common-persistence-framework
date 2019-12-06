package cn.wq.persistence.sql.jdbc.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 此Bean需要通过{@link org.springframework.context.annotation.Import}导入容器才能注入ApplicationContext
 *
 * @auther : 万强
 * @date : 2019/6/3 15:16
 * @desc : SpringContextUtils工具类
 * @version : 1.0
 */
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName){
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> requiredType){
        return applicationContext.getBean(beanName, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType){
        return applicationContext.getBean(requiredType);
    }


}
 
