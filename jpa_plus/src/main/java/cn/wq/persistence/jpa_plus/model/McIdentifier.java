package cn.wq.persistence.jpa_plus.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mc_identifier")
@Builder
/**
 * 在每个实体类上都加上下面2个注解，否则toString、equals、hashcode方法以及Json序列化都无法拿到基类中的属性
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class McIdentifier extends BaseEntity{

    @Column(name = "Confidence_ID")
    private Integer confidenceID;
    /**
     * 置信度类型
     */
    @Column(name = "Confidence_Type")
    private Integer confidenceType;
    /**
     * 置信度名称
     */
    @Column(name = "Confidence_Type_name")
    private String confidenceTypeName;
    /**
     * 客户id
     */
    @Column(name = "Collection_ID")
    private Integer collectionID;
    /**
     * 属性id
     */
    @Column(name = "Attribute_ID")
    private Integer attributeID;
    /**
     * 属性名称
     */
    @Column(name = "Attribute_Name")
    private String attributeName;

}
