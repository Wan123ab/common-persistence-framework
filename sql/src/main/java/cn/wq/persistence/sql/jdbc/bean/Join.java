package cn.wq.persistence.sql.jdbc.bean;

import cn.wq.persistence.common.util.EntityUtils;
import cn.wq.persistence.sql.jdbc.function.FieldSupplier;
import lombok.Data;

/**
 * @Auther: 万强
 * @Date: 2019/7/17 09:33
 * @Description: SQL Join封装
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

    public class As {

        public On on(String field1, String field2) {
            joinSQL.append(" ON ").append(field1).append(" = ").append(field2).append(" ");
            return new On();
        }

        public <T> On on(FieldSupplier<T> supplier1, FieldSupplier<T> supplier2) {
            joinSQL.append(" ON ").append(supplier1.getColumnNameByFieldDefault()).append(" = ").append(supplier2.getColumnNameByFieldDefault()).append(" ");
            return new On();
        }
    }


    public class On extends BaseJoin {

//        public Where where(String key, Object value) {
//            return where(key, "=", value);
//        }
//
//        public Where where(String key, String opt, Object value) {
//            joinSQL.append(" WHERE " + key + " " + opt + " " + value);
//            return new Where();
//        }
//
//        public <T> Where where(FieldSupplier<T> supplier1, FieldSupplier<T> supplier2) {
//            return where(supplier1.getColumnNameByFieldDefault(), supplier2.getColumnNameByFieldDefault());
//        }
//
//        public <T> Where where(FieldSupplier<T> supplier1, String opt, FieldSupplier<T> supplier2) {
//            return where(supplier1.getColumnNameByFieldDefault(), opt, supplier2.getColumnNameByFieldDefault());
//        }

    }

//    public class Where extends BaseJoin {
//
//        public Where or(String key, Object value) {
//            return or(key, "=", value);
//        }
//
//        public Where or(String key,String opt, Object value) {
//            joinSQL.append(" OR " + key + " " + opt + " " + value);
//            return this;
//        }
//
//        public <T> Where or(FieldSupplier<T> supplier1, FieldSupplier<T> supplier2) {
//            return or(supplier1.getColumnNameByFieldDefault(), supplier2.getColumnNameByFieldDefault());
//        }
//
//        public <T> Where or(FieldSupplier<T> supplier1, String opt, FieldSupplier<T> supplier2) {
//            return or(supplier1.getColumnNameByFieldDefault(), opt, supplier2.getColumnNameByFieldDefault());
//        }
//
//        public Where and(String key, Object value) {
//            return and(key, "=", value);
//        }
//
//        public Where and(String key, String opt, Object value) {
//            joinSQL.append(" AND " + key + " " + opt + " " + value);
//            return this;
//        }
//
//        public <T> Where and(FieldSupplier<T> supplier1, FieldSupplier<T> supplier2) {
//            return and(supplier1.getColumnNameByFieldDefault(), supplier2.getColumnNameByFieldDefault());
//        }
//
//        public <T> Where and(FieldSupplier<T> supplier1, String opt, FieldSupplier<T> supplier2) {
//            return and(supplier1.getColumnNameByFieldDefault(), opt, supplier2.getColumnNameByFieldDefault());
//        }
//    }


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
