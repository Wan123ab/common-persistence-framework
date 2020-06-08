package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: 万强
 * @Date: 2019/7/19 16:07
 * @Description: SQL EXISTS操作封装
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseExists {

    private ExistsType existsType;

    private QuerySQL sql;
}
