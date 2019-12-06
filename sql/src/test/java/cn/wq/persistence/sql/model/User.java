package cn.wq.persistence.sql.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sys_user")
public class User extends AbstractEntity<User> {

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "address")
    private String address;

    /**
     * lombok子类继承父类时，使用Builder无法设置父类属性，
     * 所以此处只能手写全参构造
     * @param id
     * @param createDate
     * @param updateDate
     * @param name
     * @param age
     * @param address
     */
    @Builder
    public User(Integer id, Date createDate, Date updateDate, String name, Integer age, String address) {
        super(id, createDate, updateDate);
        this.name = name;
        this.age = age;
        this.address = address;
    }


}
