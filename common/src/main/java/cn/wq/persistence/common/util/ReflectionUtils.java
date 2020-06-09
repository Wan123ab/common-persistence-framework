package cn.wq.persistence.common.util;

import org.apache.commons.lang3.reflect.FieldUtils;

import javax.persistence.GeneratedValue;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: 万强
 * @date: 2019/7/17 16:36
 * @desc: 反射工具类
 * @version  1.0
 */
public class ReflectionUtils {

    private static Map<Class<?>, Map<String, Field>> cacheMap = new ConcurrentHashMap<>();

    private static Map<Class<?>, List<String>> cacheEntityColumnNameMap = new ConcurrentHashMap<>();

    /**
     * 获取指定class的所有属性，包括父类的public、protected、default、private属性
     *
     * @param clz
     * @return
     */
    public static List<Field> getAllFields(Class<?> clz) {

        return FieldUtils.getAllFieldsList(clz);
    }

    /**
     * 获取指定clz中所有具有指定annotation的field，并且将自动识别主键的生成策略，如果加了GeneratedValue，那么忽略主键field
     * @param clz
     * @return
     */
    public static List<Field> getAllFieldsAnnotationIgnoreGeneratedValue(Class<?> clz, Class<? extends Annotation> annotationType) {

        List<Field> allFieldsList = FieldUtils.getAllFieldsList(clz);
        List<Field> fields = allFieldsList.stream().filter(t -> t.isAnnotationPresent(annotationType) && !t.isAnnotationPresent(GeneratedValue.class)).collect(Collectors.toList());

        return fields;
    }

    public static List<Field> getAllFieldsAnnotation(Class<?> clz, Class<? extends Annotation> annotationType) {

        List<Field> allFieldsList = FieldUtils.getAllFieldsList(clz);
        List<Field> fields = allFieldsList.stream().filter(t -> t.isAnnotationPresent(annotationType)).collect(Collectors.toList());

        return fields;
    }

    /**
     * 获取指定clz的fieldName--field的Map
     * @param clz 需要分析的Class
     * @return Map<String, Field>
     */
    public static Map<String, Field> getAllFieldNameFieldMap(Class<?> clz) {

        if (!cacheMap.containsKey(clz)) {
            List<Field> allFields = getAllFields(clz);
            Map<String, Field> fieldNameFieldMap = allFields.stream()
                    /*注意：此处对应key的FieldName全部转为大写，以便于通过大小写不一致的key（FieldName）找到Field*/
                    .collect(Collectors.toMap(field -> field.getName().toUpperCase(), Function.identity()));
            cacheMap.put(clz, fieldNameFieldMap);
        }

        return cacheMap.get(clz);
    }

    /**
     * 获取指定clz的所有具有指定annotation的field的columnName
     * @param clz 需要分析的Class
     * @return List<String>
     */
    public static List<String> getAllColumnNamesAnnotation(Class<?> clz, Class<? extends Annotation> annotationType) {

        if (!cacheEntityColumnNameMap.containsKey(clz)) {
            List<Field> allFields = getAllFieldsAnnotation(clz, annotationType);

            List<String> columnNames = allFields.stream().map(EntityUtils::getColumnName).collect(Collectors.toList());
            cacheEntityColumnNameMap.put(clz, columnNames);
        }

        return cacheEntityColumnNameMap.get(clz);
    }

    /**
     * 获取指定Class指定字段名的Field，此处fieldName在查找时将全部转换为大写
     * @param clz 需要分析的Class
     * @param fieldName 字段名
     * @return Field
     */
    public static Field getField(Class<?> clz, String fieldName) {

        if (cacheMap.get(clz) == null || !cacheMap.get(clz).containsKey(fieldName.toUpperCase())) {
            getAllFieldNameFieldMap(clz);
        }

        return cacheMap.get(clz).get(fieldName.toUpperCase());

    }

}
