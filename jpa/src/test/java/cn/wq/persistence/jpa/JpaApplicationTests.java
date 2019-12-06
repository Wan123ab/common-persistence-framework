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

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JpaApplicationTests {

    @Autowired
    private BaseDao<Serializable> baseDao;

    @Test
    public void testAR() {
        McIdentifier mcIdentifier = new McIdentifier();
        try {
            List<McIdentifier> mcIdentifierList = mcIdentifier.queryAll();
            System.out.println(JsonUtils.obj2Json(mcIdentifierList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        try {
            List<McIdentifier> mcIdentifierList = baseDao.queryAll(McIdentifier.class);
            System.out.println(JsonUtils.obj2Json(mcIdentifierList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCount() {
        try {
            BigInteger count = baseDao.queryCount(McIdentifier.class);
            BigInteger count1 = new McIdentifier().queryCount();
            System.out.println("记录总数count：" + count);
            System.out.println("记录总数count1：" + count1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCountWithCriteria() {
        try {
            McIdentifier mcIdentifier = new McIdentifier();
            Criteria criteria = new Criteria().where(mcIdentifier::getCollectionID, 1)
                    .and(mcIdentifier::getConfidenceID, "<=", 160)
                    .andIfAbsent(mcIdentifier::getAttributeID, null);
            BigInteger count = baseDao.queryCountWithCriteria(criteria, McIdentifier.class);

            BigInteger count1 = mcIdentifier.queryCountWithCriteria(criteria);
            System.out.println("记录总数count：" + count);
            System.out.println("记录总数count1：" + count1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
