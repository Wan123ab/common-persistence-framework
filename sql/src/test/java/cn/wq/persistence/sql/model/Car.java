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
@Table(name = "car")
public class Car extends AbstractEntity<Car>{

    @Column(name = "brand")
    private String brand;

    @Column(name = "color")
    private String color;

    @Column(name = "userId")
    private Integer userId;

    @Builder
    public Car(Integer id, Date createDate, Date updateDate, String brand, String color, Integer userId) {
        super(id, createDate, updateDate);
        this.brand = brand;
        this.color = color;
        this.userId = userId;
    }

}
