package cn.wq.persistence.sql.jdbc.function;

import cn.wq.persistence.common.util.EntityUtils;
import cn.wq.persistence.common.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import java.beans.Introspector;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

/**
 * 函数式接口，用于封装和解析用lamda表达式（函数引用）表示的字段入参，如User::getId
 * 更加面向对象，同时使用者无需关心该字段具体对应的表列名
 * @param <T>
 * @Version 1.0
 * @Date 2019-7-17 下午5:31
 */
@FunctionalInterface
public interface FieldSupplier<T> extends Serializable, Supplier<T> {

    /**
     * 获取列名称: 通过getter方法获取，适用于字段名与数据库表列名一致的情况
     *
     * @return
     */
    default String getColumnNameByGetterDefault() {
        try {
            Method method = this.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(this);
            String getter = serializedLambda.getImplMethodName();
            String fieldName = Introspector.decapitalize(getter.replace("get", ""));
            return fieldName;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取列名称： 通过字段上的注解获取，如@Column，适用于字段名与数据库表列名不一致的情况
     *
     * @return
     */
    default String getColumnNameByFieldDefault() {
        try {
            Method method = this.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(this);
            String getter = serializedLambda.getImplMethodName();

            // 获取字段名
            // TODO 注意：此处的fieldName将会是驼峰命名风格，首字母将变为小写，真实的字段名首字母可能就是大写
            String fieldName = Introspector.decapitalize(getter.replace("get", ""));

            //方法一：获取代理的真实对象全路径名，如User -> com/wq/jpatest/entity/User
//        String implClass = serializedLambda.getImplClass();
//        implClass = implClass.replaceAll("/",".");
//        //通过反射获取Class对象
//        Class<?> aClass = Class.forName(implClass);

            //方法二：获取lamda表达式代理的真实对象，如User::getAge,真实对象就是User
            T t = (T) serializedLambda.getCapturedArg(0);

            //获取表名
            String tableName = EntityUtils.getTableName(t.getClass());

            //获取字段名对应的field
            Field field = ReflectionUtils.getField(t.getClass(), fieldName);
            //列注解默认@Column，如果是自定义注解，需在此处进行修改
            Column annotation = field.getAnnotation(Column.class);
            if (annotation == null || StringUtils.isEmpty(annotation.name())) {
                return tableName + "." + fieldName;
            }
            return tableName + "." + annotation.name();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取列名称
     *
     * @param lambda lamda表达式
     * @return String 列名称
     */
    static String getColumnNameByGetter(FieldSupplier lambda) {
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
            String getter = serializedLambda.getImplMethodName();
            String fieldName = Introspector.decapitalize(getter.replace("get", ""));
            return fieldName;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据指定lamda获取表名.列名
     * @param lambda
     * @return
     */
    static String getColumnNameByField(FieldSupplier lambda) {
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
            String getter = serializedLambda.getImplMethodName();

            // 获取字段名
            String fieldName = Introspector.decapitalize(getter.replace("get", ""));

            //获取lamda表达式真实调用方
            Object capturedArg = serializedLambda.getCapturedArg(0);

            //获取表名
            String tableName = EntityUtils.getTableName(capturedArg.getClass());

            //获取字段名对应的field
            /**
             * getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。
             * getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
             */
//            Field field = aClass.getDeclaredField(fieldName);

            Field field = ReflectionUtils.getField(capturedArg.getClass(), fieldName);

            //列注解默认@Column，如果是自定义注解，需在此处进行修改
            Column annotation = field.getAnnotation(Column.class);
            if (annotation == null || StringUtils.isEmpty(annotation.name())) {
                return tableName + "." + fieldName;
            }
            return tableName + "." + annotation.name();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据指定lamda获取表名.列名+别名，用于查询出结果集RS后与POJO进行映射，如 house.house_addr as houseAddr
     * @param lambda
     * @return
     */
    static <T> String getColumnNameAndAliasByField(FieldSupplier<T> lambda) {
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
              method.setAccessible(Boolean.TRUE);
              SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
              String getter = serializedLambda.getImplMethodName();

              // 获取字段名
              String fieldName = Introspector.decapitalize(getter.replace("get", ""));

              /**
             * 此处判断是否引用构造方法，如果是代表查询该类的所有字段
             */
            if("<init>".equalsIgnoreCase(fieldName)){

                //返回user.id as id, user.name as name ...
//                return getAllColumnNameAndAlias(lambda.get());
                //返回user.*
                return getAllColumnName(lambda.get());
            }

            /**
             * 如果是父类字段，那么此处将返回父类全路径名，注释掉
             */
//            //获取代理的真实对象全路径名，如User -> com/wq/jpatest/entity/User，
//            String implClass = serializedLambda.getImplClass();
//            implClass = implClass.replaceAll("/", ".");
//            //通过反射获取Class对象
//            Class<?> aClass = Class.forName(implClass);

            //获取lamda表达式真实调用方
            Object capturedArg = serializedLambda.getCapturedArg(0);

            //获取表名
            String tableName = EntityUtils.getTableName(capturedArg.getClass());

            //获取字段名对应的field
            Field field = ReflectionUtils.getField(capturedArg.getClass(), fieldName);

            //列注解默认@Column，如果是自定义注解，需在此处进行修改
            Column annotation = field.getAnnotation(Column.class);
            if (annotation == null || StringUtils.isEmpty(annotation.name())) {
                return tableName + "." + fieldName + " as " + fieldName;
            }
            return tableName + "." + annotation.name() + " as " + fieldName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定对象的所有字段对应表名.列名+别名
     * @param t
     * @param <T>
     * @return
     */
    static <T> String getAllColumnName(T t) {
        String tableName = EntityUtils.getTableName(t.getClass());

        return tableName + ".*";
    }

    /**
     * 获取指定对象的所有字段对应表名.列名+别名
     * @param t
     * @param <T>
     * @return
     */
    static <T> String getAllColumnNameAndAlias(T t) {
        return getAllColumnNameAndAlias(t.getClass());
    }

    /**
     * 获取clz所有标注有@Column的Field名+"as"+别名
     * @param clz
     * @param <T>
     * @return
     */
    static <T> String getAllColumnNameAndAlias(Class<T> clz) {
        List<Field> fields = ReflectionUtils.getAllFieldsAnnotation(clz, Column.class);
        StringBuffer sb = new StringBuffer();

        fields.forEach(field -> {
            //列注解默认@Column，如果是自定义注解，需在此处进行修改
            Column annotation = field.getAnnotation(Column.class);
            if (StringUtils.isEmpty(annotation.name())) {
                sb.append(field.getName()).append(" as ").append(field.getName());
            } else {
                sb.append(annotation.name()).append(" as ").append(field.getName());
            }
            sb.append(", ");
        });

        if(sb.length() >= 2){
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }



}
