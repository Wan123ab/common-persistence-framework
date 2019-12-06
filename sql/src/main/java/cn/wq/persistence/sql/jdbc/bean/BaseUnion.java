package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: 万强
 * @Date: 2019/7/17 10:55
 * @Desc: SQL Union/UnionAll操作
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseUnion {

    private UnionType unionType;

    private SQL sql;
}
