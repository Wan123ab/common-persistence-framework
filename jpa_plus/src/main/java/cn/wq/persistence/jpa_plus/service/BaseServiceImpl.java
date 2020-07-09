package cn.wq.persistence.jpa_plus.service;

import cn.wq.persistence.jpa_plus.model.BaseEntity;
import cn.wq.persistence.jpa_plus.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/6 17:38
 * @desc service基类，包含一些通用的CRUD方法
 */
public abstract class BaseServiceImpl<T extends BaseEntity, Dao extends BaseRepository<T>> implements BaseService<T, Long> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Dao repository;

    /**
     * 保存1个实体对象并返回
     *
     * @param entity 需要保存的实体对象
     * @param <S>    泛型
     * @return 实体对象
     */
    @Override
    public <S extends T> S save(S entity) {
        return repository.save(entity);
    }

    /**
     * 保存1个实体对象集合并返回
     *
     * @param entities 实体对象集合
     * @param <S>      泛型
     * @return 实体对象集合
     */
    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    /**
     * 通过主键id查询一个对象
     *
     * @param id 主键
     * @return 实体对象
     */
    @Override
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * 判断指定id的对象是否存在
     *
     * @param id 主键
     * @return boolean
     */
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    /**
     * 查询所有的实体对象
     *
     * @return 实体对象集合
     */
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * 通过id集合查询实体对象
     *
     * @param ids 主键集合
     * @return 实体对象集合
     */
    @Override
    public List<T> findAllById(Iterable<Long> ids) {
        return repository.findAllById(ids);
    }

    /**
     * 查询实体对象总记录数
     *
     * @return 总记录数
     */
    @Override
    public long count() {
        return repository.count();
    }

    /**
     * 通过给定id删除对象
     *
     * @param id 主键
     */
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    /**
     * 通过给定id逻辑删除对象
     *
     * @param id 主键
     */
    @Override
    public void deleteByIdWithLogical(Long id) {
        Optional<T> optional = findById(id);
        if (optional.isPresent()) {
            T t = optional.get();
            t.setStatus(1);
            save(t);
        }
    }

    /**
     * 删除给定的实体对象
     *
     * @param entity 实体对象
     */
    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    /**
     * 逻辑删除给定的实体对象
     *
     * @param entity 实体对象
     */
    @Override
    public void deleteWithLogical(T entity) {
        entity.setStatus(1);
        save(entity);
    }

    /**
     * 删除所有的实体对象
     */
    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        repository.deleteAll(entities);
    }

    /**
     * 逻辑删除给定的实体对象集合
     *
     * @param entities 实体对象集合
     */
    @Override
    public void deleteAllWithLogical(Iterable<? extends T> entities) {
        entities.forEach(t -> t.setStatus(1));
        saveAll(entities);
    }

    /**
     * 删除所有的实体对象
     */
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return page
     */
    @Override
    public Page<T> findAllByPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * 通过条件分页查询
     *
     * @param example  查询条件
     * @param pageable 分页参数
     * @return page
     */
    @Override
    public Page<T> findAllByPage(Example<T> example, Pageable pageable) {
        return repository.findAll(example, pageable);
    }

    /**
     * 排序查询所有实体对象
     *
     * @param sort 排序
     * @return list
     */
    @Override
    public List<T> findAllBySort(Sort sort) {
        return repository.findAll(sort);
    }

    /**
     * 通过条件排序查询
     *
     * @param example 查询条件
     * @param sort    排序
     * @return list
     */
    @Override
    public List<T> findAllBySort(Example<T> example, Sort sort) {
        return repository.findAll(example, sort);
    }

    /**
     * 根据原生sql进行查询，返回指定clz的结果集
     *
     * @param sql    要执行的sql
     * @param clz    查询返回对象类型
     * @param params sql参数
     * @return List<T>
     * @throws Exception 异常
     */
    @Override
    public List<T> executeQuery(String sql, Class<T> clz, Object... params) {
        return repository.executeQuery(sql, clz, params);
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
        return repository.executeUpdate(sql, params);
    }

}
 
