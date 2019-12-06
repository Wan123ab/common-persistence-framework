package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * where条件参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WhereParam {

    /**
     * 是否使用占位符，若为true将使用？，否则原样输出
     * @Builder.Default 表示支持默认属性
     */
    @Builder.Default
    private Boolean usePlaceholder = true;

    private String key;

    private String opt;

    private Object value;


}
