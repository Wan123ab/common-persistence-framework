package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : 万强
 * @date : 2019/7/17 14:41
 * @desc : SQL包装类，封装了初始SQL（含占位符）以及参数数组
 * @version 1.0
 */
@Data//自动添加getter/setter,重写toString,equals,hashCode等方法
@NoArgsConstructor//添加无参构造
@AllArgsConstructor//添加全参构造
@Builder//支持建造者模式，实现链式调用
public class SQLWrapper {

    private String sql;

    private Object[] params;

}
