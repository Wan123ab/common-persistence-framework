package cn.wq.persistence.jpa_plus.repository;

import cn.wq.persistence.jpa_plus.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/9 10:33
 * @desc 通用Repository，继承JpaRepository
 * <p>
 * 做了一些扩展，包含以下功能：
 * 1、灵活的执行原生sql以及参数绑定
 * 2、支持自定义Criteria动态拼接条件进行灵活的查询
 * 3、支持执行扩展的SQL对象
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    /**
     * 根据原生sql进行查询，返回指定clz的结果集
     *
     * @param sql    要执行的sql
     * @param clz    查询返回对象类型
     * @param params sql参数
     * @return List<T>
     */
    List<T> executeQuery(String sql, Class<T> clz, Object... params);


    /**
     * 执行原生sql（增删改）
     *
     * @param sql    要执行的sql
     * @param params sql参数
     * @return 影响的行数rows
     */
    int executeUpdate(String sql, Object... params);

}
