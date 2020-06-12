package cn.wq.persistence.mybatis_plus.dao;

import cn.wq.persistence.sql.model.Model;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/6/10 16:06
 * @desc
 */
public interface BaseMapper<T extends Model> extends
        com.baomidou.mybatisplus.core.mapper.BaseMapper<T>{
}
 
