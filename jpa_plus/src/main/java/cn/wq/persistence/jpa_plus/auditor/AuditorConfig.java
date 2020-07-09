package cn.wq.persistence.jpa_plus.auditor;

import cn.wq.persistence.jpa_plus.repository.BaseRepositoryFactoryBean;
import cn.wq.persistence.jpa_plus.repository.BaseRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/7 09:26
 * @desc JPA 审计配置类
 */
@Configuration
/**
 * 开启JPA审计功能
 */
@EnableJpaAuditing
/**
 * 引入自己扩展的Repository，@EnableJpaRepositories中的2个属性配置都不能少
 */
@EnableJpaRepositories(repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class, basePackageClasses = BaseRepositoryImpl.class)
public class AuditorConfig {

    @Bean
    public MyAuditorAware myAuditorAware() {
        return new MyAuditorAware();
    }
}
 
