package cn.wq.persistence.sql.jdbc.utils;

import cn.wq.persistence.common.util.EntityUtils;
import cn.wq.persistence.common.util.ReflectionUtils;
import cn.wq.persistence.sql.jdbc.bean.*;
import cn.wq.persistence.sql.model.Model;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Auther: 万强
 * @Date: 2019/7/17 11:09
 * @Desc: SQL解析工具类，用于将SQL对象解析成最终的sql以及参数数组(SQLWrapper)
 */
@SuppressWarnings({"all"})
public class SqlBuilderUtils {

    private static final String SPACE = " ";
    private static final String EQUALS = "=";
    private static final String SQL_IN = "IN";
    private static final String SQL_NOT_IN = "NOT IN";
    private static final char IN_START = '(';
    private static final char IN_END = ')';
    private static final String SQL_IS = "IS";
    private static final String SQL_IS_NOT = "IS NOT";
    private static final String SQL_ORDER_BY = "ORDER BY";
    private static final String SQL_GROUP_BY = "GROUP BY";
    private static final String SQL_BETWEEN = "BETWEEN";

    /**
     * 将SQL对象解析成SQLWrapper
     *
     * @param sql
     * @return
     */
    public static SQLWrapper buildSql(SQL sql) {
        SqlType sqlType = sql.getSqlType();
        SQLWrapper sqlWrapper = null;
        switch (sqlType) {
            case SELECT:
                sqlWrapper = buildQuerySql((QuerySQL) sql);
                break;
            case INSERT:
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            default:
                break;
        }
        return sqlWrapper;
    }

    /**
     * 使用查询SQL构建SQLWrapper
     *
     * @param sql
     * @return
     */
    public static SQLWrapper buildQuerySql(QuerySQL sql) {
        StringBuffer sb = new StringBuffer();
        List params = Lists.newArrayList();

        List<BaseUnion> unions = Lists.newArrayList();

        //1、递归封装BaseUnion
        QuerySQL tmp = sql;

        while (tmp.getSqlChild() != null) {
            tmp = tmp.getSqlChild();
            if (tmp.getUnion() != null) {
                unions.add(tmp.getUnion());
            }
        }

        //2、集合元素顺序反转
        Collections.reverse(unions);

        //3、遍历解析SQL
        //3、1 解析第一条SQL
        resolveQuerySql(tmp, sb, params);

        //3、2 解析第一条SQL后面的union/union all
        resolveUnionAndUnionAll(unions, sb, params);

        /**
         * 封装成SQLWrapper并返回
         */
        return new SQLWrapper(sb.toString(), params.toArray());
    }

    /**
     * 解析查询SQL，返回SQLWrapper
     *
     * @param sql
     * @param sb
     * @param params
     */
    public static void resolveQuerySql(QuerySQL sql, StringBuffer sb, List params) {

        /**
         * 拼接SQL 操作符
         */
        sb.append(sql.getSqlType().getType());

        /**
         * 解析需要查询的字段
         */
        resolveQueryFields(sql.getQueryFields(), sb);

        /**
         * 解析需要查询的表
         */
        resolveQueryTables(sql.getQueryTables(), sb);

        /**
         * 解析Join参数
         */
        resolveJoins(sql.getJoins(), sb);

        /**
         * 判断是否有WHERE EXISTS/WHERE NOT EXISTS
         * 如果有，意味着需要解析WHERE EXISTS/WHERE NOT EXISTS，
         * 不需要解析WhereParam
         */
        List<ExistsType> existsTypes = Lists.newArrayList();

        if (CollectionUtils.isEmpty(sql.getExists())) {

            /**
             * 拼接WhereParam参数
             */
            if (!CollectionUtils.isEmpty(sql.getWhereParams())) {
                sb.append(" WHERE ");
                resolveWhereParam(sql.getWhereParams(), sb, params);
            }

            /**
             * 如果存在Criteria条件，那么不解析WhereParam
             */
            if (!CollectionUtils.isEmpty(sql.getCriteriaWrappers())) {
                SQLWrapper sqlWrapper = resolveCriteriaWrapper(sql.getCriteriaWrappers().get(0));
//                sb.append(" WHERE ").append(sqlWrapper.getSql());
                sb.append(sqlWrapper.getSql());
                params.addAll(Arrays.asList(sqlWrapper.getParams()));
            }

        } else {
            existsTypes = sql.getExists().stream().map(BaseExists::getExistsType).collect(Collectors.toList());

            // 如果包含WHERE EXISTS/WHERE NOT EXISTS
            if (existsTypes.contains(ExistsType.WHERE_EXISTS) || existsTypes.contains(ExistsType.WHERE_NOT_EXISTS)) {
                // 过滤其中的WHERE EXISTS/WHERE NOT EXISTS
                List<BaseExists> whereExists = sql.getExists().stream().filter(e -> (ExistsType.WHERE_EXISTS.equals(e.getExistsType())
                        || ExistsType.WHERE_NOT_EXISTS.equals(e.getExistsType()))).collect(Collectors.toList());
                resolveExists(whereExists, sb, params);
            } else {

                /**
                 * 拼接WhereParam参数
                 */
                if (!CollectionUtils.isEmpty(sql.getWhereParams())) {
                    sb.append(" WHERE ");
                    resolveWhereParam(sql.getWhereParams(), sb, params);
                }

                /**
                 * 如果存在Criteria条件，那么不解析WhereParam
                 */
                if (!CollectionUtils.isEmpty(sql.getCriteriaWrappers())) {
                    SQLWrapper sqlWrapper = resolveCriteriaWrapper(sql.getCriteriaWrappers().get(0));
                    sb.append(" WHERE ").append(sqlWrapper.getSql());
                    params.addAll(Arrays.asList(sqlWrapper.getParams()));

                }
            }
        }

        /**
         * 判断是否有AND EXIST/AND NOT EXISTS
         */
        if (!CollectionUtils.isEmpty(sql.getExists()) &&
                (existsTypes.contains(ExistsType.AND_EXISTS) || existsTypes.contains(ExistsType.AND_NOT_EXISTS))) {

            // 过滤其中的AND EXIST/AND NOT EXISTS
            List<BaseExists> andExists = sql.getExists().stream().filter(e -> (ExistsType.AND_EXISTS.equals(e.getExistsType())
                    || ExistsType.AND_NOT_EXISTS.equals(e.getExistsType()))).collect(Collectors.toList());
            resolveExists(andExists, sb, params);
        }

        /**
         * 拼接group by参数
         */
        resolveGroupBy(sql.getGroupByFields(), sb);

        /**
         * 拼接having参数
         */
        resolveHaving(sql.getHaving(), sb);

        /**
         * 拼接order by参数
         */
        resolveOrderBy(sql.getSorts(), sb);

    }

    /**
     * 解析需要查询的字段
     *
     * @param queryFields
     * @param sql
     */
    public static void resolveQueryFields(Set<String> queryFields, StringBuffer sql) {
        queryFields.forEach(f -> sql.append(f).append(", "));
        //去掉最后一个,
        sql.setLength(sql.length() - 2);

    }

    /**
     * 解析需要查询的表
     *
     * @param queryTables
     * @param sql
     */
    public static void resolveQueryTables(Set<String> queryTables, StringBuffer sql) {
        sql.append(" FROM ");
        queryTables.forEach(t -> sql.append(t).append(", "));
        sql.setLength(sql.length() - 2);

    }

    /**
     * 解析Join参数
     *
     * @param joins
     * @param sql
     */
    public static void resolveJoins(List<Join.BaseJoin> joins, StringBuffer sql) {
        joins.forEach(join -> sql.append(join.getJoinSQL()));
    }

    /**
     * 解析EXISTS操作
     *
     * @param exists
     * @param sql
     * @param params
     */
    public static void resolveExists(List<BaseExists> exists, StringBuffer sql, List params) {

        if (!CollectionUtils.isEmpty(exists)) {
            exists.forEach(exist -> {
                sql.append(SPACE).append(exist.getExistsType().getType())
                        .append("( ");
                resolveQuerySql(exist.getSql(), sql, params);
                sql.append(")");
            });
        }

    }

    /**
     * 解析WhereParam参数，并拼接到sql上，需要考虑拼接Criteria的情况
     *
     * @param whereParams
     * @param sql
     * @param params
     */
    public static void resolveWhereParam(List<WhereParam> whereParams, StringBuffer sql, List params) {

        whereParams.forEach(whereParam -> {
            Boolean usePlaceholder = whereParam.getUsePlaceholder();
            String key = whereParam.getKey();
            String opt = whereParam.getOpt();
            Object value = whereParam.getValue();

            if (usePlaceholder) {
                sql.append(key).append(SPACE).append(opt).append(SPACE);
                if (SQL_IN.equalsIgnoreCase(opt)
                        || SQL_NOT_IN.equalsIgnoreCase(opt)) {
                    sql.append(IN_START);
                    if (value instanceof Collection) {
                        ((Collection) value).forEach(v -> {
                            sql.append("?,");
                            params.add(v);
                        });
                        sql.setLength(sql.length() - 1);
                    } else {
                        sql.append("?");
                        params.add(value);
                    }
                    sql.append(IN_END).append(SPACE);
                } else if (SQL_BETWEEN.equalsIgnoreCase(opt)) {
                    sql.append("? AND ?").append(SPACE);
                    params.add(value);
                } else if (SQL_IS.equalsIgnoreCase(opt)) {
                    sql.append(value).append(SPACE);
                } else if (SQL_IS_NOT.equalsIgnoreCase(opt)) {
                    sql.append(value).append(SPACE);
                } else {
                    sql.append("?").append(SPACE);
                    params.add(value);
                }
            }
            /**
             * 不使用占位符，直接原样输出
             */
            else {
                sql.append(key).append(SPACE).append(opt)
                        .append(SPACE).append(value).append(SPACE);
            }
        });

    }

    /**
     * 解析group by参数
     *
     * @param groupByFields
     * @param sql
     */
    public static void resolveGroupBy(Set<String> groupByFields, StringBuffer sql) {
        if (!CollectionUtils.isEmpty(groupByFields)) {
            sql.append(SQL_GROUP_BY).append(SPACE);
            groupByFields.forEach(f -> sql.append(f).append(","));
            sql.setLength(sql.length() - 1);
        }

    }

    /**
     * 解析having参数
     *
     * @param having
     * @param sql
     */
    public static void resolveHaving(StringBuffer having, StringBuffer sql) {
        if (!StringUtils.isEmpty(having)) {
            sql.append(having);
        }

    }

    /**
     * 解析order by参数
     *
     * @param sorts
     * @param sql
     */
    public static void resolveOrderBy(Collection<Sort> sorts, StringBuffer sql) {
        if (!CollectionUtils.isEmpty(sorts)) {
            sql.append(SPACE).append(SQL_ORDER_BY).append(SPACE);
            sorts.forEach(sort -> {
                sql.append(sort.getKey()).append(SPACE).append(sort.getType()).append(",");
            });
            sql.setLength(sql.length() - 1);
            sql.append(SPACE);
        }

    }

    /**
     * 解析union和union all拼接的SQL
     *
     * @param unions
     * @param sql
     * @param params
     */
    public static void resolveUnionAndUnionAll(List<BaseUnion> unions, StringBuffer sql, List params) {

        if (!CollectionUtils.isEmpty(unions)) {
            unions.forEach(u -> {
                //union类型，union或者union all
                sql.append(u.getUnionType().getType())
                        .append(SPACE);
                resolveQuerySql(u.getSql(), sql, params);
            });
        }

    }

    /**
     * 解析SQL里面拼接的CriteriaWrapper
     *
     * @param criteriaWrapper
     * @return
     */
    public static SQLWrapper resolveCriteriaWrapper(CriteriaWrapper criteriaWrapper) {
        StringBuffer sql = new StringBuffer();

        sql.append(criteriaWrapper.getCriteriaType().getType()).append(SPACE).append("(");

        SQLWrapper sqlWrapper = resolveCriteria(criteriaWrapper.getCriteria());
        sqlWrapper.setSql(sql.toString() + sqlWrapper.getSql() + ")");

        return sqlWrapper;
    }

    /**
     * 解析Criteria嵌套条件封装
     *
     * @param criteria
     * @return
     */
    public static SQLWrapper resolveCriteria(Criteria criteria) {
        StringBuffer sql = new StringBuffer();
        ArrayList<Object> params = Lists.newArrayList();

//        sql.append(" WHERE ");

        if (!CollectionUtils.isEmpty(criteria.getWhereParams())) {

            /**
             * 解析whereParams参数
             */
            resolveWhereParam(criteria.getWhereParams(), sql, params);
        }

        /**
         * 解析更加复杂的Criteria
         */
        criteria.getCriteriaWrappers().forEach(c -> {
            sql.append(c.getCriteriaType().getType());
            buildCriteria(c.getCriteria(), sql, params);
            sql.append(SPACE);
        });

        return new SQLWrapper(sql.toString(), params.toArray());
    }


    public static void buildCriteria(Criteria criteria, StringBuffer sql, List params) {
        sql.append(SPACE).append("( ");

        /**
         * 解析whereParams参数
         */
        resolveWhereParam(criteria.getWhereParams(), sql, params);

        sql.append(")");

    }

    /**
     * 生成具体实体类的增、删、改的sql
     *
     * @param sqlType
     * @param <T>
     * @return
     */
    public static <T extends Model> SQLWrapper buildSql(T t, SqlType sqlType) {
        StringBuffer sql = new StringBuffer();
        List<Object> params = new ArrayList<>();

        Class<?> clz = t.getClass();
        String tableName = EntityUtils.getTableName(clz);
        List<String> columnNames = ReflectionUtils.getAllColumnNamesAnnotation(clz, Column.class);
        List<Field> fields = ReflectionUtils.getAllFieldsAnnotationIgnoreGeneratedValue(clz, Column.class);

        sql.append(sqlType.getType()).append(tableName);
        switch (sqlType) {
            case INSERT:
                sql.append("(");
                columnNames.forEach(s -> sql.append(s).append(", "));
                sql.setLength(sql.length() - 2);
                sql.append(") VALUES (");
                columnNames.forEach(s -> sql.append("?, "));
                sql.setLength(sql.length() - 2);
                sql.append(")");

                fields.forEach(f -> {
                    try {
                        /*暴力反射*/
                        f.setAccessible(true);
                        params.add(f.get(t));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

                break;
            case UPDATE:
                sql.append(" SET ");
                columnNames.forEach(s -> sql.append(s).append(" = ?").append(", "));
                sql.setLength(sql.length() - 2);
                sql.append(" WHERE " + EntityUtils.getPK(clz) + " = ? ");

                fields.forEach(f -> {
                    try {
                        /*暴力反射*/
                        f.setAccessible(true);
                        params.add(f.get(t));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

                /*最后加上id = ？对应参数*/
                params.add(t.getId());
                break;
            case DELETE:
                sql.append(" WHERE " + EntityUtils.getPK(clz) + " = ? ");
                params.add(t.getId());
                break;
            default:
                break;
        }

        return new SQLWrapper(sql.toString(), params.toArray());
    }


}
