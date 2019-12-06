package cn.wq.persistence.sql.jdbc.bean;

import cn.wq.persistence.sql.jdbc.function.FieldSupplier;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排序参数
 */
@Data
@NoArgsConstructor
public class Sort {

    public static final String DESC = "DESC";

    public static final String ASC = "ASC";

    private String key;

    /**
     * 默认DESC
     */
    private String type = DESC;

    public Sort(String key) {
        this.key = key;
    }

    public Sort(String key, String type) {
        this.key = key;
        this.type = type;
    }

    public <T> Sort(FieldSupplier<T> supplier) {
        this.key = supplier.getColumnNameByFieldDefault();
    }

    public <T> Sort(FieldSupplier<T> supplier, String type) {
        this.key = supplier.getColumnNameByFieldDefault();
        this.type = type;

    }

}
