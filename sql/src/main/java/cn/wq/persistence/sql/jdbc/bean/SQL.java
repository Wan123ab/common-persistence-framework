package cn.wq.persistence.sql.jdbc.bean;

import cn.wq.persistence.common.util.EntityUtils;
import cn.wq.persistence.sql.jdbc.function.FieldSupplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SQL封装类，包含常用的查询操作，设计初衷是为了更方便地使用面向对象的方式进行SQL查询
 * 对于非常复杂的查询操作，仍然建议使用原生SQL进行操作
 * 以便于后期维护和调优
 *
 * @author : 万强
 * @date : 2019-7-16 下午8:11
 * @desc : SQL封装类
 * @version  1.0
 */
@Data
@NoArgsConstructor
@SuppressWarnings({"serial"})
public class SQL extends AbstractCriteria<SQL> {

    /**
     * SQL操作类型
     * @see SqlType
     */
    private SqlType sqlType;

    /**
     * 孩子SQL，用于union/union all操作
     */
    private SQL sqlChild;

    /**
     * 查询字段
     */
    private Set<String> queryFields = Sets.newLinkedHashSet();

    /**
     * 查询的表
     */
    private Set<String> queryTables = Sets.newLinkedHashSet();

    /**
     * group by 参数
     */
    private Set<String> groupByFields = Sets.newLinkedHashSet();

    /**
     * having参数
     */
    private StringBuffer having = new StringBuffer();

    /**
     * order by排序参数
     */
    private Set<Sort> sorts = Sets.newLinkedHashSet();

    /**
     * EXISTS/NOT EXISTS 操作
     */
    private List<BaseExists> exists = Lists.newArrayList();

    /**
     * JOIN 操作
     */
    private List<Join.BaseJoin> joins = Lists.newArrayList();

    /**
     * Union/Union All 操作
     */
    private BaseUnion union;

    /**
     * 需要执行update操作的class
     * 2019-12-6 新增update操作
     */
    private Class updateTableClass;


    public SQL(SQL sqlChild) {
        this.sqlChild = sqlChild;
    }

    /*=============================================Select查询操作 START============================================*/
    public SQL select(String... fields) {
        sqlType = SqlType.SELECT;
        queryFields.addAll(Arrays.asList(fields));
        return this;
    }

    /**
     * 当使用lamda表达式表示查询的字段时，用字段名作为查询的列别名，以便于后面查询出来的结果集RS映射成POJO
     * @param suppliers
     * @return
     */
    public SQL select(FieldSupplier<?>... suppliers) {
        sqlType = SqlType.SELECT;
        List<String> collect = Arrays.stream(suppliers).map(FieldSupplier::getColumnNameAndAliasByField).collect(Collectors.toList());
        queryFields.addAll(collect);
        return this;
    }

    public SQL from(String... tableNames) {
        queryTables.addAll(Arrays.asList(tableNames));
        return this;
    }

    /**
     * 当使用Class表示查询的表时，以表名作为查询表的别名
     * @param clzs
     * @return
     */
    public SQL from(Class<?>... clzs) {
        StringBuffer tableNames = new StringBuffer();
        Arrays.stream(clzs).forEach(clz -> {
            tableNames.append(EntityUtils.getTableName(clz)).append(" as ").append(EntityUtils.getTableName(clz)).append(",");
        });
        tableNames.setLength(tableNames.length() - 1);
        queryTables.add(tableNames.toString());
        return this;
    }
    /*=============================================Select查询操作 END============================================*/

    /*=============================================Update更新操作 START============================================*/
    public SQL update(Class updateTableClass){
        this.updateTableClass = updateTableClass;
        return this;
    }

//    public As from(Class<?>... clzs) {
//        return new As(this, clzs);
//    }

//    @Data
//    @NoArgsConstructor
//    public class As{
//
//        private Class[] clzs;
//
//        private String[] alias;
//
//        private SQL sql;
//
//        public As(SQL sql, Class<?>... clzs){
//            this.clzs = clzs;
//            this.sql = sql;
//        }
//
//        public SQL as(String... alias){
//            if(clzs.length != alias.length){
//                throw new IllegalArgumentException("参数错误：表别名数量与表数量不对应！");
//            }
//            StringBuffer tableNames = new StringBuffer();
//            for (int i = 0; i < alias.length; i++) {
//                tableNames.append(EntityUtils.getTableName(clzs[i]) + " as " + alias[i] + ",");
//            }
//            tableNames.setLength(tableNames.length() - 1);
//            queryTables.add(tableNames.toString());
//            return sql;
//        }
//    }


    /**
     * 下面2个where重载方法主要用于多表关联时设置关联条件，如user.id=car.userId
     * @param supplier1
     * @param opt
     * @param supplier2
     * @param <T>
     * @return
     */
    public <T> SQL where(FieldSupplier<T> supplier1, String opt, FieldSupplier<T> supplier2) {
        whereParams.add(WhereParam.builder().usePlaceholder(false).key(supplier1.getColumnNameByFieldDefault()).opt(opt).value(supplier2.getColumnNameByFieldDefault()).build());
        return this;
    }

    public <T> SQL where(FieldSupplier<T> supplier1, FieldSupplier<T> supplier2) {
        return where(supplier1, "=", supplier2);
    }

    public SQL groupBy(String... fields) {
        groupByFields.addAll(Arrays.asList(fields));
        return this;
    }

    /**
     * FieldSupplier<T>换成FieldSupplier<?>，引用处就不警告了，奇怪？？？
     * @param suppliers
     * @return
     */
    public SQL groupBy(FieldSupplier<?>... suppliers) {
        List<String> collect = Arrays.stream(suppliers).map(FieldSupplier::getColumnNameByField).collect(Collectors.toList());
        groupByFields.addAll(collect);
        return this;
    }

    public SQL orderBy(Sort... sorts) {
        this.sorts.addAll(Arrays.asList(sorts));
        return this;
    }

    /**
     * 下面2个and重载方法用于多表关联条件拼接，如a.id = b.aid and b.id = c.bid
     * @param supplier1
     * @param opt
     * @param supplier2
     * @param <T>
     * @return
     */
    public <T> SQL and(FieldSupplier<T> supplier1, String opt, FieldSupplier<T> supplier2) {
        whereParams.add(WhereParam.builder().usePlaceholder(false).key("AND " + supplier1.getColumnNameByFieldDefault()).opt(opt).value(supplier2.getColumnNameByFieldDefault()).build());
        return this;
    }

    public <T> SQL and(FieldSupplier<T> supplier1, FieldSupplier<T> supplier2) {
        return and(supplier1, "=", supplier2);
    }


    /************************************EXISTS 操作 START*********************************/

    public SQL whereExists(SQL sql){
        exists.add(new BaseExists(ExistsType.WHERE_EXISTS, sql));
        return this;
    }

    public SQL andExists(SQL sql){
        exists.add(new BaseExists(ExistsType.AND_EXISTS, sql));
        return this;
    }

    public SQL whereNotExists(SQL sql){
        exists.add(new BaseExists(ExistsType.WHERE_NOT_EXISTS, sql));
        return this;
    }

    public SQL andNotExists(SQL sql){
        exists.add(new BaseExists(ExistsType.AND_NOT_EXISTS, sql));
        return this;
    }

    /************************************EXISTS 操作 END*********************************/

    /*************************************JOIN操作 START*************************************/

    private SQL join(Join.BaseJoin join){
        joins.add(join);
        return this;
    }

    public SQL leftJoin(Join.BaseJoin join){
        join.setJoinType(JoinType.LeftJoin);
        return join(join);
    }

    public SQL rightJoin(Join.BaseJoin join){
        join.setJoinType(JoinType.RightJoin);
        return join(join);
    }

    public SQL innerJoin(Join.BaseJoin join){
        join.setJoinType(JoinType.InnerJoin);
        return join(join);
    }

    public SQL natureJoin(Join.BaseJoin join){
        join.setJoinType(JoinType.NatureJoin);
        return join(join);
    }

    /*************************************JOIN操作 END****************************************/


    /*************************************Union操作 START****************************************/

    public SQL union() {
        SQL nextSQL = new SQL(this);
        union = BaseUnion.builder().unionType(UnionType.UNION).sql(nextSQL).build();
        return nextSQL;
    }

    public SQL unionAll(){
        SQL nextSQL = new SQL(this);
        union = BaseUnion.builder().unionType(UnionType.UNION_ALL).sql(nextSQL).build();
        return nextSQL;
    }

    /*************************************Union操作 END****************************************/


//    public SQL from(SQL sql){
//
//    }
//

    /*************************************Having操作 START****************************************/

    //直接字符操作，having("count(1) > 0")
    public SQL having(String having) {
        this.having.append(" HAVING ").append(having);
        return this;
    }

    //直接字符操作，1、having("count(1)", ">", 0)  2、having(count("1"), ">", 0)
    public SQL having(String func, String opt, Object value){
        this.having.append(" HAVING ").append(func).append(" ").append(opt).append(" ").append(value);
        return this;
    }

    //lamda操作
    public <T> SQL having(FieldSupplier<T> supplier, String opt, Object value){
        return having(supplier.getColumnNameByFieldDefault(), opt, value);
    }

    /*************************************Having操作 END****************************************/



}
