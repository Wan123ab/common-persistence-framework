package cn.wq.persistence.jpa_plus.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/8 18:13
 * @desc controller返回值处理器工厂，用于配置自定义的返回值处理器
 */
public class ReturnValueHandlerFactory implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * 重新设置requestMappingHandlerAdapter中的ReturnValueHandlers，替换掉内置的RequestResponseBodyMethodProcessor
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        /*注意：此处需要转换为ArrayList再执行后续操作，否则后面的handler.set将会报错*/
        ArrayList<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(Objects.requireNonNull(requestMappingHandlerAdapter.getReturnValueHandlers()));
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if(handler instanceof RequestResponseBodyMethodProcessor){
                /*将内置的RequestResponseBodyMethodProcessor替换为本自定义的handler*/
                handlers.set(handlers.indexOf(handler), new CustomReturnValueHandler(handler));
            }
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(handlers);
    }
}
 
