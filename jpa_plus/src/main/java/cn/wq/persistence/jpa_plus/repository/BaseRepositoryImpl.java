package cn.wq.persistence.jpa_plus.repository;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.jpa_plus.model.BaseEntity;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/9 10:42
 * @desc <p>
 * 做了一些扩展，包含以下功能：
 * 1、灵活的执行原生sql以及参数绑定
 * 2、支持自定义Criteria动态拼接条件进行灵活的查询
 * 3、支持执行扩展的SQL对象
 * <p>
 * 可灵活添加其他通用方法，并且方法名不会受到JPA方法名命名规则的限制
 */
public class BaseRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, Long> implements BaseRepository<T> {

    private final EntityManager em;

    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     * @param em          must not be {@literal null}.
     */
    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {

        super(domainClass, em);
        this.em = em;
    }

    /**
     * 根据原生sql进行查询，返回指定clz的结果集
     *
     * @param sql    要执行的sql
     * @param clz    查询返回对象类型
     * @param params sql参数
     * @return List<T>
     */
    @Override
    public List<T> executeQuery(String sql, Class<T> clz, Object... params) {
        Query nativeQuery = em.createNativeQuery(sql);
        setParameters(nativeQuery, params);
        nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        /*查询数据集合*/
        List<Map> list = (List<Map>) nativeQuery.getResultList();
        return list.stream().map(m -> JsonUtils.map2Obj(m, clz)).collect(Collectors.toList());
    }

    /**
     * 执行原生sql（增删改）
     *
     * @param sql    要执行的sql
     * @param params sql参数
     * @return 影响的行数rows
     */
    @Override
    public int executeUpdate(String sql, Object... params) {
        Query query = em.createNativeQuery(sql);
        setParameters(query, params);
        return query.executeUpdate();
    }

    /**
     * 设置下标参数
     *
     * @param query  nativeQuery
     * @param params 参数数组
     */
    private static void setParameters(Query query, Object[] params) {
        if (params == null) return;

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
    }

}
 
