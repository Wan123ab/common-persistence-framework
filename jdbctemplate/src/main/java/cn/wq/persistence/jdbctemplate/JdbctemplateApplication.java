package cn.wq.persistence.jdbctemplate;

import cn.wq.persistence.sql.jdbc.annotation.EnableJDBC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJDBC
public class JdbctemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdbctemplateApplication.class, args);
    }

}
