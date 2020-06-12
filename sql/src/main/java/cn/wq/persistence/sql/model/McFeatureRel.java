package cn.wq.persistence.sql.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table( name = "mc_feature_rel")
public class McFeatureRel extends Model<McFeatureRel, Integer> {

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
     * 要素所在图幅
     */
    @Column(name = "feature_mesh")
    private String featureMesh;

    /**
     * 要素关联的link
     */
    @Column(name = "link_rel_id")
    private int linkRelId;

    /**
     * 记录link所在图幅
     */
    @Column(name = "link_rel_mesh")
    private String linkRelMesh;

    /**
     * 额外信息
     */
    @Column(name = "memo")
    private String memo;

    @Override
    public Integer getId() {
        return null;
    }
}
