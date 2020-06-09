package cn.wq.persistence.sql.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: 万强
 * @date: 2019/7/18 10:41
 * @description:
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "house")
public class House extends AbstractEntity<House>{

    @Column(name = "house_addr")
    private String houseAddr;

    @Column(name = "price")
    private Double price;

    @Column(name = "floor_num")
    private Integer floorNum;

    @Column(name = "user_id")
    private Integer userId;

}
