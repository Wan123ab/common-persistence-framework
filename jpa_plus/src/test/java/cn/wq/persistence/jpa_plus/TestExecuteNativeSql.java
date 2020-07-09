package cn.wq.persistence.jpa_plus;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.jpa_plus.model.McIdentifier;
import cn.wq.persistence.jpa_plus.service.McIdentifierService;
import cn.wq.persistence.sql.jdbc.bean.QuerySQL;
import cn.wq.persistence.sql.jdbc.bean.SQL;
import cn.wq.persistence.sql.jdbc.bean.SQLWrapper;
import cn.wq.persistence.sql.jdbc.utils.SqlBuilderUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/9 14:20
 * @desc 测试执行原生sql
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestExecuteNativeSql {

    @Autowired
    private McIdentifierService mcIdentifierService;

    @Test
    public void test(){
        QuerySQL querySQL = new SQL<>().buildQuerySQL()
                .select(McIdentifier::new)
                .from(McIdentifier.class)
                .where(new McIdentifier()::getId, 1);
        SQLWrapper sqlWrapper = SqlBuilderUtils.buildSql(querySQL);

        List<McIdentifier> mcIdentifiers = mcIdentifierService.executeQuery(sqlWrapper.getSql(), McIdentifier.class, sqlWrapper.getParams());
        System.out.println(JsonUtils.obj2Json(mcIdentifiers));
    }



}
 
