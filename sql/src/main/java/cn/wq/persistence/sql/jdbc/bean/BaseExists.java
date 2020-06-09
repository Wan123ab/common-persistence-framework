package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 万强
 * @date: 2019/7/19 16:07
 * @description: SQL EXISTS操作封装
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
