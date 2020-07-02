package cn.wq.persistence.sql.jdbc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Objects;
import java.util.stream.IntStream;

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

    private int[] paramTypes;

    public SQLWrapper(String sql, Object[] params) {
        this.sql = sql;
        this.params = params;
        if(Objects.nonNull(params)){
            resolveParamTypes();
        }
    }

    /**
     * 解析sql中的占位符，填充参数
     * @return sql
     */
    public String resolve2SQL(){

        for (Object param : params) {
            //TODO 目前仅判断是String类型的情况下才拼接''，后面需要补充完善
            if(String.class.equals(param.getClass())){
                sql = StringUtils.replaceOnce(sql, "?", "'" + param + "'");
            }else{
                sql = StringUtils.replaceOnce(sql, "?", String.valueOf(param));
            }

        }
        return sql;
    }

    /**
     * 根据params解析paramTypes
     */
    public int[] resolveParamTypes(){
        paramTypes = new int[params.length];
        IntStream.range(0, params.length).forEach(index -> paramTypes[index] = getTypes(params[index].getClass()));

        return paramTypes;
    }

    private static<T> int getTypes(Class<T> arg) {
        if (String.class.equals(arg)) {
            return Types.VARCHAR;
        } else if (int.class.equals(arg) || Integer.class.equals(arg)) {
            return Types.INTEGER;
        } else if (double.class.equals(arg) || Double.class.equals(arg)) {
            return Types.DOUBLE;
        } else if (java.util.Date.class.isAssignableFrom(arg)) {
            return Types.TIMESTAMP;
        } else if (long.class.equals(arg) || Long.class.equals(arg)) {
            return Types.BIGINT;
        } else if (float.class.equals(arg) || Float.class.equals(arg)) {
            return Types.FLOAT;
        } else if (boolean.class.equals(arg) || Boolean.class.equals(arg)) {
            return Types.BOOLEAN;
        } else if (short.class.equals(arg) || Short.class.equals(arg)) {
            return Types.INTEGER;
        } else if (byte.class.equals(arg) || Byte.class.equals(arg)) {
            return Types.INTEGER;
        } else if (BigDecimal.class.equals(arg)) {
            return Types.DECIMAL;
        } else {
            return Types.OTHER;
        }
    }
    

    public static void main(String[] params) {
        String str = "?????";
        String replaceOnce = StringUtils.replaceOnce(str, "?", "1");
        replaceOnce = StringUtils.replaceOnce(replaceOnce, "?", "2");
        replaceOnce = StringUtils.replaceOnce(replaceOnce, "?", "3");

        //123??
        System.out.println(replaceOnce);
    }

}
