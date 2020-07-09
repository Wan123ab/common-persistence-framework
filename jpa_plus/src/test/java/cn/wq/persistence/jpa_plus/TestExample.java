package cn.wq.persistence.jpa_plus;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.jpa_plus.model.McIdentifier;
import cn.wq.persistence.jpa_plus.repository.McIdentifierRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/3 14:53
 * @desc
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestExample {


    @Autowired
    private McIdentifierRepository mcIdentifierRepository;

    /**
     * 测试查询返回接口类型result
     */
    @Test
    public void test1() {

        McIdentifier mcIdentifier = new McIdentifier();
        mcIdentifier.setCollectionID(1);

        Example<McIdentifier> mcIdentifierExample = Example.of(mcIdentifier);
        List<McIdentifier> mcIdentifiers = mcIdentifierRepository.findAll(mcIdentifierExample);
        System.out.println(JsonUtils.obj2Json(mcIdentifiers));
    }



}
 
