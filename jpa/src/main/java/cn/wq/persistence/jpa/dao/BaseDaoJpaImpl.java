package cn.wq.persistence.jpa.dao;

import cn.wq.persistence.common.util.EntityUtils;
import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.sql.dao.BaseDao;
import cn.wq.persistence.sql.jdbc.bean.*;
import cn.wq.persistence.sql.jdbc.function.FieldSupplier;
import cn.wq.persistence.sql.model.Model;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.wq.persistence.common.util.EntityUtils.getPK;
import static cn.wq.persistence.sql.jdbc.utils.SqlBuilderUtils.*;


/**
 * @author: 万强
 * @date: 2019/7/26 14:18
 * @desc: BaseDao通用实现类---基于JPA
 * @Version: 1.0
 */
@Repository
@SuppressWarnings({"all"})
public class BaseDaoJpaImpl implements BaseDao {

    /**
     * 注入容器托管的EntityManager，事务需要配合@Transactional使用，不能手动控制
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 注入容器托管的EntityManagerFactory，可用于创建应用托管的EntityManager
     * 需要手动创建、开启、提交和回滚事务
     */
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void save(T t) throws Exception {
        entityManager.persist(t);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void update(T t) throws Exception {
        entityManager.merge(t);
    }

    /**
     * 批量保存，batch_size=1000，此处手动控制事务以保证每次flush()都能实时同步到DB
     * 注意：需保证entityManager.persist()在事务范围内执行，否则将会报错。
     * 以为涉及到实体状态转化
     *
     * @param list 实体对象集合
     * @param <T>
     */
    @Override
    /*手动控制事务，不再使用Spring容器自带事务*/
//    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void batchSave(List<T> list) throws Exception {
        if (!CollectionUtils.isEmpty(list)) {
            EntityManager entityManager = null;
            EntityTransaction transaction = null;
            try {
                /*创建应用托管的EntityManager，可手动控制事务*/
                entityManager = entityManagerFactory.createEntityManager();
                /*创建事务*/
                transaction = entityManager.getTransaction();
                /*开启事务*/
                transaction.begin();
                int index = 0;
                for (T t : list) {
                    index++;
                    entityManager.persist(t);
                    if (index % BATCH_PAGE_SIZE == 0 || index == list.size()) {
                        /*
                           调用flush()同步到DB，此时没有真正执行，需等到事务提交才会执行
                         */
                        entityManager.flush();
                        /*每次批处理完后清空EntityManager，避免实体类积累造成
                          EntityManager内存溢出或者flush()速度慢的问题
                         */
                        entityManager.clear();

                        /*提交事务，会立刻持久化到DB中*/
                        transaction.commit();
                        /*每次迭代到batchSize临界值，便会提交事务，更新以前成功执行的批处理作业。
                          然后再次开启事务，进行下一次任务，这样可避免大事务操作，同时可确保
                          如果执行失败，不会丢失以前成功执行的批处理作业完成的工作
                        */
                        transaction.begin();
                    }
                }
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    /*
                       如果抛出异常，我们必须确保回滚当前正在运行的数据库事务。 否则可能会导致许多问题，
                       因为数据库可能仍然认为事务处于打开状态，锁可能会被持有，直到事务超时或由DBA结束
                     */
                    transaction.rollback();
                }
                throw e;
            } finally {
                if (entityManager != null) {
                    /*关闭EntityManager，以便可以清除上下文并释放Session级的资源*/
                    entityManager.close();
                }
            }
        }
    }

    /**
     * 批量更新
     *
     * @param list 实体对象集合
     * @param <T>
     */
    @Override
//    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void batchUpdate(List<T> list) throws Exception {
        if (!CollectionUtils.isEmpty(list)) {
            EntityManager entityManager = null;
            EntityTransaction transaction = null;
            try {
                /*创建应用托管的EntityManager，可手动控制事务*/
                entityManager = entityManagerFactory.createEntityManager();
                /*创建事务*/
                transaction = entityManager.getTransaction();
                /*开启事务*/
                transaction.begin();
                int index = 0;
                for (T t : list) {
                    index++;
                    entityManager.merge(t);
                    if (index % BATCH_PAGE_SIZE == 0 || index == list.size()) {
                        /*
                           调用flush()同步到DB，此时没有真正执行，需等到事务提交才会执行
                         */
                        entityManager.flush();
                        /*每次批处理完后清空EntityManager，避免实体类积累造成
                          EntityManager内存溢出或者flush()速度慢的问题
                         */
                        entityManager.clear();

                        /*提交事务，会立刻持久化到DB中*/
                        transaction.commit();
                        /*每次迭代到batchSize临界值，便会提交事务，更新以前成功执行的批处理作业。
                          然后再次开启事务，进行下一次任务，这样可避免大事务操作，同时可确保
                          如果执行失败，不会丢失以前成功执行的批处理作业完成的工作
                        */
                        transaction.begin();
                    }
                }
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    /*
                       如果抛出异常，我们必须确保回滚当前正在运行的数据库事务。 否则可能会导致许多问题，
                       因为数据库可能仍然认为事务处于打开状态，锁可能会被持有，直到事务超时或由DBA结束
                     */
                    transaction.rollback();
                }
                throw e;
            } finally {
                if (entityManager != null) {
                    /*关闭EntityManager，以便可以清除上下文并释放Session级的资源*/
                    entityManager.close();
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void delete(Serializable id, Class<T> clz) throws Exception {
        entityManager.remove(queryOne(id, clz));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void delete(T t) throws Exception {
        entityManager.remove(t);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void deleteAll(Class<T> clz) throws Exception {
        String sql = "DELETE FROM %s ";
        Query query = entityManager.createQuery(String.format(sql, EntityUtils.getTableName(clz)));
        query.executeUpdate();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void deleteWithCriteria(Criteria criteria, Class<T> clz) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM %s WHERE ");
        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        sql.append(sqlWrapper.getSql());
        Object[] params = sqlWrapper.getParams();

        Query nativeQuery = entityManager.createNativeQuery(String.format(sql.toString(), EntityUtils.getTableName(clz)));
        setParameters(nativeQuery, params);
        nativeQuery.executeUpdate();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void batchDelete(List<Serializable> ids, Class<T> clz) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM %s WHERE ").append(getPK(clz)).append(" in (:ids)");

        Query query = entityManager.createQuery(String.format(sql.toString(), EntityUtils.getTableName(clz)));
        query.setParameter("ids", ids);
        query.executeUpdate();
    }

    @Override
    public <T extends Model> T queryOne(Serializable id, Class<T> clz) throws Exception {
        return entityManager.find(clz, id);
    }

    @Override
    public <T extends Model> List<T> queryAll(Class<T> clz, Sort... sorts) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT " + FieldSupplier.getAllColumnNameAndAlias(clz) + " FROM  " + EntityUtils.getTableName(clz));
        if (sorts.length > 0) {
            resolveOrderBy(Arrays.asList(sorts), sql);
        }
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        /*设置将RS映射为Map，而不是直接返回Object[]*/
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        /*查询数据集合*/
        List<Map> list = (List<Map>) nativeQuery.getResultList();
        List<T> collect = list.stream().map(m -> JsonUtils.map2Obj(m, clz)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public <T extends Model> PageResult<T> pageQuery(Page page, Class<T> clz, Sort... sorts) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT " + FieldSupplier.getAllColumnNameAndAlias(clz) + " FROM  " + EntityUtils.getTableName(clz));
        if (sorts.length > 0) {
            resolveOrderBy(Arrays.asList(sorts), sql);
        } else {
            if (!StringUtils.isEmpty(getPK(clz))) {
                sql.append(" ORDER BY ").append(getPK(clz)).append(" ");
            }
        }
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        /*设置分页*/
        nativeQuery.setFirstResult(page.getOffset());
        nativeQuery.setMaxResults(page.getPageSize());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        /*查询数据集合*/
        List<Map> list = (List<Map>) nativeQuery.getResultList();
        List<T> collect = list.stream().map(m -> JsonUtils.map2Obj(m, clz)).collect(Collectors.toList());

        /*查询数据总数*/
        String countSql = "SELECT count(1) FROM " + EntityUtils.getTableName(clz);
        Query countQuery = entityManager.createNativeQuery(countSql);
        Integer total = Integer.valueOf(String.valueOf(countQuery.getSingleResult()));

        return new PageResult<>(total, collect);
    }

    @Override
    public <T extends Model> PageResult<T> pageQueryWithCriteria(Page page, Criteria criteria, Class<T> clz, Sort... sorts) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT " + FieldSupplier.getAllColumnNameAndAlias(clz) + " FROM  %s WHERE ");
        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        sql.append(sqlWrapper.getSql());

        if (sorts.length > 0) {
            resolveOrderBy(Arrays.asList(sorts), sql);
        } else {
            if (!StringUtils.isEmpty(getPK(clz))) {
                sql.append(" ORDER BY ").append(getPK(clz)).append(" ");
            }
        }

        Object[] params = sqlWrapper.getParams();

        Query nativeQuery = entityManager.createNativeQuery(String.format(sql.toString(), EntityUtils.getTableName(clz)));
        setParameters(nativeQuery, params);
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        /*设置分页*/
        nativeQuery.setFirstResult(page.getOffset());
        nativeQuery.setMaxResults(page.getPageSize());

        /*查询数据集合*/
        List<Map> list = (List<Map>) nativeQuery.getResultList();
        List<T> collect = list.stream().map(m -> JsonUtils.map2Obj(m, clz)).collect(Collectors.toList());

        /*查询数据总数*/
        String countSql = "SELECT count(1) FROM " + EntityUtils.getTableName(clz);
        Query countQuery = entityManager.createNativeQuery(countSql);
        Integer total = Integer.valueOf(String.valueOf(countQuery.getSingleResult()));

        return new PageResult<>(total, collect);
    }

    @Override
    public <T extends Model> List<T> queryWithCriteria(Criteria criteria, Class<T> clz, Sort... sorts) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT " + FieldSupplier.getAllColumnNameAndAlias(clz) + " FROM  %s WHERE ");
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

        Query nativeQuery = entityManager.createNativeQuery(String.format(sql.toString(), EntityUtils.getTableName(clz)));
        setParameters(nativeQuery, params);
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        /*查询数据集合*/
        List<Map> list = (List<Map>) nativeQuery.getResultList();
        List<T> collect = list.stream().map(m -> JsonUtils.map2Obj(m, clz)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public <T extends Model> T queryOne(Criteria criteria, Class<T> clz) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT " + FieldSupplier.getAllColumnNameAndAlias(clz) + " FROM  %s WHERE ");
        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        sql.append(sqlWrapper.getSql());

        Object[] params = sqlWrapper.getParams();

        Query nativeQuery = entityManager.createNativeQuery(String.format(sql.toString(), EntityUtils.getTableName(clz)));
        setParameters(nativeQuery, params);
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        /*查询数据*/
        Map singleResult = (Map) nativeQuery.getSingleResult();
        T t = JsonUtils.map2Obj(singleResult, clz);
        return t;
    }

    @Override
    public <T> T queryOneWithSql(SQL sql, Class<T> clz) throws Exception {
        SQLWrapper sqlWrapper = buildSql(sql);
        Query nativeQuery = entityManager.createNativeQuery(sqlWrapper.getSql());
        setParameters(nativeQuery, sqlWrapper.getParams());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        /*查询数据*/
        Map singleResult = (Map) nativeQuery.getSingleResult();
        T t = JsonUtils.map2Obj(singleResult, clz);
        return t;
    }

    @Override
    public <T> List<T> queryWithSql(SQL sql, Class<T> clz) throws Exception {
        List<Map<String, Object>> mapList = queryMapListWithSql(sql);
        List<T> collect = mapList.stream().map(m -> JsonUtils.map2Obj(m, clz)).collect(Collectors.toList());
        return collect;
    }

    /**
     * 根据SQL分页查询
     *
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
     * 执行原生sql查询
     *
     * @param sql
     * @param clz
     * @param params
     * @param <T>
     * @return
     * @throws Exception
     */
    @Override
    public <T> List<T> executeQuery(String sql, Class<T> clz, Object... params) throws Exception {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        setParameters(nativeQuery, params);
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        /*查询数据集合*/
        List<Map> list = (List<Map>) nativeQuery.getResultList();
        List<T> collect = list.stream().map(m -> JsonUtils.map2Obj(m, clz)).collect(Collectors.toList());
        return collect;
    }

    /**
     * 调用Query#executeUpdate时，必须添加@Transactional，否则调用报错，提示需要事务
     *
     * @param sql sql拼接器
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeSQL(SQL sql) throws Exception {
        SQLWrapper sqlWrapper = buildSql(sql);
        Query nativeQuery = entityManager.createNativeQuery(sqlWrapper.getSql());
        setParameters(nativeQuery, sqlWrapper.getParams());

        /*执行sql*/
        int i = nativeQuery.executeUpdate();
        return i;
    }

    /**
     * 执行原生sql
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeSql(String sql, Object... params) throws Exception {
        Query query = entityManager.createNativeQuery(sql);
        setParameters(query, params);
        return query.executeUpdate();
    }

    /**
     * 执行hql
     *
     * @param hql
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int executeHql(String hql, Object... params) throws Exception {
        Query query = entityManager.createQuery(hql);
        setParameters(query, params);
        return query.executeUpdate();
    }

    /**
     * 设置下标参数
     *
     * @param query
     * @param params
     */
    private static void setParameters(Query query, Object[] params) {
        if (params == null) return;

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
    }

    /**
     * 查询记录总数
     *
     * @param <T> 返回值泛型
     * @param clz 要查询的表Class
     * @return Integer 记录总数
     * @throws Exception 异常
     */
    @Override
    public <T> Integer queryCount(Class<T> clz) throws Exception {
        /*查询数据总数*/
        String countSql = "SELECT COUNT(*) FROM " + EntityUtils.getTableName(clz);
        Query countQuery = entityManager.createNativeQuery(countSql);
        Integer total = Integer.valueOf(String.valueOf(countQuery.getSingleResult()));

        return total;
    }

    /**
     * 查询记录总数
     *
     * @param <T>      返回值泛型
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
        Object[] params = sqlWrapper.getParams();

        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        setParameters(countQuery, params);
        Integer total = (Integer) countQuery.getSingleResult();

        return total;
    }

    @Override
    public List<Map<String, Object>> queryMapListWithSql(SQL sql) throws Exception {
        SQLWrapper sqlWrapper = buildSql(sql);
        StringBuffer stringBuffer = new StringBuffer(sqlWrapper.getSql());

        Query nativeQuery = entityManager.createNativeQuery(stringBuffer.toString());
        setParameters(nativeQuery, sqlWrapper.getParams());
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        /*查询数据集合*/
        List<Map<String, Object>> list = (List<Map<String, Object>>) nativeQuery.getResultList();
        return list;
    }


}
 
