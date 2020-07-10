package cn.wq.persistence.jpa_plus.repository;

import cn.wq.persistence.jpa_plus.dto.IMcIdentifierDto;
import cn.wq.persistence.jpa_plus.dto.McIdentifierDto;
import cn.wq.persistence.jpa_plus.model.McIdentifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/3 14:10
 * @desc McIdentifier Dao
 */
public interface McIdentifierRepository extends BaseRepository<McIdentifier> {

    /**
     * 返回自定义接口类型的DTO，实际上JPA会生成一个jdk动态代理实现类
     * 注意：JPA会通过解析方法名来获取查询字段，要特别注意实体类中字段首字母必须小写不能大写
     * (提供了findByCollectionIDIgnoreCase，但是似乎没有用？？？)
     * @param CollectionID
     * @return IMcIdentifierDto
     */
    List<IMcIdentifierDto> findByCollectionID(Integer CollectionID);

    /**
     * 返回自定义Class类型的DTO
     * 注意：
     * 1、JPA会通过解析方法名来获取查询字段，要特别注意实体类中字段首字母必须小写不能大写
     * 2、这种方式DTO必须添加全参构造器！！！
     * @param CollectionID
     * @return IMcIdentifierDto
     */
    List<McIdentifierDto> queryByCollectionID(Integer CollectionID);

    /**
     * 返回结果泛型化
     * @param CollectionID
     * @param clz 返回的pojo，可以是自定义DTO，也可以是实体类
     * @param <T>
     * @return
     */
    <T> List<T> getByCollectionID(Integer CollectionID, Class<T> clz);

    /**
     * 分页查询
     * @param CollectionID
     * @param pageable 分页参数
     * @param clz 返回的pojo，可以是自定义DTO，也可以是实体类
     * @param <T>
     * @return
     */
    <T> Page<T> readByCollectionID(Integer CollectionID, Pageable pageable, Class<T> clz);

}
