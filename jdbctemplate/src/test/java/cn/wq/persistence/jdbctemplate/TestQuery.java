package cn.wq.persistence.jdbctemplate;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.sql.dao.BaseDao;
import cn.wq.persistence.sql.jdbc.bean.Criteria;
import cn.wq.persistence.sql.jdbc.bean.Page;
import cn.wq.persistence.sql.jdbc.bean.PageResult;
import cn.wq.persistence.sql.jdbc.bean.Sort;
import cn.wq.persistence.sql.model.McIdentifier;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/6/17 11:24
 * @desc
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestQuery {

    @Autowired
    private BaseDao baseDao;

    @Test
    public void testQueryOne(){
        McIdentifier mcIdentifier = new McIdentifier();
        Criteria criteria = new Criteria().where(mcIdentifier::getConfidenceID, 1);
        try {
            McIdentifier mcIdentifier1 = mcIdentifier.queryOne(criteria);
            System.out.println(mcIdentifier1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryList(){
        McIdentifier mcIdentifier = new McIdentifier();
        Criteria criteria = new Criteria()
                .where(mcIdentifier::getConfidenceID, "in", Lists.newArrayList(1,2,3,4,5))
                .andBetween(mcIdentifier::getConfidenceType, 1, 3);
        try {
            List<McIdentifier> mcIdentifierList = mcIdentifier.queryWithCriteria(criteria);
            System.out.println(JsonUtils.obj2Json(mcIdentifierList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryList1(){
        McIdentifier mcIdentifier = new McIdentifier();
        Criteria criteria = new Criteria().where(mcIdentifier::getConfidenceID, "in", Lists.newArrayList(1,2,3,4,5));
        try {
            List<McIdentifier> mcIdentifierList = mcIdentifier.queryWithCriteria(criteria);
            System.out.println(JsonUtils.obj2Json(mcIdentifierList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryAll(){
        try {
            List<McIdentifier> mcIdentifiers = baseDao.queryAll(McIdentifier.class);
            System.out.println(JsonUtils.obj2Json(mcIdentifiers));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testPageQuery(){
        try {
            PageResult<McIdentifier> mcIdentifierPageResult = baseDao.pageQuery(new Page(2, 10), McIdentifier.class);
            System.out.println(JsonUtils.obj2Json(mcIdentifierPageResult));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
 
