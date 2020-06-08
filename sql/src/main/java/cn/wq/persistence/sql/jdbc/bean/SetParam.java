package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 万强
 * @version 1.0
 * @date 2019/12/13 09:50
 * @desc updateSQL中的set参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetParam {

    private String key;

    private Object value;
}
 
