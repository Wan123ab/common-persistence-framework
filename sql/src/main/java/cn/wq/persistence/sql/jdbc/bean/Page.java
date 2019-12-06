package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: 万强
 * @Date: 2019/7/26 下午2:02
 * @Description: Page分页对象
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page implements Serializable {
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 每页记录数
     */
    private int pageSize;

    /**
     * 获取偏移量
     * @return
     */
    public int getOffset() {
        //当前页数小于0
        if (currentPage < 1) {
            return 0;
        }
        return (this.currentPage - 1) * this.pageSize;
    }

}
