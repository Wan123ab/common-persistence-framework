package cn.wq.persistence.sql.table;

import lombok.Data;

import java.util.Set;

/**
 * @author 万强
 * @version 1.0
 * @date 2019/11/23 16:17
 * @desc 实体类表对象
 */
@Data
public class Table<T> {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 所属schema
     */
    private String schema;

    /**
     * 包含的column
     */
    private Set<TableColumn<T>> tableColumns;

}
 
