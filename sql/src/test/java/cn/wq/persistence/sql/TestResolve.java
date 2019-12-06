package cn.wq.persistence.sql;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.sql.jdbc.utils.SqlTableUtils;
import cn.wq.persistence.sql.jdbc.bean.SQLWrapper;
import cn.wq.persistence.sql.model.McIdentifier;
import cn.wq.persistence.sql.table.Table;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @author 万强
 * @version 1.0
 * @date 2019/11/26 14:16
 * @desc
 */
public class TestResolve {

    @Test
    public void test(){

        Table<McIdentifier> mcIdentifierTable = SqlTableUtils.resolve2Table(McIdentifier.class);
        System.out.println(JsonUtils.obj2Json(mcIdentifierTable));

    }

    @Test
    public void testInsert(){

        McIdentifier mcIdentifier = new McIdentifier();
        mcIdentifier.setAttributeID(1);
        mcIdentifier.setAttributeName("AttributeName");
        mcIdentifier.setCollectionID(2);
        mcIdentifier.setConfidenceID(160);
        mcIdentifier.setConfidenceType(3);
        mcIdentifier.setConfidenceTypeName("ConfidenceTypeName");

        SQLWrapper sqlWrapper = SqlTableUtils.buildInsertSql(mcIdentifier);
        System.err.println(sqlWrapper.getSql());
        System.err.println(JSON.toJSONString(sqlWrapper.getParams()));
    }

    @Test
    public void testUpdate(){

        McIdentifier mcIdentifier = new McIdentifier();
        mcIdentifier.setAttributeID(1);
        mcIdentifier.setAttributeName("AttributeName");
        mcIdentifier.setCollectionID(2);
        mcIdentifier.setConfidenceID(160);
        mcIdentifier.setConfidenceType(3);
        mcIdentifier.setConfidenceTypeName("ConfidenceTypeName");

        SQLWrapper sqlWrapper = SqlTableUtils.buildUpdateSql(mcIdentifier);
        System.err.println(sqlWrapper.getSql());
        System.err.println(JSON.toJSONString(sqlWrapper.getParams()));
    }

    @Test
    public void testDelete(){

        McIdentifier mcIdentifier = new McIdentifier();
        mcIdentifier.setAttributeID(1);
        mcIdentifier.setAttributeName("AttributeName");
        mcIdentifier.setCollectionID(2);
        mcIdentifier.setConfidenceID(160);
        mcIdentifier.setConfidenceType(3);
        mcIdentifier.setConfidenceTypeName("ConfidenceTypeName");

        SQLWrapper sqlWrapper = SqlTableUtils.buildDeleteSql(mcIdentifier);
        System.err.println(sqlWrapper.getSql());
        System.err.println(JSON.toJSONString(sqlWrapper.getParams()));
    }


}
 
