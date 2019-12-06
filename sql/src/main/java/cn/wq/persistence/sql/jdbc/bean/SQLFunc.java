package cn.wq.persistence.sql.jdbc.bean;

import cn.wq.persistence.sql.jdbc.function.FieldSupplier;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author : 万强
 * @date : 2019/7/19 10:21
 * @desc : SQL函数
 * @version  1.0
 */
public class SQLFunc {


    public static String avg(String field) {
        return "AVG(" + field + ")";
    }

    public static <T> String avg(FieldSupplier<T> supplier){
        return avg(supplier.getColumnNameByFieldDefault());
    }

    public static String avgAs(String field, String alias) {
        return "AVG(" + field + ") AS " + alias;
    }

    public static <T> String avgAs(FieldSupplier<T> supplier, String alias){
        return avgAs(supplier.getColumnNameByFieldDefault(), alias);
    }

    public static String count(String field) {
        return "COUNT(" + field + ")";
    }

    public static <T> String count(FieldSupplier<T> supplier){
        return count(supplier.getColumnNameByFieldDefault());
    }

    public static String countAs(String field, String alias) {
        return "COUNT(" + field + ") AS " + alias;
    }

    public static <T> String countAs(FieldSupplier<T> supplier, String alias){
        return countAs(supplier.getColumnNameByFieldDefault(), alias);
    }

    public static String min(String field) {
        return "MIN(" + field + ")";
    }

    public static <T> String min(FieldSupplier<T> supplier){
        return min(supplier.getColumnNameByFieldDefault());
    }

    public static String minAs(String field, String alias) {
        return "MIN(" + field + ") AS " + alias;
    }

    public static <T> String minAs(FieldSupplier<T> supplier, String alias){
        return minAs(supplier.getColumnNameByFieldDefault(), alias);
    }

    public static String max(String field) {
        return "MAX(" + field + ")";
    }

    public static <T> String max(FieldSupplier<T> supplier){
        return max(supplier.getColumnNameByFieldDefault());
    }

    public static String maxAs(String field, String alias) {
        return "MAX(" + field + ") AS " + alias;
    }

    public static <T> String maxAs(FieldSupplier<T> supplier, String alias){
        return maxAs(supplier.getColumnNameByFieldDefault(), alias);
    }

    public static String sum(String field) {
        return "SUM(" + field + ")";
    }

    public static <T> String sum(FieldSupplier<T> supplier){
        return sum(supplier.getColumnNameByFieldDefault());
    }

    public static String sumAs(String field, String alias) {
        return "SUM(" + field + ") AS " + alias;
    }

    public static <T> String sumAs(FieldSupplier<T> supplier, String alias){
        return sumAs(supplier.getColumnNameByFieldDefault(), alias);
    }


    public static String concat(String... fields) {
        return "CONCAT(" + Arrays.stream(fields).collect(Collectors.joining(",")) + ")";
    }

    public static <T> String concat(FieldSupplier<T>... suppliers){
        return "CONCAT(" + Arrays.stream(suppliers).map(FieldSupplier::getColumnNameByField).collect(Collectors.joining(",")) + ")";
    }

    public static String concatAs(String alias, String... fields) {
        return "CONCAT(" + Arrays.stream(fields).collect(Collectors.joining(",")) + ") AS " + alias;
    }

    public static <T> String concatAs(String alias, FieldSupplier<T>... suppliers){
        return "CONCAT(" + Arrays.stream(suppliers).map(FieldSupplier::getColumnNameByField).collect(Collectors.joining(",")) + ") AS " + alias;
    }


}
