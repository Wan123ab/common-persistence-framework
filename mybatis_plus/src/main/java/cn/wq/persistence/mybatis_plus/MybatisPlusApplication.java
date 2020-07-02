package cn.wq.persistence.mybatis_plus;

import cn.wq.persistence.sql.jdbc.annotation.EnableJDBC;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"cn.wq.persistence.mybatis_plus.dao"})
@EnableJDBC
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

}
