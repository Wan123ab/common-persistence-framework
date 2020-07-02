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
 * @author 万强
 * @version 1.0
 * @date 2019/12/6 17:27
 * @desc 查询SQL，SQL<QuerySQL>中的QuerySQL用于声明泛型，便于链式调用
 */
@Data
@NoArgsConstructor
public class QuerySQL extends SQL<QuerySQL> {
    /**
     * 孩子SQL，用于union/union all操作
     */
    private QuerySQL sqlChild;

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
     * 分页参数
     */
    private Integer limit;

    private Integer offset;

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

    public QuerySQL(QuerySQL sqlChild) {
        this.sqlChild = sqlChild;
    }

    public QuerySQL select(String... fields) {
        sqlType = SqlType.SELECT;
        queryFields.addAll(Arrays.asList(fields));
        return this;
    }

    /**
     * 当使用lamda表达式表示查询的字段时，用字段名作为查询的列别名，以便于后面查询出来的结果集RS映射成POJO
     *
     * @param suppliers
     * @return
     */
    public QuerySQL select(FieldSupplier<?>... suppliers) {
        sqlType = SqlType.SELECT;
        List<String> collect = Arrays.stream(suppliers).map(FieldSupplier::getColumnNameAndAliasByField).collect(Collectors.toList());
        queryFields.addAll(collect);
        return this;
    }

    public QuerySQL from(String... tableNames) {
        queryTables.addAll(Arrays.asList(tableNames));
        return this;
    }

    /**
     * 当使用Class表示查询的表时，以表名作为查询表的别名
     *
     * @param clzs
     * @return
     */
    public QuerySQL from(Class<?>... clzs) {
        StringBuffer tableNames = new StringBuffer();
        Arrays.stream(clzs).forEach(clz -> {
            tableNames.append(EntityUtils.getTableName(clz)).append(" as ").append(EntityUtils.getTableName(clz)).append(",");
        });
        tableNames.setLength(tableNames.length() - 1);
        queryTables.add(tableNames.toString());
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
//        private QuerySQL sql;
//
//        public As(QuerySQL sql, Class<?>... clzs){
//            this.clzs = clzs;
//            this.sql = sql;
//        }
//
//        public QuerySQL as(String... alias){
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
     *
     * @param supplier1
     * @param opt
     * @param supplier2
     * @param <T>
     * @return
     */
    public <T> QuerySQL where(FieldSupplier<T> supplier1, String opt, FieldSupplier<T> supplier2) {
        whereParams.add(WhereParam.builder().usePlaceholder(false).key(supplier1.getColumnNameByFieldDefault()).opt(opt).value(supplier2.getColumnNameByFieldDefault()).build());
        return this;
    }

    public <T> QuerySQL where(FieldSupplier<T> supplier1, FieldSupplier<T> supplier2) {
        return where(supplier1, "=", supplier2);
    }

    public QuerySQL groupBy(String... fields) {
        groupByFields.addAll(Arrays.asList(fields));
        return this;
    }

    /**
     * FieldSupplier<T>换成FieldSupplier<?>，引用处就不警告了，奇怪？？？
     *
     * @param suppliers
     * @return
     */
    public QuerySQL groupBy(FieldSupplier<?>... suppliers) {
        List<String> collect = Arrays.stream(suppliers).map(FieldSupplier::getColumnNameByField).collect(Collectors.toList());
        groupByFields.addAll(collect);
        return this;
    }

    public QuerySQL orderBy(Sort... sorts) {
        this.sorts.addAll(Arrays.asList(sorts));
        return this;
    }

    public QuerySQL limit(Integer limit){
        this.limit = limit;
        return this;
    }

    public QuerySQL offset(Integer offset){
        this.offset = offset;
        return this;
    }

    /**
     * 下面2个and重载方法用于多表关联条件拼接，如a.id = b.aid and b.id = c.bid
     *
     * @param supplier1
     * @param opt
     * @param supplier2
     * @param <T>
     * @return
     */
    public <T> QuerySQL and(FieldSupplier<T> supplier1, String opt, FieldSupplier<T> supplier2) {
        whereParams.add(WhereParam.builder().usePlaceholder(false).key("AND " + supplier1.getColumnNameByFieldDefault()).opt(opt).value(supplier2.getColumnNameByFieldDefault()).build());
        return this;
    }

    public <T> QuerySQL and(FieldSupplier<T> supplier1, FieldSupplier<T> supplier2) {
        return and(supplier1, "=", supplier2);
    }


    /************************************EXISTS 操作 START*********************************/

    public QuerySQL whereExists(QuerySQL sql) {
        exists.add(new BaseExists(ExistsType.WHERE_EXISTS, sql));
        return this;
    }

    public QuerySQL andExists(QuerySQL sql) {
        exists.add(new BaseExists(ExistsType.AND_EXISTS, sql));
        return this;
    }

    public QuerySQL whereNotExists(QuerySQL sql) {
        exists.add(new BaseExists(ExistsType.WHERE_NOT_EXISTS, sql));
        return this;
    }

    public QuerySQL andNotExists(QuerySQL sql) {
        exists.add(new BaseExists(ExistsType.AND_NOT_EXISTS, sql));
        return this;
    }

    /************************************EXISTS 操作 END*********************************/

    /*************************************JOIN操作 START*************************************/

    private QuerySQL join(Join.BaseJoin join) {
        joins.add(join);
        return this;
    }

    public QuerySQL leftJoin(Join.BaseJoin join) {
        join.setJoinType(JoinType.LeftJoin);
        return join(join);
    }

    public QuerySQL rightJoin(Join.BaseJoin join) {
        join.setJoinType(JoinType.RightJoin);
        return join(join);
    }

    public QuerySQL innerJoin(Join.BaseJoin join) {
        join.setJoinType(JoinType.InnerJoin);
        return join(join);
    }

    public QuerySQL natureJoin(Join.BaseJoin join) {
        join.setJoinType(JoinType.NatureJoin);
        return join(join);
    }

    /*************************************JOIN操作 END****************************************/


    /*************************************Union操作 START****************************************/

    public QuerySQL union() {
        QuerySQL nextSQL = new QuerySQL(this);
        union = BaseUnion.builder().unionType(UnionType.UNION).sql(nextSQL).build();
        return nextSQL;
    }

    public QuerySQL unionAll() {
        QuerySQL nextSQL = new QuerySQL(this);
        union = BaseUnion.builder().unionType(UnionType.UNION_ALL).sql(nextSQL).build();
        return nextSQL;
    }

    /*************************************Union操作 END****************************************/


//    public QuerySQL from(QuerySQL sql){
//
//    }
//

    /*************************************Having操作 START****************************************/

    //直接字符操作，having("count(1) > 0")
    public QuerySQL having(String having) {
        this.having.append(" HAVING ").append(having);
        return this;
    }

    //直接字符操作，1、having("count(1)", ">", 0)  2、having(count("1"), ">", 0)
    public QuerySQL having(String func, String opt, Object value) {
        this.having.append(" HAVING ").append(func).append(" ").append(opt).append(" ").append(value);
        return this;
    }

    //lamda操作
    public <T> QuerySQL having(FieldSupplier<T> supplier, String opt, Object value) {
        return having(supplier.getColumnNameByFieldDefault(), opt, value);
    }

    /*************************************Having操作 END****************************************/

}
 
