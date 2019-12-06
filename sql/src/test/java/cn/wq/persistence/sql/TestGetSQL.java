package cn.wq.persistence.sql;

import cn.wq.persistence.sql.jdbc.bean.SQLWrapper;
import cn.wq.persistence.sql.jdbc.bean.SqlType;
import cn.wq.persistence.sql.jdbc.utils.SqlBuilderUtils;
import cn.wq.persistence.sql.model.McIdentifier;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @version 1.0
 * @auther 万强
 * @date 2019/11/15 16:26
 * @desc
 */
public class TestGetSQL {

    @Test
    public void testInsert(){
        McIdentifier mcIdentifier = new McIdentifier();
        mcIdentifier.setAttributeID(1);
        mcIdentifier.setAttributeName("AttributeName");
        mcIdentifier.setCollectionID(2);
        mcIdentifier.setConfidenceID(160);
        mcIdentifier.setConfidenceType(3);
        mcIdentifier.setConfidenceTypeName("ConfidenceTypeName");

        SQLWrapper sqlWrapper = SqlBuilderUtils.buildSql(mcIdentifier, SqlType.INSERT);
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

        SQLWrapper sqlWrapper = SqlBuilderUtils.buildSql(mcIdentifier, SqlType.DELETE);
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

        SQLWrapper sqlWrapper = SqlBuilderUtils.buildSql(mcIdentifier, SqlType.UPDATE);
        System.err.println(sqlWrapper.getSql());
        System.err.println(JSON.toJSONString(sqlWrapper.getParams()));
    }


}
 
