package cn.wq.persistence.sql;

import cn.wq.persistence.common.util.JsonUtils;
import cn.wq.persistence.common.util.SqlFormatter;
import cn.wq.persistence.sql.jdbc.bean.*;
import cn.wq.persistence.sql.model.Car;
import cn.wq.persistence.sql.model.House;
import cn.wq.persistence.sql.model.User;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.junit.Test;

import static cn.wq.persistence.sql.jdbc.bean.SQLFunc.count;
import static cn.wq.persistence.sql.jdbc.utils.SqlBuilderUtils.buildSql;
import static cn.wq.persistence.sql.jdbc.utils.SqlBuilderUtils.resolveCriteria;


/**
 * @author : 万强
 * @date : 2019/7/17 15:08
 * @desc :
 * @version 1.0
 */
public class TestQuerySQL {

    private SqlFormatter sqlFormatter = new SqlFormatter();

    @Test
    public void select() {
        SQL sql = new SQL().buildQuerySQL()
                .select(new User()::getAge).from(User.class).where(new User()::getName, "张三");

        SQLWrapper sqlWrapper = buildSql(sql);

        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void selectSub() {

        SQL sql = new SQL().buildQuerySQL()
                .select(new User()::getAge, new Car()::getBrand, new User()::getName)
                .from(User.class, Car.class)
                .where(new User()::getId, new Car()::getUserId)
                .and(new User()::getName, "张三")
                .and(new Car()::getBrand, "大众斯柯达")
                .and(new Car()::getColor, "黑色")
                .andIfAbsent(new Car()::getUserId, 1)
                .groupBy(new Car()::getBrand, new User()::getAge)
                .having(count(new User()::getId), ">", 3)
                .orderBy(new Sort(new Car()::getCreateDate));

        SQLWrapper sqlWrapper = buildSql(sql);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void selectJoin() {

        SQL sql = new SQL().buildQuerySQL()
                .select(new User()::getAge, new Car()::getBrand, new User()::getName, new House()::getHouseAddr)
                .from(User.class)
                .leftJoin(new Join().with(Car.class).on(new User()::getId, new Car()::getUserId))
                .rightJoin(new Join().with(House.class).on(new User()::getId, new House()::getUserId))
                .where(new House()::getFloorNum, 4)
                .and(new User()::getName, "张三")
                .and(new Car()::getBrand, "大众斯柯达")
                .and(new Car()::getColor, "黑色")
                .andIfAbsent(new Car()::getUserId, "")
                .orderBy(new Sort(new Car()::getCreateDate));

        SQLWrapper sqlWrapper = buildSql(sql);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void selectUnion() {

        SQL sql = new SQL().buildQuerySQL()
                .select(User::new)
                .from(User.class)
                .leftJoin(new Join().with(Car.class).on(new User()::getId, new Car()::getUserId))
                .rightJoin(new Join().with(House.class).on(new User()::getId, new House()::getUserId))
                .where(new House()::getFloorNum, 4)
                .and(new User()::getName, "张三")
                .and(new Car()::getBrand, "大众斯柯达")
                .and(new Car()::getColor, "黑色")
                .andIfAbsent(new Car()::getUserId, "")
                .orderBy(new Sort(new Car()::getCreateDate))
                .union()
                .select(Car::new).from(Car.class).where(new Car()::getColor, "白色")
                .unionAll()
                .select(House::new).from(House.class).where(new House()::getFloorNum, 4);

        SQLWrapper sqlWrapper = buildSql(sql);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JsonUtils.obj2Json(sqlWrapper.getParams()));
    }

    /**
     * 查询所有字段，User::new表示查询User所有字段
     */
    @Test
    public void selectAllField() {

        SQL sql = new SQL().buildQuerySQL()
                .select(User::new, new Car()::getBrand, new House()::getHouseAddr)
                .from(User.class)
                .leftJoin(new Join().with(Car.class).on(new User()::getId, new Car()::getUserId))
                .rightJoin(new Join().with(House.class).on(new User()::getId, new House()::getUserId))
                .where(new House()::getFloorNum, 4)
                .and(new User()::getName, "张三")
                .and(new Car()::getBrand, "大众斯柯达")
                .and(new Car()::getColor, "黑色")
                .andIfAbsent(new Car()::getUserId, "")
                .orderBy(new Sort(new Car()::getCreateDate));

        SQLWrapper sqlWrapper = buildSql(sql);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void selectAndExists() {

        SQL sql = new SQL().buildQuerySQL()
                .select(User::new, new Car()::getBrand, new House()::getHouseAddr)
                .from(User.class)
                .leftJoin(new Join().with(Car.class).on(new User()::getId, new Car()::getUserId))
                .rightJoin(new Join().with(House.class).on(new User()::getId, new House()::getUserId))
                .where(new House()::getFloorNum, 4)
                .and(new House()::getId, "IN", Lists.newArrayList(1,2,3))
                .andLike(new User()::getName, LikeType.LEFT_LIKE, "张三")
                .andLike(new Car()::getBrand ,LikeType.LIKE, "大众斯柯达")
                .andLike(new Car()::getColor,LikeType.RIGHT_LIKE, "黑色")
                .andIfAbsent(new Car()::getUserId, "")
                .andExists(new SQL().buildQuerySQL().select("1").from(House.class).where(new User()::getId, new House()::getUserId))
                .andNotExists(new SQL().buildQuerySQL().select("1").from(House.class).where(new User()::getId, new House()::getUserId))
                .orderBy(new Sort(new Car()::getCreateDate));


        SQLWrapper sqlWrapper = buildSql(sql);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void selectWhereExists() {

        SQL sql = new SQL().buildQuerySQL()
                .select(User::new, new Car()::getCreateDate, new House()::getUpdateDate)
                .from(User.class)
                .leftJoin(new Join().with(Car.class).on(new User()::getId, new Car()::getUserId))
                .rightJoin(new Join().with(House.class).on(new User()::getId, new House()::getUserId))
                .whereExists(new SQL().buildQuerySQL().select("1").from(House.class).where(new User()::getId, new House()::getUserId))
                .andNotExists(new SQL().buildQuerySQL().select("1").from(House.class).where(new User()::getId, new House()::getUserId))
                .orderBy(new Sort(new Car()::getCreateDate));

        SQLWrapper sqlWrapper = buildSql(sql);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void testCriteria(){
        Criteria criteria = new Criteria()
                .where("a","b")
                .and("c","d")
                .andCriteria(new Criteria().whereIsNull("e").or("f",">","1"));

        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void testCriteria1(){
        Criteria criteria = new Criteria()
                .withCriteria(new Criteria().whereIsNull("e").or("f",">","1"))
                .orCriteria(new Criteria().where("a","b").and("c","d"));

        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void testCriteria2() {
        Criteria criteria = new Criteria()
                .where("g", "h")
                .andCriteria(new Criteria().whereIsNull("e").or("f", ">", "1"))
                .orCriteria(new Criteria().where("a", "b").and("c", "d"));

        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void testCriteria3() {
        Criteria criteria = new Criteria()
                .withCriteria(new Criteria().where("g", "h").andIsNull("e").or("f", ">", "1"))
                .orCriteria(new Criteria().where("a", "b").and("c", "d"));

        SQLWrapper sqlWrapper = resolveCriteria(criteria);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void testSQLCriteria1() {

        Criteria criteria = new Criteria()
                .withCriteria(new Criteria().where("g", "h").andIsNull("e").or("f", ">", "1"))
                .orCriteria(new Criteria().where("a", "b").and("c", "d"));

        SQL sql = new SQL().buildQuerySQL()
                .select(new Car()::getBrand,new User()::getName)
                .from(User.class)
                .leftJoin(new Join().with(Car.class).on(new User()::getId, new Car()::getUserId))
                .whereCriteria(criteria);

        SQLWrapper sqlWrapper = buildSql(sql);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }

    @Test
    public void testSQLCriteria2() {

        Criteria criteria = new Criteria().where("g", "h").andIsNull("e").or("f", ">", "1")
                .orCriteria(new Criteria().where("a", "b").and("c", "d"));

        SQL sql = new SQL().buildQuerySQL().select(new Car()::getBrand,new User()::getName).from(Car.class, User.class)
                .where(new Car()::getUserId, new User()::getId)
                .andCriteria(criteria);

        SQLWrapper sqlWrapper = buildSql(sql);
        System.err.println(sqlFormatter.format(sqlWrapper.getSql()));
        System.err.println(JSON.toJSON(sqlWrapper.getParams()));
    }


}
