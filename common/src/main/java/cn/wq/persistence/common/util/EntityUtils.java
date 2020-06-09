package cn.wq.persistence.common.util;

import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 实体类工具类
 *
 * @date 2019-7-17 下午5:54
 * @Version 1.0
 */
public class EntityUtils {

    public static final Map<Class, String> tableNameMaps = Maps.newConcurrentMap();

    public static final Map<Field, String> columnNameMaps = Maps.newConcurrentMap();

    /**
     * 获取实体类clz的表名
     *
     * @param clz
     * @return
     */
    public static String getTableName(Class<?> clz) {
        if (!tableNameMaps.containsKey(clz)) {
            Table table = clz.getAnnotation(Table.class);
            String tableName;
            if (table == null || StringUtils.isEmpty(table.name())) {
                tableName = clz.getSimpleName();
            } else {
                tableName = table.name();
            }
            tableNameMaps.put(clz, tableName);

        }
        return tableNameMaps.get(clz);
    }

    public static String getColumnName(Field field) {
        if (!columnNameMaps.containsKey(field)) {
            Column column = field.getAnnotation(Column.class);
            String columnName;
            if (column == null || StringUtils.isEmpty(column.name())) {
                columnName = field.getName();
            } else {
                columnName = column.name();
            }
            columnNameMaps.put(field, columnName);

        }
        return columnNameMaps.get(field);
    }

    /**
     * 获取主键信息，默认最多一个主键，不考虑联合主键
     *
     * @param clz
     * @param <T>
     * @return
     */
    public static  <T> String getPK(Class<T> clz) {
        List<Field> pkFields = ReflectionUtils.getAllFieldsAnnotation(clz, Id.class);
        /*没有主键和多个主键的情况不予考虑*/
        if (CollectionUtils.isEmpty(pkFields) || pkFields.size() > 1) {
            return "";
        }
        Field pkField = pkFields.get(0);
        if (pkField.isAnnotationPresent(Column.class)) {
            Column column = pkField.getAnnotation(Column.class);
            if (!StringUtils.isEmpty(column.name())) {
                return column.name();
            }
        }
        return pkField.getName();
    }

}
