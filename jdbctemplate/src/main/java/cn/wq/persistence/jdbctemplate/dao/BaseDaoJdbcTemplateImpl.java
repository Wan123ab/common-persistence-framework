package cn.wq.persistence.jdbctemplate.dao;

import cn.wq.persistence.common.util.EntityUtils;
import cn.wq.persistence.sql.dao.BaseDao;
import cn.wq.persistence.sql.jdbc.bean.*;
import cn.wq.persistence.sql.jdbc.function.FieldSupplier;
import cn.wq.persistence.sql.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cn.wq.persistence.common.util.EntityUtils.getPK;
import static cn.wq.persistence.sql.jdbc.utils.SqlBuilderUtils.*;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/6/15 17:18
 * @desc
 */
@Repository
@SuppressWarnings({"all"})
public class BaseDaoJdbcTemplateImpl implements BaseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 批量保存指定的持久化对象
     *
     * @param list 实体对象集合
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T extends Model> void batchSave(List<T> list) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<Object[]> batchArgs = new ArrayList<>();
        SQLWrapper sqlWrapper = buildSql(list.get(0), SqlType.INSERT);
        int[] argTypes = sqlWrapper.getParamTypes();
        list.forEach(t -> batchArgs.add(buildSql(t, SqlType.INSERT).getParams()));

        jdbcTemplate.batchUpdate(sqlWrapper.getSql(), batchArgs, argTypes);
    }

    /**
     * 批量更新指定的持久化对象
     *
     * @param list 实体对象集合
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T extends Model> void batchUpdate(List<T> list) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<Object[]> batchArgs = new ArrayList<>();
        SQLWrapper sqlWrapper = buildSql(list.get(0), SqlType.UPDATE);
        int[] argTypes = sqlWrapper.getParamTypes();
        list.forEach(t -> batchArgs.add(buildSql(t, SqlType.UPDATE).getParams()));

        jdbcTemplate.batchUpdate(sqlWrapper.getSql(), batchArgs, argTypes);
    }

    /**
     * 根据主键删除
     *
     * @param id  实体主键
     * @param clz
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T extends Model> void delete(Serializable id, Class<T> clz) throws Exception {

        SQLWrapper sqlWrapper = buildSql(clz.newInstance(), SqlType.DELETE);
        jdbcTemplate.update(sqlWrapper.getSql(), id);
    }

    @Override
    public <T extends Model> void delete(T t) throws Exception {
        SQLWrapper sqlWrapper = buildSql(t, SqlType.DELETE);
        jdbcTemplate.update(sqlWrapper.getSql(), sqlWrapper.getParams());
    }

    @Override
    public <T extends Model> void deleteAll(Class<T> clz) throws Exception {
        String sql = "DELETE FROM %s ";
        jdbcTemplate.execute(String.format(sql, EntityUtils.getTableName(clz)));
    }

    /**
     * 根据where条件删除
     *
     * @param criteria 条件参数
     * @param clz
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T extends Model> void deleteWithCriteria(Criteria criteria, Class<T> clz) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM %s WHERE ");
        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        sql.append(sqlWrapper.getSql());
        Object[] params = sqlWrapper.getParams();

        jdbcTemplate.update(String.format(sql.toString(), EntityUtils.getTableName(clz)), params);
    }

    /**
     * 根据主键批量删除
     *
     * @param ids 主键集合
     * @param clz
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T extends Model> void batchDelete(List<Serializable> ids, Class<T> clz) throws Exception {
        T t = clz.newInstance();
        Criteria criteria = new Criteria().where(t::getId, "in", ids);
        deleteWithCriteria(criteria, clz);
    }

    /**
     * 根据ID检索持久化对象
     *
     * @param id  主键
     * @param clz
     * @return T 实体对象
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T extends Model> T queryOne(Serializable id, Class<T> clz) throws Exception {
        T t = clz.newInstance();
        Criteria criteria = new Criteria().where(t::getId, id);
        return DataAccessUtils.singleResult(queryWithCriteria(criteria, clz));
    }

    /**
     * 检索所有持久化对象
     *
     * @param clz
     * @param sorts
     * @return
     * @throws Exception
     */
    @Override
    public <T extends Model> List<T> queryAll(Class<T> clz, Sort... sorts) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT " + FieldSupplier.getAllColumnNameAndAlias(clz) + " FROM  " + EntityUtils.getTableName(clz));
        if (sorts.length > 0) {
            resolveOrderBy(Arrays.asList(sorts), sql);
        } else {
            if (!StringUtils.isEmpty(getPK(clz))) {
                sql.append(" ORDER BY ").append(getPK(clz));
            }
        }
        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(clz));
    }

    /**
     * 分页查询
     *
     * @param page  分页条件
     * @param clz
     * @param sorts
     * @return PageResult 分页查询结果
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T extends Model> PageResult<T> pageQuery(Page page, Class<T> clz, Sort... sorts) throws Exception {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT " + FieldSupplier.getAllColumnNameAndAlias(clz) + " FROM %s ");
        if (sorts.length > 0) {
            resolveOrderBy(Arrays.asList(sorts), sql);
        } else {
            sql.append(" ORDER BY " + getPK(clz));
        }
        sql.append(" LIMIT %s OFFSET %s");
        String pageQuerySql = String.format(sql.toString(), EntityUtils.getTableName(clz), page.getPageSize(), page.getOffset());

        List<T> list = jdbcTemplate.query(pageQuerySql, new BeanPropertyRowMapper<>(clz));

        //查询记录总数
        Integer total = queryCount(clz);

        return new PageResult<>(total, list);
    }

    /**
     * 分页条件查询
     *
     * @param page     分页条件
     * @param criteria 查询条件
     * @param clz
     * @param sorts
     * @return PageResult 分页查询结果
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T extends Model> PageResult<T> pageQueryWithCriteria(Page page, Criteria criteria, Class<T> clz, Sort... sorts) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT " + FieldSupplier.getAllColumnNameAndAlias(clz) + " FROM %s WHERE ");
        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        sql.append(sqlWrapper.getSql());

        if (sorts.length > 0) {
            resolveOrderBy(Arrays.asList(sorts), sql);
        } else {
            sql.append(" ORDER BY " + getPK(clz));
        }

        sql.append(" LIMIT %s OFFSET %s");

        String pageQuerySql = String.format(sql.toString(), EntityUtils.getTableName(clz), page.getPageSize(), page.getOffset());

        List<T> list = jdbcTemplate.query(pageQuerySql, sqlWrapper.getParams(), sqlWrapper.getParamTypes(), new BeanPropertyRowMapper<>(clz));

        //查询记录总数
        Integer total = queryCount(clz);

        return new PageResult<>(total, list);
    }

    /**
     * 条件查询
     *
     * @param criteria 查询条件
     * @param clz
     * @param sorts
     * @return List 结果集
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T extends Model> List<T> queryWithCriteria(Criteria criteria, Class<T> clz, Sort... sorts) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT " + FieldSupplier.getAllColumnNameAndAlias(clz) + " FROM %s WHERE ");
        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        sql.append(sqlWrapper.getSql());

        if (sorts.length > 0) {
            resolveOrderBy(Arrays.asList(sorts), sql);
        } else {
            if (!StringUtils.isEmpty(getPK(clz))) {
                sql.append(" ORDER BY ").append(getPK(clz));
            }
        }

        Object[] params = sqlWrapper.getParams();

        return jdbcTemplate.query(String.format(sql.toString(), EntityUtils.getTableName(clz)), params, new BeanPropertyRowMapper<>(clz));

    }

    /**
     * 根据条件查询
     *
     * @param criteria 查询条件
     * @param clz
     * @return T 实体对象
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T extends Model> T queryOne(Criteria criteria, Class<T> clz) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT " + FieldSupplier.getAllColumnNameAndAlias(clz) + " FROM %s WHERE ");
        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        sql.append(sqlWrapper.getSql());

        Object[] params = sqlWrapper.getParams();
        List<T> list = jdbcTemplate.query(String.format(sql.toString(), EntityUtils.getTableName(clz)), params, new BeanPropertyRowMapper<>(clz));
        return DataAccessUtils.singleResult(list);
    }

    /**
     * 根据条件查询
     *
     * @param sql 查询条件
     * @param clz
     * @return T 实体对象
     * @throws Exception sql错误抛出异常
     */
    @Override
    public <T> T queryOneWithSql(SQL sql, Class<T> clz) throws Exception {
        return DataAccessUtils.singleResult(queryWithSql(sql, clz));
    }

    /**
     * 根据sql查询
     *
     * @param sql sql拼接器
     * @param clz
     * @throws Exception
     */
    @Override
    public <T> List<T> queryWithSql(SQL sql, Class<T> clz) throws Exception {
        SQLWrapper sqlWrapper = buildSql(sql);
        return jdbcTemplate.query(sqlWrapper.getSql(), sqlWrapper.getParams(), sqlWrapper.getParamTypes(), new BeanPropertyRowMapper<>(clz));
    }

    /**
     * 根据SQL分页查询
     *
     * @param page
     * @param sql
     * @param clz
     * @return
     * @throws Exception
     */
    @Override
    public <T> PageResult<T> pageQueryWithSql(SQL sql, Class<T> clz) throws Exception {

        List<T> list = queryWithSql(sql, clz);
        Integer total = queryCount(clz);
        return new PageResult<>(total, list);
    }

    /**
     * 根据原生sql进行查询，返回指定clz的结果集
     *
     * @param sql
     * @param clz
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public <T> List<T> executeQuery(String sql, Class<T> clz, Object... params) throws Exception {
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(clz));
    }

    /**
     * 执行Sql（增删改）
     *
     * @param sql sql拼接器
     * @return int 更新条目数量
     * @throws Exception
     */
    @Override
    public int executeSQL(SQL sql) throws Exception {
        SQLWrapper sqlWrapper = buildSql(sql);
        return jdbcTemplate.update(sqlWrapper.getSql(), sqlWrapper.getParams(), sqlWrapper.getParamTypes());
    }

    /**
     * 执行原生sql（增删改）
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public int executeSql(String sql, Object... params) throws Exception {

        return jdbcTemplate.update(sql, params);
    }

    /**
     * 查询记录总数
     *
     * @param clz 要查询的表Class
     * @return Integer 记录总数
     * @throws Exception 异常
     */
    @Override
    public <T> Integer queryCount(Class<T> clz) throws Exception {
        /*查询数据总数*/
        String countSql = "SELECT COUNT(*) FROM " + EntityUtils.getTableName(clz);
        return jdbcTemplate.queryForObject(countSql, Integer.class);
    }

    /**
     * 查询记录总数
     *
     * @param criteria 条件对象
     * @param clz      要查询的表Class
     * @return Integer 记录总数
     * @throws Exception 异常
     */
    @Override
    public <T> Integer queryCountWithCriteria(Criteria criteria, Class<T> clz) throws Exception {
        StringBuffer countSql = new StringBuffer("SELECT COUNT(*) FROM " + EntityUtils.getTableName(clz));
        countSql.append(" WHERE ");
        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        countSql.append(sqlWrapper.getSql());

        return jdbcTemplate.queryForObject(countSql.toString(), sqlWrapper.getParams(), sqlWrapper.getParamTypes(), Integer.class);
    }

    /**
     * 使用SQL查询，返回Map集合
     *
     * @param sql
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> queryMapListWithSql(SQL sql) throws Exception {
        SQLWrapper sqlWrapper = buildSql(sql);
        StringBuffer stringBuffer = new StringBuffer(sqlWrapper.getSql());

        return jdbcTemplate.queryForList(sqlWrapper.getSql(), sqlWrapper.getParams(), sqlWrapper.getParamTypes());
    }
}
 
