package cn.wq.persistence.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @auther 万强
 * @date 2019/9/4 20:05
 * @desc Collection工具类
 */
public class CollectionUtils {

    /**
     * 将指定List按照指定大小分割成多个List
     *
     * @param list 待分割的list
     * @param len  分割长度
     * @return List<List < T>>
     */
    public static <T> List<List<T>> splitList(List<T> list, int len) {
        List<List<T>> result = new ArrayList<>();
        if (list == null || list.size() == 0 || len < 1) {
            result.add(list);
            return result;
        }

        int size = list.size();
        int count = (size + len - 1) / len;

        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @param list 待分割的list
     * @param n    均分的list数量
     * @return List<List < T>>
     */
    public static <T> List<List<T>> splitAverage(List<T> list, int n) {
        List<List<T>> result = new ArrayList<>();
        int remaider = list.size() % n;  //(先计算出余数)
        int number = list.size() / n;  //然后是商
        int offset = 0;//偏移量

        for (int i = 0; i < n; i++) {
            List<T> value;
            if (remaider > 0) {
                value = list.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = list.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }


}
 
