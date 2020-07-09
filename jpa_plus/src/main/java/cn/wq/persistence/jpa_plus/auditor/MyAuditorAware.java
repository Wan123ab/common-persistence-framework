package cn.wq.persistence.jpa_plus.auditor;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import java.util.Optional;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/7 09:26
 * @desc 实现AuditorAware用于告诉JPA当前用户是谁
 *
 * @see CreatedBy
 */
public class MyAuditorAware implements AuditorAware<Long> {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        /**
         * 获取当前用户
         * 1、如果集成了Spring Security，从Security中获取
         * 2、如果集成了Shiro，从Shiro中获取
         * 3、从request或session中获取
         */
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        Long userId = (Long) servletRequestAttributes.getRequest().getSession().getAttribute("userId");
//        return Optional.of(userId);

        return Optional.empty();
    }
}
 
