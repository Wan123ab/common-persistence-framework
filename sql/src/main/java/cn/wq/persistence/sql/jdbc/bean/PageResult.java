package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: 万强
 * @Date: 2019/7/17 11:09
 * @Description: 分页查询返回结果
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    /**
     * 总数量
     */
    private BigInteger total;
    /**
     * 返回对象类型列表
     */
    private List<T> list;

    /**
     * 返回空的分页查询结果
     * @return
     */
    public static PageResult empty() {
        return new PageResult(BigInteger.valueOf(0), Collections.EMPTY_LIST);
    }

}
