package cn.wq.persistence.sql.jdbc.bean;

import cn.wq.persistence.common.util.EntityUtils;
import cn.wq.persistence.sql.jdbc.function.FieldSupplier;
import lombok.Data;

/**
 * @author: 万强
 * @date: 2019/7/17 09:33
 * @description: SQL Join封装
 * @Version 1.0
 */
@Data
public class Join {

    private StringBuilder joinSQL;

    public Join() {
        joinSQL = new StringBuilder();
    }

    public With with(String tableName) {
        joinSQL.append(" %s ").append(tableName);
        return new With();
    }

    /**
     * 当使用Class作为Join连接的表时，使用表名作为表的别名
     * @param clz
     * @return
     */
    public As with(Class<?> clz) {
        joinSQL.append(" %s ").append(EntityUtils.getTableName(clz)).append(" as ").append(EntityUtils.getTableName(clz));
        return new As();
    }

    public class With {

        public As as(String alias) {
            joinSQL.append(" as ").append(alias);
            return new As();
        }

    }

    public class As extends BaseJoin {

        public As on(String field1, String field2) {
            joinSQL.append(" ON ").append(field1).append(" = ").append(field2).append(" ");
            return this;
        }

        public <T> As on(FieldSupplier<T> supplier1, FieldSupplier<T> supplier2) {
            joinSQL.append(" ON ").append(supplier1.getColumnNameByFieldDefault()).append(" = ").append(supplier2.getColumnNameByFieldDefault()).append(" ");
            return this;
        }

        public As and(String field1, String field2) {
            joinSQL.append(" AND ").append(field1).append(" = ").append(field2).append(" ");
            return this;
        }

        public <T> As and(FieldSupplier<T> supplier1, FieldSupplier<T> supplier2) {
            joinSQL.append(" AND ").append(supplier1.getColumnNameByFieldDefault()).append(" = ").append(supplier2.getColumnNameByFieldDefault()).append(" ");
            return this;
        }
    }

    @Data
    public abstract class BaseJoin {

        private JoinType joinType;

        public String getJoinSQL() {
            return joinSQL.toString();
        }

        public void setJoinType(JoinType joinType) {
            joinSQL = new StringBuilder(String.format(joinSQL.toString(), joinType.getType()));
        }

    }


}
