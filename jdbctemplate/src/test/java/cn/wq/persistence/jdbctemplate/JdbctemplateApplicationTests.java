package cn.wq.persistence.jdbctemplate;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.sql.jdbc.bean.Join;
import cn.wq.persistence.sql.jdbc.bean.QuerySQL;
import cn.wq.persistence.sql.jdbc.bean.SQL;
import cn.wq.persistence.sql.jdbc.bean.SQLWrapper;
import cn.wq.persistence.sql.jdbc.utils.SqlBuilderUtils;
import cn.wq.persistence.sql.model.McFeatureRel;
import cn.wq.persistence.sql.model.McValue;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JdbctemplateApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

        //返回MapList
        List<Map<String, Object>> mapList = jdbcTemplate.query(sqlWrapper.getSql(), sqlWrapper.getParams(), new ColumnMapRowMapper());
        System.out.println("查询返回result："+ JsonUtils.obj2Json(mapList));

    }

}
