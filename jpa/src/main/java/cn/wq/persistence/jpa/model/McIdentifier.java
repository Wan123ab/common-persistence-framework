package cn.wq.persistence.jpa.model;

import cn.wq.persistence.sql.model.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "mc_identifier")
public class McIdentifier extends Model<McIdentifier, Integer> {
    /**
     * ID
     * Id表示该字段对应数据库表的主键id
     * GeneratedValue中strategy表示使用数据库自带的主键生成策略
     * GeneratedValue中generator配置为"JDBC",在数据插入完毕之后,会自动将主键id填充到实体类中.类似普通mapper.xml中配置的selectKey标签
     */
    @Id
    @Column(name = "Confidence_ID", insertable = false)
    private Integer ConfidenceID;
    /**
     * 置信度类型
     */
    @Column(name = "Confidence_Type")
    private Integer ConfidenceType;
    /**
     * 置信度名称
     */
    @Column(name = "Confidence_Type_name")
    private String ConfidenceTypeName;
    /**
     * 客户id
     */
    @Column(name = "Collection_ID")
    private Integer CollectionID;
    /**
     * 属性id
     */
    @Column(name = "Attribute_ID")
    private Integer AttributeID;
    /**
     * 属性名称
     */
    @Column(name = "Attribute_Name")
    private String AttributeName;

    @Override
    @JsonIgnore
    public Integer getId() {
        return ConfidenceID;
    }
}
