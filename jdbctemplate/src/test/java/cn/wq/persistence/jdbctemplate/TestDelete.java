package cn.wq.persistence.jdbctemplate;

import cn.wq.persistence.sql.jdbc.bean.Criteria;
import cn.wq.persistence.sql.model.McIdentifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/6/15 17:33
 * @desc
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDelete {

    @Test
    public void deleteMcIdentifier(){
        McIdentifier mcIdentifier = new McIdentifier();
        Criteria criteria = new Criteria().where(mcIdentifier::getConfidenceID, 999);

        try {
            boolean result = mcIdentifier.deleteWithCriteria(criteria);
            System.out.println("result--->>>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
 
