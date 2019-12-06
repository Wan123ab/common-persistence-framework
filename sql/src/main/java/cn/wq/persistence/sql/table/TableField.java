package cn.wq.persistence.sql.table;

import lombok.Builder;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author 万强
 * @version 1.0
 * @date 2019/11/23 16:16
 * @desc Table字段对象
 */
@Data
@Builder
public class TableField<T> {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 属性field
     */
    private Field field;

    /**
     * 属性field所属类型
     */
    private Class<?> javaType;

    /**
     * 判断当前field是否有annotationClass注解
     * @param annotationClass field上的注解
     * @return boolean
     */
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        boolean result = false;
        if (field != null) {
            result = field.isAnnotationPresent(annotationClass);
        }
        return result;
    }

    /**
     * 获取当前字段上的指定注解
     * @param annotationClass 指定注解
     * @param <A> 泛型
     * @return A 指定注解
     */
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        A result = null;
        if (field != null) {
            result = field.getAnnotation(annotationClass);
        }
        return result;
    }

    /**
     * 获取指定实例的当前field的value
     * @param t 实例对象
     * @return Object 当前field的值
     * @throws IllegalAccessException 异常
     */
    public Object getValue(T t) throws IllegalAccessException {
        Object result = null;
        if (field != null) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            result = field.get(t);
        }
        return result;
    }

}
 
