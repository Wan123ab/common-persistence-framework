package cn.wq.persistence.jpa_plus.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/8 15:26
 * @desc
 */

@Configuration
/*
    开启Spring data的web支持，开启后，Spring容器会注册下面2个组件
    1、org.springframework.data.repository.support.DomainClassConverter
    分页参数解析器
    2.1、org.springframework.data.web.PageableHandlerMethodArgumentResolver
    排序参数解析器
    2.2、org.springframework.data.web.SortHandlerMethodArgumentResolver

 */
@EnableSpringDataWebSupport
public class WebConfig {

    @Bean
    public ReturnValueHandlerFactory returnValueHandlerFactory(){
        return new ReturnValueHandlerFactory();
    }
}
 
