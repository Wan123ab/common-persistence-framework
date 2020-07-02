package cn.wq.persistence.mybatis_plus;

import cn.wq.persistence.mybatis_plus.dao.CommonMapper;
import cn.wq.persistence.mybatis_plus.dao.McIdentifierMapper;
import cn.wq.persistence.sql.model.McIdentifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/6/17 17:44
 * @desc
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DebugMybatis {

    @Resource
    private McIdentifierMapper mcIdentifierMapper;

    @Test
    public void testInsert(){
        McIdentifier mcIdentifier = McIdentifier.builder()
                .AttributeID(1)
                .AttributeName("123")
                .CollectionID(1)
                .ConfidenceID(999)
                .ConfidenceType(1)
                .ConfidenceTypeName("213131")
                .build();

        mcIdentifierMapper.insert(mcIdentifier);
    }
}
 
