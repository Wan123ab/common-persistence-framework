package cn.wq.persistence.mybatis_plus;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.mybatis_plus.dao.CommonMapper;
import cn.wq.persistence.mybatis_plus.service.McValueService;
import cn.wq.persistence.sql.jdbc.bean.Join;
import cn.wq.persistence.sql.jdbc.bean.QuerySQL;
import cn.wq.persistence.sql.jdbc.bean.SQL;
import cn.wq.persistence.sql.jdbc.bean.SQLWrapper;
import cn.wq.persistence.sql.jdbc.utils.SqlBuilderUtils;
import cn.wq.persistence.sql.model.McFeatureRel;
import cn.wq.persistence.sql.model.McValue;
import cn.wq.persistence.sql.vo.McValueFeatureRelVO;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MybatisPlusApplicationTests {

    @Resource
    private McValueService mcValueService;

    @Resource
    private CommonMapper commonMapper;

    @Test
    public void test() {
        McValue mcValue = new McValue();
        McFeatureRel mcFeatureRel = new McFeatureRel();
        QuerySQL sql = new SQL<>().buildQuerySQL()
                .select(McValue::new, mcFeatureRel::getLinkRelId, mcFeatureRel::getLinkRelMesh)
                .from(McValue.class)
                .innerJoin(new Join().with(McFeatureRel.class).on(mcValue::getFeatureId, mcFeatureRel::getFeatureId).and(mcValue::getFeatureType, mcFeatureRel::getFeatureType))
                .where(mcFeatureRel::getLinkRelMesh, "26482653")
                .and(mcFeatureRel::getLinkRelId, 524658)
                .andBetween(mcValue::getConfidenceId, 101, 160)
                .and(mcValue::getConfidenceId, "in", Lists.newArrayList(101, 160));

        SQLWrapper sqlWrapper = SqlBuilderUtils.buildSql(sql);
        try {
            List<Map<String, Object>> mapList = commonMapper.executeQuery(sqlWrapper.resolve2SQL());
            List<McValueFeatureRelVO> mcValueFeatureRelVOS = mapList.stream().map(map -> JsonUtils.map2Obj(map, McValueFeatureRelVO.class)).collect(Collectors.toList());
            System.out.println(JsonUtils.obj2Json(mcValueFeatureRelVOS));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        McValue mcValue = new McValue();
        McFeatureRel mcFeatureRel = new McFeatureRel();
        QuerySQL sql = new SQL<>().buildQuerySQL()
                .select(McValue::new, mcFeatureRel::getLinkRelId, mcFeatureRel::getLinkRelMesh)
                .from(McValue.class)
                .innerJoin(new Join().with(McFeatureRel.class).on(mcValue::getFeatureId, mcFeatureRel::getFeatureId).and(mcValue::getFeatureType, mcFeatureRel::getFeatureType))
                .where(mcFeatureRel::getLinkRelMesh, "26482653")
                .and(mcFeatureRel::getLinkRelId, 524658)
                .andBetween(mcValue::getConfidenceId, 101, 160)
                .and(mcValue::getConfidenceId, "in", Lists.newArrayList(101, 160));

        SQLWrapper sqlWrapper = SqlBuilderUtils.buildSql(sql);
        System.out.println("sqlWrapper:\r\n" + JsonUtils.obj2Json(sqlWrapper));
        //TODO
        System.out.println("sql:\r\n" + sqlWrapper.resolve2SQL());
    }

}
