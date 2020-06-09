package cn.wq.persistence.jpa.vo;

import lombok.Data;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/6/9 10:30
 * @desc
 */
@Data
public class McValueFeatureRelVO {

    private int featureId;

    /**
     * 要素类型1、link 2、lane 3、mark 4、LinkNode 5、object
     */
    private int featureType;

    /**
     * 要素状态：0、无 1、施工 2、compliant 3、人工识别变化 4、人工识别确认 5、众包识别变化 6、众包识别未变化 7、typeAB更新确认
     * 8、typeAB更新变化 9、情报识别变化 10、情报识别未变化 11、问题反馈 12、问题确认 99、其他
     */
    private int featureStatus;

    /**
     * 置信度编号
     */
    private int confidenceId;

    /**
     * 当confidence_id为[101,160]时
     * 记录位置精度的方差
     * 单位：米
     * 当confidence_id为其他数值时
     * 记录当前要素的置信度
     * 值域：(0,1000)
     */
    private double confidenceValue;

    /**
     * 要素更新时间
     */
    private String featureUpdateTime;

    /**
     * 要素验证时间
     */
    private String featureCheckTime;

    /**
     * 额外信息
     */
    private String memo;

    /**
     * 要素关联的link
     */
    private int linkRelId;

    /**
     * 记录link所在图幅
     */
    private String linkRelMesh;
}
 
