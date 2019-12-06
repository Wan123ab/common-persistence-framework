package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : 万强
 * @date : 2019/7/25 19:39
 * @desc : Criteria包装类，记录SQL中拼接Criteria的类型以及拼接时whereParam所处下标，以便于后面解析SQL
 * @version : 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaWrapper {

    private CriteriaType criteriaType;

    private Criteria criteria;

}
 
