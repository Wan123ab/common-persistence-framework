package cn.wq.persistence.jpa_plus.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/6 17:04
 * @desc
 */
@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User extends BaseEntity{

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "sex")
    private Integer sex;

    @Column(name = "age")
    private Integer age;


}
 
