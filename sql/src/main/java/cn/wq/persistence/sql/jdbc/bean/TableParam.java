package cn.wq.persistence.sql.jdbc.bean;

import cn.wq.persistence.common.util.EntityUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询涉及表参数
 */
@Data
@NoArgsConstructor
public class TableParam {

    private Class clz;

    private String tableName;

    private String alias;

    public TableParam(Class<?> clz){
        this.clz = clz;
        this.tableName = EntityUtils.getTableName(clz);
        this.alias = EntityUtils.getTableName(clz);
    }

    public TableParam(Class<?> clz, String alias){
        this.clz = clz;
        this.alias = alias;
    }
}
