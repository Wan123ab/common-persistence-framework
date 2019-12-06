package cn.wq.persistence.sql.jdbc.utils;

import cn.wq.persistence.common.util.ReflectionUtils;
import cn.wq.persistence.sql.jdbc.bean.SQLWrapper;
import cn.wq.persistence.sql.jdbc.bean.SqlType;
import cn.wq.persistence.sql.table.Table;
import cn.wq.persistence.sql.table.TableColumn;
import cn.wq.persistence.sql.table.TableField;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 万强
 * @version 1.0
 * @date 2019/11/26 11:21
 * @desc
 */
public class SqlTableUtils {

    private final static Map<Class<?>, Table<?>> cacheTable = new ConcurrentHashMap<>();

    /**
     * 将实体类Class解析为Table
     * @param entityClass 实体类Class
     * @param <T> 实体类泛型
     * @return Table
     */
    public static <T> Table<T> resolve2Table(Class<T> entityClass) {

        if (cacheTable.containsKey(entityClass)) {
            return (Table<T>) cacheTable.get(entityClass);
        }
        javax.persistence.Table tableAnnotation = entityClass.getAnnotation(javax.persistence.Table.class);

        Table<T> table = null;
        if (tableAnnotation != null) {
            table = new Table<>();
            String tableName = tableAnnotation.name();
            String schema = tableAnnotation.schema();
            table.setTableName(tableName);
            table.setSchema(schema);

            List<Field> allFields = ReflectionUtils.getAllFields(entityClass);

            Set<TableField<T>> tableFieldSet = new HashSet<>();
            for (Field field : allFields) {
                tableFieldSet.add(
                        TableField.<T>builder()
                                .field(field)
                                .fieldName(field.getName())
                                .javaType(field.getType())
                                .build());
            }

            Set<TableColumn<T>> tableColumnSet = new HashSet<>();
            for (TableField<T> tableField : tableFieldSet) {
                Field field = tableField.getField();
                if (field.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    tableColumnSet.add(
                            TableColumn.<T>builder()
                                    .isPK(field.isAnnotationPresent(Id.class))
                                    .columnName(column.name())
                                    .fieldName(field.getName())
                                    .javaType(field.getType())
                                    .insertAble(column.insertable())
                                    .updateAble(column.updatable())
                                    .tableField(tableField)
                                    .build());
                }
            }

            table.setTableColumns(tableColumnSet);

            cacheTable.put(entityClass, table);
        }
        return table;
    }

    /**
     * 根据实体类对象和Sql操作类型构建SQLWrapper
     * @param t 实体类对象
     * @param sqlType sql操作类型
     * @param <T> 实体类泛型
     * @return SQLWrapper
     */
    public static <T> SQLWrapper buildSql(T t, SqlType sqlType){
        switch (sqlType) {
            case INSERT:
                return buildInsertSql(t);
            case DELETE:
                return buildDeleteSql(t);
            case UPDATE:
                return buildUpdateSql(t);
            default:
                return null;
        }
    }

    /**
     * 根据实体对象构建Insert SQLWrapper
     * @param t 实体类对象
     * @param <T> 实体类泛型
     * @return SQLWrapper
     */
    public static <T> SQLWrapper buildInsertSql(T t){
        if (t == null) return null;

        Class<T> clz = (Class<T>) t.getClass();
        Table<T> table = resolve2Table(clz);

        if (table == null) return null;

        String tableName = table.getTableName();

        /*1、解析成sql，含占位符*/
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ").append(tableName).append(" (");
        table.getTableColumns().stream().filter(TableColumn::isInsertAble)
                .forEach(tableColumn -> sql.append(tableColumn.getColumnName()).append(", "));

        sql.setLength(sql.length() - 2);
        sql.append(") VALUES (");

        table.getTableColumns().forEach(tableColumn -> sql.append("?, "));
        sql.setLength(sql.length() - 2);
        sql.append(")");

        /*2、解析参数数组*/
        List<Object> params = new ArrayList<>();
        table.getTableColumns().stream().filter(TableColumn::isInsertAble)
                .forEach(tableColumn -> {
                    TableField<T> tableField = tableColumn.getTableField();
                    try {
                        params.add(tableField.getValue(t));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return new SQLWrapper(sql.toString(), params.toArray());
    }

    /**
     * 根据实体对象构建Update SQLWrapper
     * @param t 实体类对象
     * @param <T> 实体类泛型
     * @return SQLWrapper
     */
    public static <T> SQLWrapper buildUpdateSql(T t){
        if (t == null) return null;

        Class<T> clz = (Class<T>) t.getClass();
        Table<T> table = resolve2Table(clz);

        if (table == null) return null;

        String tableName = table.getTableName();

        /*1、解析成sql，含占位符*/
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ").append(tableName).append(" SET ");

        /*只更新允许修改并且不是主键的字段*/
        table.getTableColumns().stream().filter(TableColumn::isUpdateAble).filter((TableColumn::isNotPK))
                .forEach(tableColumn -> sql.append(tableColumn.getColumnName()).append(" = ?, "));

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE ");

        table.getTableColumns().stream().filter(TableColumn::isPK)
                .forEach(tableColumn -> sql.append(tableColumn.getColumnName()).append(" = ? AND "));

        sql.setLength(sql.length() - 5);

        /*2、解析参数数组*/
        List<Object> params = new ArrayList<>();
        table.getTableColumns().stream().filter(TableColumn::isUpdateAble).filter((TableColumn::isNotPK))
                .forEach(tableColumn -> {
                    TableField<T> tableField = tableColumn.getTableField();
                    try {
                        params.add(tableField.getValue(t));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        table.getTableColumns().stream().filter(TableColumn::isPK)
                .forEach(tableColumn -> {
                    TableField<T> tableField = tableColumn.getTableField();
                    try {
                        params.add(tableField.getValue(t));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return new SQLWrapper(sql.toString(), params.toArray());
    }

    /**
     * 根据实体对象构建Delete SQLWrapper
     * @param t 实体类对象
     * @param <T> 实体类泛型
     * @return SQLWrapper
     */
    public static <T> SQLWrapper buildDeleteSql(T t){
        if (t == null) return null;

        Class<T> clz = (Class<T>) t.getClass();
        Table<T> table = resolve2Table(clz);

        if (table == null) return null;

        String tableName = table.getTableName();

        /*1、解析成sql，含占位符*/
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ").append(tableName).append(" WHERE ");

        table.getTableColumns().stream().filter(TableColumn::isPK)
                .forEach(tableColumn -> sql.append(tableColumn.getColumnName()).append(" = ?"));

        /*2、解析参数数组*/
        List<Object> params = new ArrayList<>();

        table.getTableColumns().stream().filter(TableColumn::isPK)
                .forEach(tableColumn -> {
                    TableField<T> tableField = tableColumn.getTableField();
                    try {
                        params.add(tableField.getValue(t));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return new SQLWrapper(sql.toString(), params.toArray());
    }



}
 
