package cn.wq.persistence.mybatis_plus.dao;

import cn.wq.persistence.sql.model.McValue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/6/10 18:16
 * @desc
 */
public interface CommonMapper extends BaseMapper<McValue>{

    @Select("${sql}")
    List<Map<String, Object>> executeQuery(@Param("sql") String sql) throws Exception;

    @Select("${sql}")
    Integer executeUpdate(@Param("sql") String sql) throws Exception;

}
 
