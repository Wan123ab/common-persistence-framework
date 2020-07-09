package cn.wq.persistence.jpa_plus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/3 14:14
 * @desc
 */
@Data
/*添加全参构造器，便于JPA将查询结果进行映射*/
@AllArgsConstructor
public class McIdentifierDto {

    private Integer confidenceID;
    /**
     * 置信度类型
     */
    private Integer confidenceType;
    /**
     * 置信度名称
     */
    private String confidenceTypeName;
    /**
     * 客户id
     */
    private Integer collectionID;
    /**
     * 属性id
     */
    private Integer attributeID;
    /**
     * 属性名称
     */
    private String attributeName;

}
 
