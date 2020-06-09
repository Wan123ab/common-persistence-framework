package cn.wq.persistence.jpa;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.jpa.model.McFeatureRel;
import cn.wq.persistence.jpa.model.McIdentifier;
import cn.wq.persistence.jpa.model.McValue;
import cn.wq.persistence.jpa.vo.McValueFeatureRelVO;
import cn.wq.persistence.sql.dao.BaseDao;
import cn.wq.persistence.sql.jdbc.bean.Criteria;
import cn.wq.persistence.sql.jdbc.bean.Join;
import cn.wq.persistence.sql.jdbc.bean.QuerySQL;
import cn.wq.persistence.sql.jdbc.bean.SQL;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @version 1.0
 * @author 万强
 * @date 2019/11/15 17:59
 * @desc
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class CriteriaTest {

    @Autowired
    private BaseDao<Integer> baseDao;

    @Test
    public void testBaseDao(){
        McIdentifier mcIdentifier = new McIdentifier();
        Criteria criteria = new Criteria().where(mcIdentifier::getCollectionID, 1);
        try {
            List<McIdentifier> mcIdentifiers = baseDao.queryWithCriteria(criteria, McIdentifier.class);
            System.out.println(JsonUtils.obj2Json(mcIdentifiers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAR(){
        McIdentifier mcIdentifier = new McIdentifier();
        Criteria criteria = new Criteria().where(mcIdentifier::getCollectionID, 1);
        try {
            List<McIdentifier> mcIdentifiers = mcIdentifier.queryWithCriteria(criteria);
            System.out.println(JsonUtils.obj2Json(mcIdentifiers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关联mc_value和mc_feature_rel查询返回McValueFeatureRelVO
     *
     * SELECT mc_value.feature_id          as featureId,
     *        mc_value.feature_type        as featureType,
     *        mc_value.feature_status      as featureStatus,
     *        mc_value.confidence_id       as confidenceId,
     *        mc_value.confidence_value    as confidenceValue,
     *        mc_value.feature_updatetime  as featureUpdateTime,
     *        mc_value.feature_checktime   as featureCheckTime,
     *        mc_value.memo                as memo,
     *        mc_feature_rel.link_rel_id   as linkRelId,
     *        mc_feature_rel.link_rel_mesh as linkRelMesh
     * FROM mc_value as mc_value,
     *      mc_feature_rel as mc_feature_rel
     * WHERE mc_value.feature_id = mc_feature_rel.feature_id
     *   AND mc_value.feature_type = mc_feature_rel.feature_type
     *   AND mc_feature_rel.link_rel_mesh = '26482653'
     *   AND mc_feature_rel.link_rel_id = 524658
     */
    @Test
    public void queryMcValue1(){
        McValue mcValue = new McValue();
        McFeatureRel mcFeatureRel = new McFeatureRel();
        QuerySQL sql = new SQL<>().buildQuerySQL()
                .select(McValue::new, mcFeatureRel::getLinkRelId, mcFeatureRel::getLinkRelMesh)
                .from(McValue.class, McFeatureRel.class)
                .where(mcValue::getFeatureId, mcFeatureRel::getFeatureId)
                .and(mcValue::getFeatureType, mcFeatureRel::getFeatureType)
                .and(mcFeatureRel::getLinkRelMesh, "26482653")
                .and(mcFeatureRel::getLinkRelId, 524658);

        try {
            List<McValueFeatureRelVO> mcValueFeatureRelVOS = baseDao.queryWithSql(sql, McValueFeatureRelVO.class);
            System.out.println("记录总数count：" + mcValueFeatureRelVOS.size());
            System.out.println("记录result：" + JsonUtils.obj2Json(mcValueFeatureRelVOS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关联mc_value和mc_feature_rel查询返回McValueFeatureRelVO
     *
     * SELECT mc_value.feature_id          as featureId,
     *        mc_value.feature_type        as featureType,
     *        mc_value.feature_status      as featureStatus,
     *        mc_value.confidence_id       as confidenceId,
     *        mc_value.confidence_value    as confidenceValue,
     *        mc_value.feature_updatetime  as featureUpdateTime,
     *        mc_value.feature_checktime   as featureCheckTime,
     *        mc_value.memo                as memo,
     *        mc_feature_rel.link_rel_id   as linkRelId,
     *        mc_feature_rel.link_rel_mesh as linkRelMesh
     * FROM mc_value as mc_value
     *          INNER JOIN mc_feature_rel as mc_feature_rel
     * ON mc_value.feature_id = mc_feature_rel.feature_id
     *   AND mc_value.feature_type = mc_feature_rel.feature_type
     * WHERE  mc_feature_rel.link_rel_mesh = '26482653'
     *   AND mc_feature_rel.link_rel_id = 524658;
     */
    @Test
    public void queryMcValue2(){
        McValue mcValue = new McValue();
        McFeatureRel mcFeatureRel = new McFeatureRel();
        QuerySQL sql = new SQL<>().buildQuerySQL()
                .select(McValue::new, mcFeatureRel::getLinkRelId, mcFeatureRel::getLinkRelMesh)
                .from(McValue.class)
                .innerJoin(new Join().with(McFeatureRel.class).on(mcValue::getFeatureId, mcFeatureRel::getFeatureId).and(mcValue::getFeatureType, mcFeatureRel::getFeatureType))
                .where(mcFeatureRel::getLinkRelMesh, "26482653")
                .and(mcFeatureRel::getLinkRelId, 524658);

        try {
            List<McValueFeatureRelVO> mcValueFeatureRelVOS = baseDao.queryWithSql(sql, McValueFeatureRelVO.class);
            System.out.println("记录总数count：" + mcValueFeatureRelVOS.size());
            System.out.println("记录result：" + JsonUtils.obj2Json(mcValueFeatureRelVOS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
 
