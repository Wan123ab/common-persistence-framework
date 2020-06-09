package cn.wq.persistence.sql.model;

import cn.wq.persistence.sql.dao.BaseDao;
import cn.wq.persistence.sql.jdbc.bean.Criteria;
import cn.wq.persistence.sql.jdbc.bean.Page;
import cn.wq.persistence.sql.jdbc.bean.PageResult;
import cn.wq.persistence.sql.jdbc.utils.SpringContextUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.List;

/**
 * AR（ActiveRecord）模式，是一种领域模型模式，特点是一个模型类对应关系型数据库中的一个表，
 * 而模型类的一个实例对应表中的一行记录。ActiveRecord一直广受动态语言（PHP 、Ruby等）的喜爱，
 * 而Java作为准静态语言，并没有提供相关支持。
 * 此处使用Model封装了单表常用的CRUD操作，实体类只需继承Model即可使用。
 * <p>
 * 设计思路借鉴了Mybatis-plus的Model
 * </p>
 *
 * @version : 1.0
 * @author : 万强
 * @date : 2019/8/3 11:22
 * @desc : 抽象Model，封装了常用的单表CRUD操作
 */
@Data
@SuppressWarnings("all")
public abstract class Model<T extends Model, ID extends Serializable> implements Serializable {

    /**
     * T泛型Class
     */
    @JsonIgnore
    private Class<T> entityClass;

    private BaseDao<ID> baseDao;

    private BaseDao<ID> getBaseDao() {

        if (baseDao == null) {
            baseDao = SpringContextUtils.getBean(BaseDao.class);
        }
        return baseDao;
    }

    public Model() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        entityClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    /**
     * 获取主键ID
     *
     * @return ID
     */
    public abstract ID getId();

    /**
     * 保存
     *
     * @return
     * @throws Exception
     */
    public boolean save() throws Exception {

        return getBaseDao().save(this);
    }

    /**
     * 更新
     *
     * @return
     * @throws Exception
     */
    public boolean update() throws Exception {

        return getBaseDao().update(this);
    }

    /**
     * 批量保存
     *
     * @param list
     * @return
     * @throws Exception
     */
    public boolean batchSave(List<T> list) throws Exception {
        getBaseDao().batchSave(list);
        return true;
    }

    /**
     * 批量更新
     *
     * @param list
     * @return
     * @throws Exception
     */
    public boolean batchUpdate(List<T> list) throws Exception {
        getBaseDao().batchUpdate(list);
        return true;
    }

    /**
     * 删除
     *
     * @return
     * @throws Exception
     */
    public boolean delete() throws Exception {
        getBaseDao().delete(this);
        return true;
    }

    /**
     * 根据条件删除
     *
     * @param criteria
     * @return
     * @throws Exception
     */
    public boolean deleteWithCriteria(Criteria criteria) throws Exception {
        getBaseDao().deleteWithCriteria(criteria, entityClass);
        return true;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    public boolean batchDelete(List<ID> ids) throws Exception {
        getBaseDao().batchDelete(ids, entityClass);
        return true;
    }

    /**
     * 根据主键查询1条数据
     *
     * @param id
     * @return
     * @throws Exception
     */
    public T queryOne(ID id) throws Exception {
        return getBaseDao().queryOne(id, entityClass);
    }

    /**
     * 根据条件查询一条记录
     *
     * @param criteria
     * @return
     * @throws Exception
     */
    public T queryOne(Criteria criteria) throws Exception {
        return getBaseDao().queryOne(criteria, entityClass);
    }

    /**
     * 查询所有数据
     *
     * @return
     * @throws Exception
     */
    public List<T> queryAll() throws Exception {
        return getBaseDao().queryAll(entityClass);
    }

    /**
     * 分页查询
     *
     * @param page
     * @return
     * @throws Exception
     */
    public PageResult<T> pageQuery(Page page) throws Exception {
        return getBaseDao().pageQuery(page, entityClass);
    }

    /**
     * 根据条件分页查询
     *
     * @param page
     * @param criteria
     * @return
     * @throws Exception
     */
    public PageResult<T> pageQueryWithCriteria(Page page, Criteria criteria) throws Exception {
        return getBaseDao().pageQueryWithCriteria(page, criteria, entityClass);
    }

    /**
     * 根据条件查询
     *
     * @param criteria
     * @return
     * @throws Exception
     */
    public List<T> queryWithCriteria(Criteria criteria) throws Exception {
        return getBaseDao().queryWithCriteria(criteria, entityClass);
    }

    /**
     * 查询记录总数
     *
     * @param <T>
     * @return
     * @throws Exception
     */
    public BigInteger queryCount() throws Exception {
        return getBaseDao().queryCount(entityClass);
    }

    /**
     * 查询记录总数
     *
     * @param criteria
     * @param <T>
     * @return
     * @throws Exception
     */
    public BigInteger queryCountWithCriteria(Criteria criteria) throws Exception {
        return getBaseDao().queryCountWithCriteria(criteria, entityClass);
    }


}
 
