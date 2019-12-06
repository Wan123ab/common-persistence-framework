package cn.wq.persistence.sql.table;

import lombok.Builder;
import lombok.Data;

/**
 * @author 万强
 * @version 1.0
 * @date 2019/11/23 16:16
 * @desc Table列对象
 */
@Data
@Builder
public class TableColumn<T> {

    /**
     * 是否为主键
     */
    private boolean isPK;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 对应field字段名，映射需要用到
     */
    private String fieldName;

    /**
     * 关联的field
     */
    private TableField<T> tableField;

    /**
     * 对应field的java类型
     */
    private Class<?> javaType;

    /**
     * 是否可插入
     */
    private boolean insertAble;

    /**
     * 是否可更新
     */
    private boolean updateAble;

    public boolean isNotPK(){
        return !isPK;
    }

}
 
