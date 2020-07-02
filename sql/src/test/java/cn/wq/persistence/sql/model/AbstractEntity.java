package cn.wq.persistence.sql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * AbstractEntity: 抽象基类，定义了一些通用字段及审计功能相关字段
 *
 * 审计，是指对数据的创建、变更等生命周期进行审阅的一种机制，
 * 通常审计的属性包括 创建时间、修改时间、创建人、修改人等信息
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 让子类定义的表能拥有继承的字段(列)
 */
@MappedSuperclass
public class AbstractEntity<T extends AbstractEntity> extends Model<T> {

    /**
     * GeneratedValue 用于指定ID主键的生成方式，GenerationType.IDENTITY 指采用数据库原生的自增方式，
     * 对应到 PostGreSQL则会自动采用 BigSerial 做自增类型(匹配Long 类型)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    /**
     * 声明日期类型对应的格式，如TIMESTAMP会对应 yyyy-MM-dd HH:mm:ss的格式，而这个也会被体现到DDL中
     */
    @Column(name = "createDate", nullable = false, updatable = false)
    protected Date createDate;

    @Column(name = "updateDate", nullable = false)
    protected Date updateDate;

}
