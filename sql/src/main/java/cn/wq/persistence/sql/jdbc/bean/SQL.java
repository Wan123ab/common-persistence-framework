package cn.wq.persistence.sql.jdbc.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SQL封装类，包含常用的查询操作，设计初衷是为了更方便地使用面向对象的方式进行SQL查询
 * 对于非常复杂的查询操作，仍然建议使用原生SQL进行操作
 * 以便于后期维护和调优
 *
 * @author : 万强
 * @version 1.0
 * @date : 2019-7-16 下午8:11
 * @desc : SQL封装类
 */
@Data
@NoArgsConstructor
@SuppressWarnings({"serial"})
public class SQL<Child extends SQL> extends AbstractCriteria<Child> {

    /**
     * SQL操作类型
     *
     * @see SqlType
     */
    protected SqlType sqlType;

    public QuerySQL buildQuerySQL() {
        return new QuerySQL();
    }

    public UpdateSQL buildUpdateSQL() {
        return new UpdateSQL();
    }


}
