package cn.wq.persistence.common.util;

import org.springframework.beans.BeanUtils;

/**
 * @author 万强
 * @date 2019/8/8 19:13
 * @desc Bean操作工具类
 * @version 1.0
 */
public class BeanUtil {

    /**
     *
     * @param s 源对象
     * @param clz 目标对象class
     * @param <T> 目标对象泛型
     * @param <S> 源对象泛型
     * @return T  目标对象实例
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T, S> T copy(S s, Class<T> clz) throws IllegalAccessException, InstantiationException {

        T t = clz.newInstance();
        BeanUtils.copyProperties(s, t);

        return t;
    }

}
 
