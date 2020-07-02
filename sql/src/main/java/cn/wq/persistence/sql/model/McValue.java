package cn.wq.persistence.sql.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table( name = "mc_value")
public class McValue extends Model<McValue> {

    /**
     * 要素Id
     */
    @Column(name = "feature_id")
    private int featureId;

    /**
     * 要素类型1、link 2、lane 3、mark 4、LinkNode 5、object
     */
    @Column(name = "feature_type")
    private int featureType;

    /**
     * 要素状态：0、无 1、施工 2、compliant 3、人工识别变化 4、人工识别确认 5、众包识别变化 6、众包识别未变化 7、typeAB更新确认
     * 8、typeAB更新变化 9、情报识别变化 10、情报识别未变化 11、问题反馈 12、问题确认 99、其他
     */
    @Column(name = "feature_status")
    private int featureStatus;

    /**
     * 置信度编号
     */
    @Column(name = "confidence_id")
    private int confidenceId;

    /**
     * 当confidence_id为[101,160]时
     * 记录位置精度的方差
     * 单位：米
     * 当confidence_id为其他数值时
     * 记录当前要素的置信度
     * 值域：(0,1000)
     */
    @Column(name = "confidence_value")
    private double confidenceValue;

    /**
     * 要素更新时间
     */
    @Column(name = "feature_updatetime")
    private String featureUpdateTime;

    /**
     * 要素验证时间
     */
    @Column(name = "feature_checktime")
    private String featureCheckTime;

    /**
     * 额外信息
     */
    @Column(name = "memo")
    private String memo;

    @Override
    @JsonIgnore
    public Serializable getId() {
        return null;
    }

}
