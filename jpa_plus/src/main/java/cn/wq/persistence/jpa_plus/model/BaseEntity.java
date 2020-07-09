package cn.wq.persistence.jpa_plus.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/6 16:32
 * @desc 抽象基类，集成审计功能
 */
/*
    开启审计功能，使下面4个注解生效
    1、@CreatedBy
    2、@CreatedDate
    3、@LastModifiedBy
    4、@LastModifiedDate
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {

    /**
     * ID
     * Id表示该字段对应数据库表的主键id
     * GeneratedValue中strategy表示使用数据库自带的主键生成策略
     * GeneratedValue中generator配置为"JDBC",在数据插入完毕之后,会自动将主键id填充到实体类中.类似普通mapper.xml中配置的selectKey标签
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "created_by", nullable = true)
    protected Long createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = true)
    protected Date createTime;

    /**
     * 修改人
     */
    @LastModifiedBy
    @Column(name = "update_by", nullable = true)
    protected Long updateBy;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "update_time", nullable = true)
    protected Date updateTime;

    /**
     * 记录状态，0-正常 1-逻辑删除
     */
    @Column(name = "status", nullable = false)
    protected int status;

}
 
