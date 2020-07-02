package cn.wq.persistence.jdbctemplate;

import cn.wq.persistence.sql.dao.BaseDao;
import cn.wq.persistence.sql.model.McIdentifier;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/6/29 17:32
 * @desc
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUpdate {

    @Autowired
    private BaseDao baseDao;

    @Test
    public void batchUpdate() {
        McIdentifier mcIdentifier1 = McIdentifier.builder()
                .AttributeID(4)
                .AttributeName("123")
                .CollectionID(1)
                .ConfidenceID(997)
                .ConfidenceType(1)
                .ConfidenceTypeName("213131")
                .build();

        McIdentifier mcIdentifier2 = McIdentifier.builder()
                .AttributeID(5)
                .AttributeName("123")
                .CollectionID(1)
                .ConfidenceID(998)
                .ConfidenceType(1)
                .ConfidenceTypeName("213131")
                .build();

        McIdentifier mcIdentifier3 = McIdentifier.builder()
                .AttributeID(6)
                .AttributeName("123")
                .CollectionID(1)
                .ConfidenceID(999)
                .ConfidenceType(1)
                .ConfidenceTypeName("213131")
                .build();

        try {
            baseDao.batchUpdate(Lists.newArrayList(mcIdentifier1, mcIdentifier2, mcIdentifier3));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
 
