package cn.wq.persistence.jpa_plus.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/6 16:14
 * @desc 集成一些通用的CRUD方法
 */
public interface BaseService<T, ID> {

    /**
     * 保存1个实体对象并返回
     *
     * @param entity 需要保存的实体对象
     * @param <S>    泛型
     * @return 实体对象
     */
    <S extends T> S save(S entity);

    /**
     * 保存1个实体对象集合并返回
     *
     * @param entities 实体对象集合
     * @param <S>      泛型
     * @return 实体对象集合
     */
    <S extends T> List<S> saveAll(Iterable<S> entities);

    /**
     * 通过主键id查询一个对象
     *
     * @param id 主键
     * @return 实体对象
     */
    Optional<T> findById(ID id);

    /**
     * 判断指定id的对象是否存在
     *
     * @param id 主键
     * @return boolean
     */
    boolean existsById(ID id);

    /**
     * 查询所有的实体对象
     *
     * @return 实体对象集合
     */
    List<T> findAll();

    /**
     * 通过id集合查询实体对象
     *
     * @param ids 主键集合
     * @return 实体对象集合
     */
    List<T> findAllById(Iterable<ID> ids);

    /**
     * 查询实体对象总记录数
     *
     * @return 总记录数
     */
    long count();

    /**
     * 通过给定id删除对象
     *
     * @param id 主键
     */
    void deleteById(ID id);

    /**
     * 通过给定id逻辑删除对象
     *
     * @param id 主键
     */
    void deleteByIdWithLogical(ID id);

    /**
     * 删除给定的实体对象
     *
     * @param entity 实体对象
     */
    void delete(T entity);

    /**
     * 逻辑删除给定的实体对象
     *
     * @param entity 实体对象
     */
    void deleteWithLogical(T entity);

    /**
     * 删除给定的实体对象集合
     *
     * @param entities 实体对象集合
     */
    void deleteAll(Iterable<? extends T> entities);

    /**
     * 逻辑删除给定的实体对象集合
     *
     * @param entities 实体对象集合
     */
    void deleteAllWithLogical(Iterable<? extends T> entities);

    /**
     * 删除所有的实体对象
     */
    void deleteAll();

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return page
     */
    Page<T> findAllByPage(Pageable pageable);

    /**
     * 通过条件分页查询
     *
     * @param example  查询条件
     * @param pageable 分页参数
     * @return page
     */
    Page<T> findAllByPage(Example<T> example, Pageable pageable);

    /**
     * 排序查询所有实体对象
     *
     * @param sort 排序
     * @return list
     */
    List<T> findAllBySort(Sort sort);

    /**
     * 通过条件排序查询
     *
     * @param example 查询条件
     * @param sort    排序
     * @return list
     */
    List<T> findAllBySort(Example<T> example, Sort sort);

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
 
