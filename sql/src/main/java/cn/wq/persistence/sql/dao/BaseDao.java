package cn.wq.persistence.sql.dao;

import cn.wq.persistence.sql.jdbc.bean.*;
import cn.wq.persistence.sql.jdbc.utils.SqlBuilderUtils;
import cn.wq.persistence.sql.model.Model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @version : 1.0
 * @author : 万强
 * @date : 2019/7/26 13:56
 * @desc : 通用Dao接口
 */
public interface BaseDao {

    int BATCH_PAGE_SIZE = 1000;

    /**
     * 插入指定的持久化对象
     *
     * @param t 实体对象
     * @throws Exception sql错误抛出异常
     */
    default <T extends Model> void save(T t) throws Exception {
        SQLWrapper sqlWrapper = SqlBuilderUtils.buildSql(t, SqlType.INSERT);

        executeSql(sqlWrapper.getSql(), sqlWrapper.getParams());
    }

    /**
     * 修改指定的持久化对象
     *
     * @param t 实体对象
     * @throws Exception sql错误抛出异常
     */
    default <T extends Model> void update(T t) throws Exception {
        SQLWrapper sqlWrapper = SqlBuilderUtils.buildSql(t, SqlType.UPDATE);

        executeSql(sqlWrapper.getSql(), sqlWrapper.getParams());
    }

    /**
     * 批量保存指定的持久化对象
     *
     * @param list 实体对象集合
     * @throws Exception sql错误抛出异常
     */
    <T extends Model> void batchSave(List<T> list) throws Exception;

    /**
     * 批量更新指定的持久化对象
     *
     * @param list 实体对象集合
     * @throws Exception sql错误抛出异常
     */
    <T extends Model> void batchUpdate(List<T> list) throws Exception;

    /**
     * 根据主键删除
     *
     * @param id 实体主键
     * @throws Exception sql错误抛出异常
     */
    <T extends Model> void delete(Serializable id, Class<T> clz) throws Exception;

    <T extends Model> void delete(T t) throws Exception;

    <T extends Model> void deleteAll(Class<T> clz) throws Exception;

    /**
     * 根据where条件删除
     *
     * @param criteria 条件参数
     * @throws Exception sql错误抛出异常
     */
    <T extends Model> void deleteWithCriteria(Criteria criteria, Class<T> clz) throws Exception;

    /**
     * 根据主键批量删除
     *
     * @param ids 主键集合
     * @throws Exception sql错误抛出异常
     */
    <T extends Model> void batchDelete(List<Serializable> ids, Class<T> clz) throws Exception;

    /**
     * 根据ID检索持久化对象
     *
     * @param id 主键
     * @return T 实体对象
     * @throws Exception sql错误抛出异常
     */
    <T extends Model> T queryOne(Serializable id, Class<T> clz) throws Exception;

    /**
     * 检索所有持久化对象
     *
     * @param clz
     * @param sorts
     * @param <T>
     * @return
     * @throws Exception
     */
    <T extends Model> List<T> queryAll(Class<T> clz, Sort... sorts) throws Exception;

    /**
     * 分页查询
     *
     * @param page 分页条件
     * @return PageResult 分页查询结果
     * @throws Exception sql错误抛出异常
     */
    <T extends Model> PageResult<T> pageQuery(Page page, Class<T> clz, Sort... sorts) throws Exception;

    /**
     * 分页条件查询
     *
     * @param page     分页条件
     * @param criteria 查询条件
     * @return PageResult 分页查询结果
     * @throws Exception sql错误抛出异常
     */
    <T extends Model> PageResult<T> pageQueryWithCriteria(Page page, Criteria criteria, Class<T> clz, Sort... sorts) throws Exception;

    /**
     * 条件查询
     *
     * @param criteria 查询条件
     * @return List 结果集
     * @throws Exception sql错误抛出异常
     */
    <T extends Model> List<T> queryWithCriteria(Criteria criteria, Class<T> clz, Sort... sorts) throws Exception;

    /**
     * 根据条件查询
     *
     * @param criteria 查询条件
     * @return T 实体对象
     * @throws Exception sql错误抛出异常
     */
    <T extends Model> T queryOne(Criteria criteria, Class<T> clz) throws Exception;

    /**
     * 根据条件查询
     *
     * @param sql 查询条件
     * @return T 实体对象
     * @throws Exception sql错误抛出异常
     */
    <T> T queryOneWithSql(SQL sql, Class<T> clz) throws Exception;

    /**
     * 根据sql查询
     *
     * @param sql sql拼接器
     * @param <T> 查询结果类型
     * @throws Exception
     */
    <T> List<T> queryWithSql(SQL sql, Class<T> clz) throws Exception;

    /**
     * 根据SQL分页查询
     * @param sql
     * @param clz
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> PageResult<T> pageQueryWithSql(SQL sql, Class<T> clz) throws Exception;

    /**
     * 根据原生sql进行查询，返回指定clz的结果集
     *
     * @param sql
     * @param clz
     * @param params
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> List<T> executeQuery(String sql, Class<T> clz, Object... params) throws Exception;

    /**
     * 执行Sql（增删改）
     *
     * @param sql sql拼接器
     * @return int 更新条目数量
     * @throws Exception
     */
    int executeSQL(SQL sql) throws Exception;

    /**
     * 执行原生sql（增删改）
     *
     * @param sql
     * @param params
     * @return
     */
    int executeSql(String sql, Object... params) throws Exception;

    /**
     * 查询记录总数
     *
     * @param <T> 返回值泛型
     * @param clz 要查询的表Class
     * @return Integer 记录总数
     * @throws Exception 异常
     */
    <T> Integer queryCount(Class<T> clz) throws Exception;

    /**
     * 查询记录总数
     *
     * @param <T>      返回值泛型
     * @param criteria 条件对象
     * @param clz      要查询的表Class
     * @return Integer 记录总数
     * @throws Exception 异常
     */
    <T> Integer queryCountWithCriteria(Criteria criteria, Class<T> clz) throws Exception;

    /**
     * 使用SQL查询，返回Map集合
     * @param sql
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> queryMapListWithSql(SQL sql) throws Exception;

}
