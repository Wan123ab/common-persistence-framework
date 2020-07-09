package cn.wq.persistence.jpa_plus;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.jpa_plus.dto.IMcIdentifierDto;
import cn.wq.persistence.jpa_plus.dto.McIdentifierDto;
import cn.wq.persistence.jpa_plus.model.McIdentifier;
import cn.wq.persistence.jpa_plus.repository.McIdentifierRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class TestProjections {

    @Autowired
    private McIdentifierRepository mcIdentifierRepository;

    /**
     * 测试查询返回接口类型result
     */
    @Test
    public void test1() {

        List<IMcIdentifierDto> iMcIdentifierDtos = mcIdentifierRepository.findByCollectionID(1);
        System.out.println(JsonUtils.obj2Json(iMcIdentifierDtos));
        iMcIdentifierDtos.forEach(iMcIdentifierDto ->
                System.out.println(iMcIdentifierDto.getPrefixAndConfidenceTypeName("Prefix")));
    }

    /**
     * 测试查询返回自定义Class类型result
     */
    @Test
    public void test2() {

        List<McIdentifierDto> mcIdentifierDtos = mcIdentifierRepository.queryByCollectionID(1);
        System.out.println(JsonUtils.obj2Json(mcIdentifierDtos));
    }

    /**
     * 测试查询返回泛型化的result
     */
    @Test
    public void test3() {

        List<McIdentifier> mcIdentifiers = mcIdentifierRepository.getByCollectionID(1, McIdentifier.class);
        System.out.println(JsonUtils.obj2Json(mcIdentifiers));
    }

    @Test
    public void test4() {

        Page<McIdentifier> page = mcIdentifierRepository.findByCollectionID(
                1, PageRequest.of(1, 10), McIdentifier.class);
        System.out.println(JsonUtils.obj2Json(page));
    }

}
 
