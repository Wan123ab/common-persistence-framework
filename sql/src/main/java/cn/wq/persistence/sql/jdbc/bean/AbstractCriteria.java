package cn.wq.persistence.sql.jdbc.bean;

import cn.wq.persistence.common.bean.Pair;
import cn.wq.persistence.sql.jdbc.function.FieldSupplier;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author : 万强
 * @date : 2019/7/26 09:04
 * @desc : 抽象Criteria封装
 * @version : 1.0
 */
@Data
@SuppressWarnings({"all"})
public abstract class AbstractCriteria<Child extends AbstractCriteria> {

    /**
     * WhereParam条件参数
     */
    protected List<WhereParam> whereParams = Lists.newArrayList();

    /**
     * 拼接更加复杂的Criteria
     */
    protected List<CriteriaWrapper> criteriaWrappers = Lists.newArrayList();

    public Child where(String key, String opt, Object value) {
        whereParams.add(WhereParam.builder().key(key).opt(opt).value(value).build());
        return itSelf();
    }

    public Child where(String key, Object value) {
        return where(key, "=", value);
    }

    public <T> Child where(FieldSupplier<T> supplier, String opt, Object value) {
        return where(supplier.getColumnNameByFieldDefault(), opt, value);
    }

    public <T> Child where(FieldSupplier<T> supplier, Object value) {
        return where(supplier.getColumnNameByFieldDefault(), value);
    }

    public Child whereLike(String key, LikeType likeType, Object value) {
        return where(key, "LIKE", String.format(likeType.getType(), value));
    }

    public <T> Child whereLike(FieldSupplier<T> supplier, LikeType likeType, Object value) {
        return whereLike(supplier.getColumnNameByFieldDefault(), likeType, value);
    }

    public Child whereNotLike(String key, LikeType likeType, Object value) {
        return where(key, "NOT LIKE", String.format(likeType.getType(), value));
    }

    public <T> Child whereNotLike(FieldSupplier<T> supplier, LikeType likeType, Object value) {
        return whereNotLike(supplier.getColumnNameByFieldDefault(), likeType, value);
    }

    public Child and(String key, String opt, Object value) {
        return where("AND " + key, opt, value);
    }

    public Child and(String key, Object value) {
        return and(key, "=", value);
    }

    public <T> Child and(FieldSupplier<T> supplier, String opt, Object value) {
        return and(supplier.getColumnNameByFieldDefault(), opt, value);
    }

    public <T> Child and(FieldSupplier<T> supplier, Object value) {
        return and(supplier.getColumnNameByFieldDefault(), value);
    }

    public Child andLike(String key, LikeType likeType, Object value) {
        return and(key, "LIKE", likeType.getType().replace("value", String.valueOf(value)));
    }

    public <T> Child andLike(FieldSupplier<T> supplier, LikeType likeType, Object value) {
        return andLike(supplier.getColumnNameByFieldDefault(), likeType, value);
    }

    public Child andNotLike(String key, LikeType likeType, Object value) {
        return and(key, "NOT LIKE", likeType.getType().replace("value", String.valueOf(value)));
    }

    public <T> Child andNotLike(FieldSupplier<T> supplier, LikeType likeType, Object value) {
        return andNotLike(supplier.getColumnNameByFieldDefault(), likeType, value);
    }

    public Child or(String key, String opt, Object value) {
        return where("OR " + key, opt, value);
    }

    public Child or(String key, Object value) {
        return or("OR " + key, "=", value);
    }

    public <T> Child or(FieldSupplier<T> supplier, String opt, Object value) {
        return or(supplier.getColumnNameByFieldDefault(), opt, value);
    }

    public <T> Child or(FieldSupplier<T> supplier, Object value) {
        return or(supplier.getColumnNameByFieldDefault(), value);
    }

    public Child orLike(String key, LikeType likeType, Object value) {
        return or(key, "LIKE", likeType.getType().replace("value", String.valueOf(value)));
    }

    public <T> Child orLike(FieldSupplier<T> supplier, LikeType likeType, Object value) {
        return orLike(supplier.getColumnNameByFieldDefault(), likeType, value);
    }

    public Child orNotLike(String key, LikeType likeType, Object value) {
        return or(key, "NOT LIKE", likeType.getType().replace("value", String.valueOf(value)));
    }

    public <T> Child orNotLike(FieldSupplier<T> supplier, LikeType likeType, Object value) {
        return orNotLike(supplier.getColumnNameByFieldDefault(), likeType, value);
    }

    public Child whereIsNull(String key) {
        return where(key, "IS", "NULL");
    }

    public <T> Child whereIsNull(FieldSupplier<T> supplier) {
        return whereIsNull(supplier.getColumnNameByFieldDefault());
    }


    public Child andIsNull(String key) {
        return and(key, "IS", "NULL");
    }

    public <T> Child andIsNull(FieldSupplier<T> supplier) {
        return andIsNull(supplier.getColumnNameByFieldDefault());
    }

    public Child whereIsNotNull(String key) {
        return where(key, "IS", " NOT NULL");
    }

    public <T> Child whereIsNotNull(FieldSupplier<T> supplier) {
        return whereIsNotNull(supplier.getColumnNameByFieldDefault());
    }

    public Child andIsNotNull(String key) {
        return and(key, "IS", " NOT NULL");
    }

    public <T> Child andIsNotNull(FieldSupplier<T> supplier) {
        return andIsNotNull(supplier.getColumnNameByFieldDefault());
    }

    public Child whereBetween(String key, Object value1, Object value2) {
        return where(key, "BETWEEN", new Pair<>(value1, value2));
    }

    public <T> Child whereBetween(FieldSupplier<T> supplier, Object value1, Object value2) {
        return whereBetween(supplier.getColumnNameByFieldDefault(), value1, value2);
    }

    public Child andBetween(String key, Object value1, Object value2) {
        return and(key, "BETWEEN", new Pair<>(value1, value2));
    }

    public <T> Child andBetween(FieldSupplier<T> supplier, Object value1, Object value2) {
        return andBetween(supplier.getColumnNameByFieldDefault(), value1, value2);
    }

    public <T> Child whereIfAbsent(String key, T value, Predicate<T> predicate) {
        if (predicate.test(value)) {
            return where(key, value);
        }
        return itSelf();
    }

    public <T> Child whereIfAbsent(String key, String opt, T value) {
        return whereIfAbsent(key, opt, value, getDefaultPredicate(value));
    }

    public <T> Child whereIfAbsent(String key, String opt, T value, Predicate<T> predicate) {
        if (predicate.test(value)) {
            return where(key, opt, value);
        }
        return itSelf();
    }

    public <T, S> Child whereIfAbsent(FieldSupplier<S> supplier, T value) {
        return whereIfAbsent(supplier.getColumnNameByFieldDefault(), value, getDefaultPredicate(value));
    }

    public <T, S> Child whereIfAbsent(FieldSupplier<S> supplier, T value, Predicate<T> predicate) {
        if (predicate.test(value)) {
            return where(supplier.getColumnNameByFieldDefault(), value);
        }
        return itSelf();
    }

    public <T, S> Child whereIfAbsent(FieldSupplier<S> supplier, String opt, T value) {
        return whereIfAbsent(supplier.getColumnNameByFieldDefault(), opt, value, getDefaultPredicate(value));
    }

    public <T, S> Child whereIfAbsent(FieldSupplier<S> supplier, String opt, T value, Predicate<T> predicate) {
        if (predicate.test(value)) {
            return where(supplier.getColumnNameByFieldDefault(), opt, value);
        }
        return itSelf();
    }

    public <T> Child andIfAbsent(String key, T value) {
        return andIfAbsent(key, value, getDefaultPredicate(value));
    }

    public <T> Child andIfAbsent(String key, T value, Predicate<T> predicate) {
        if (predicate.test(value)) {
            return and(key, value);
        }
        return itSelf();
    }

    public <T> Child andIfAbsent(String key, String opt, T value) {
        return andIfAbsent(key, opt, value, getDefaultPredicate(value));
    }

    public <T> Child andIfAbsent(String key, String opt, T value, Predicate<T> predicate) {
        if (predicate.test(value)) {
            return and(key, opt, value);
        }
        return itSelf();
    }

    public <T, S> Child andIfAbsent(FieldSupplier<S> supplier, T value) {
        return andIfAbsent(supplier.getColumnNameByFieldDefault(), value, getDefaultPredicate(value));
    }

    public <T, S> Child andIfAbsent(FieldSupplier<S> supplier, T value, Predicate<T> predicate) {
        if (predicate.test(value)) {
            return and(supplier.getColumnNameByFieldDefault(), value);
        }
        return itSelf();
    }

    public <T, S> Child andIfAbsent(FieldSupplier<S> supplier, String opt, T value) {
        return andIfAbsent(supplier.getColumnNameByFieldDefault(), opt, value, getDefaultPredicate(value));
    }

    public <T, S> Child andIfAbsent(FieldSupplier<S> supplier, String opt, T value, Predicate<T> predicate) {
        if (predicate.test(value)) {
            return and(supplier.getColumnNameByFieldDefault(), opt, value);
        }
        return itSelf();
    }

    public Child withCriteria(Criteria criteria) {
        CriteriaWrapper criteriaWrapper = CriteriaWrapper.builder().criteriaType(CriteriaType.WITH)
                .criteria(criteria).build();
        criteriaWrappers.add(criteriaWrapper);
        return itSelf();
    }

    public Child whereCriteria(Criteria criteria) {
        CriteriaWrapper criteriaWrapper = CriteriaWrapper.builder().criteriaType(CriteriaType.WHERE)
                .criteria(criteria).build();
        criteriaWrappers.add(criteriaWrapper);
        return itSelf();
    }

    public Child orCriteria(Criteria criteria) {
        CriteriaWrapper criteriaWrapper = CriteriaWrapper.builder().criteriaType(CriteriaType.OR)
                .criteria(criteria).build();
        criteriaWrappers.add(criteriaWrapper);
        return itSelf();
    }

    public Child andCriteria(Criteria criteria) {
        CriteriaWrapper criteriaWrapper = CriteriaWrapper.builder().criteriaType(CriteriaType.AND)
                .criteria(criteria).build();
        criteriaWrappers.add(criteriaWrapper);
        return itSelf();
    }

    public Child itSelf() {
        return (Child) this;
    }

    /**
     * 默认判断器
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> getDefaultPredicate(T value) {
        return t -> {
            if (Objects.isNull(value)) {
                return false;
            }
            if (value instanceof String) {
                if (StringUtils.isEmpty(value)) {
                    return false;
                }
            }
            if (value instanceof Collection) {
                if (CollectionUtils.isEmpty((Collection) value)) {
                    return false;
                }
            }
            if (value.getClass().isArray()) {
                return ArrayUtils.getLength(value) != 0;
            }
            return true;
        };
    }


}
 
