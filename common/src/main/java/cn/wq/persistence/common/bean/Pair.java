package cn.wq.persistence.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 万强
 * @version 1.0
 * @date 2019/11/23 11:09
 * @desc 封装2个参数的容器对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pair<F, S> {

    private F first;

    private S second;

    /**
     * 创建一个Collector，用于将Pairs转换为Map
     * A collector to create a {@link Map} from a {@link Stream} of {@link Pair}s.
     *
     * @return Collector
     */
    public static <S, T> Collector<Pair<S, T>, ?, Map<S, T>> toMap() {
        return Collectors.toMap(Pair::getFirst, Pair::getSecond);
    }

}
 
