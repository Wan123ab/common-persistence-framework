package cn.wq.persistence.jpa_plus.web;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.*;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/8 17:44
 * @desc 自定义返回值处理器，用于对Spring内置的RequestResponseBodyMethodProcessor进行进一步的封装
 */
public class CustomReturnValueHandler implements HandlerMethodReturnValueHandler{

    private HandlerMethodReturnValueHandler handler;

    public CustomReturnValueHandler(HandlerMethodReturnValueHandler handler) {
        this.handler = handler;
    }

    /**
     * 设置支持的返回值类型，与RequestResponseBodyMethodProcessor一致
     * @param returnType
     * @return
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return handler.supportsReturnType(returnType);
    }

    /**
     * 处理返回值，经过进一步封装后再交给RequestResponseBodyMethodProcessor处理
     * @param returnValue 返回值
     * @param returnType 返回值类型
     * @param mavContainer 当前request的ModelAndViewContainer
     * @param webRequest 当前request
     * @throws Exception 异常
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        if (returnValue != null) {
            if (returnValue instanceof ResponseEntity) {
                handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            } else {
                Map<String, Object> modelMap = new HashMap<>();
                if (returnValue instanceof PageImpl<?>) {
                    PageImpl<?> page = (PageImpl<?>)returnValue;
                    modelMap.put("data", page.getContent());
                    modelMap.put("total", page.getTotalElements());
                    modelMap.put("currentPage", page.getPageable().getPageNumber());
                    modelMap.put("pageSize", page.getSize());
                    modelMap.put("totalPages", page.getTotalPages());

                } else if (returnValue instanceof Collection<?>) {
                    modelMap.put("data", returnValue);
                    modelMap.put("total", ((List<?>)returnValue).size());
                } else {
                    modelMap.put("data", returnValue);
                }
                modelMap.put("code", 200);
                modelMap.put("errorMsg", "");
                modelMap.put("requestDate", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                handler.handleReturnValue(modelMap, returnType, mavContainer, webRequest);
            }
        } else {
            handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        }
    }

}
 
