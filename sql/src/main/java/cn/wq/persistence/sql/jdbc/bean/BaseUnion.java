package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 万强
 * @date: 2019/7/17 10:55
 * @desc: SQL Union/UnionAll操作
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseUnion {

    private UnionType unionType;

    private QuerySQL sql;
}
