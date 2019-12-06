package cn.wq.persistence.jpa;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.jpa.model.McIdentifier;
import cn.wq.persistence.sql.dao.BaseDao;
import cn.wq.persistence.sql.jdbc.bean.Criteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @version 1.0
 * @auther 万强
 * @date 2019/11/15 17:59
 * @desc
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
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

}
 
