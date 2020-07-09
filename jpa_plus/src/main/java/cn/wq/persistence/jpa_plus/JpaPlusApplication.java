package cn.wq.persistence.jpa_plus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
/**
 * 使用JPA时，如果实体类或者Dao不在启动类同路径或者子包路径下（如通过maven引入）
 * 需要加上下面引入注解
 * @see EnableJpaRepositories(basePackages = "")
 * @see EntityScan("")
 */
//@EntityScan("cn.wq.persistence.sql.model")
public class JpaPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaPlusApplication.class, args);
    }

}
