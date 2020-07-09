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
@Table(name = "person")
@NoArgsConstructor
public class Person extends BaseEntity{

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "birthday")
    private Date birthday;


}
 
