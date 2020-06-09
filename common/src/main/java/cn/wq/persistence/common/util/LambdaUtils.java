package cn.wq.persistence.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @version 1.0
 * @author 万强
 * @date 2019/8/8 19:31
 * @desc jdk8 Lambda工具类
 */
@SuppressWarnings("unchecked")
public class LambdaUtils {

    private static final Logger log = LoggerFactory.getLogger(LambdaUtils.class);

    /**
     * 捕获lambda表达式Function中的异常，并重新抛出
     *
     * @param function function
     * @param <T>      输入
     * @param <R>      输出
     * @param <E>      异常
     * @return Function
     * @throws E
     */
    public static <T, R, E extends Exception> Function<T, R> rethrowFunctionException(ExceptionFunction<T, R, E> function) throws E {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                throwActualException(e);
                return null;
            }
        };
    }

    /**
     * 捕获lambda表达式Function中的异常，并打印日志
     *
     * @param function function
     * @param <T>      输入
     * @param <R>      输出
     * @return Function
     */
    public static <T, R, E extends Exception> Function<T, R> catchFunctionException(ExceptionFunction<T, R, E> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                log.error("ExceptionFunction执行出异常啦", e);
                return null;
            }
        };
    }

    /**
     * 捕获lambda表达式Consumer中的异常，并重新抛出
     *
     * @param consumer consumer
     * @param <T>      输入
     * @param <E>      异常
     * @return Consumer
     * @throws E
     */
    public static <T, E extends Exception> Consumer<T> rethrowConsumerException(ExceptionConsumer<T, E> consumer) throws E {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                throwActualException(e);
            }
        };
    }

    /**
     * 捕获lambda表达式Consumer中的异常，并打印日志
     *
     * @param consumer consumer
     * @param <T>      输入
     * @return Consumer
     */
    public static <T, E extends Exception> Consumer<T> catchConsumerException(ExceptionConsumer<T, E> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                log.error("ExceptionConsumer执行出异常啦", e);
            }
        };
    }

    /**
     * 捕获lambda表达式Runnable中的异常，并重新抛出
     *
     * @param exceptionRunnable exceptionRunnable
     * @param <E>               异常
     * @return Runnable
     * @throws E
     */
    public static <E extends Exception> Runnable rethrowExceptionRunnable(ExceptionRunnable<E> exceptionRunnable) throws E {
        return () -> {
            try {
                exceptionRunnable.run();
            } catch (Exception e) {
                throwActualException(e);
            }
        };
    }

    /**
     * 捕获lambda表达式Runnable中的异常，并打印日志
     *
     * @param exceptionRunnable exceptionRunnable
     * @param <E>               异常
     * @return Runnable
     */
    public static <E extends Exception> Runnable catchExceptionRunnable(ExceptionRunnable<E> exceptionRunnable) {
        return () -> {
            try {
                exceptionRunnable.run();
            } catch (Exception e) {
                log.error("ExceptionRunnable执行出异常啦", e);
            }
        };
    }

    /**
     * 抛出真正的异常
     *
     * @param e   异常
     * @param <E> 异常
     * @throws E
     */
    private static <E extends Exception> void throwActualException(Exception e) throws E {
        throw (E) e;
    }

    /**
     * 将一个支持序列化的lambda表达式解析为SerializedLambda以获取更多属性信息
     * 注意：此方法仅支持解析lambda表达式，不支持解析序列化接口实现或者非lambda写法的对象
     *
     * @param lambda
     * @return
     */
    public static SerializedLambda resolve(Serializable lambda) {
        Method method;
        try {
            method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            return (SerializedLambda) method.invoke(lambda);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }

    //=======================================自定义函数式接口，用于包装lambda异常============================================

    /**
     * 封装Consumer类型实现抛出的异常
     *
     * @param <T> 输入
     * @param <E> 异常
     */
    @FunctionalInterface
    public interface ExceptionConsumer<T, E extends Exception> extends Serializable {

        void accept(T t) throws E;

    }

    /**
     * 封装Function类型实现抛出的异常
     *
     * @param <T> 输入
     * @param <R> 输出
     * @param <E> 异常
     */
    @FunctionalInterface
    public interface ExceptionFunction<T, R, E extends Exception> extends Serializable {

        R apply(T t) throws E;

    }

    @FunctionalInterface
    public interface ExceptionRunnable<E extends Exception> extends Serializable {

        void run() throws E;

    }


}
 
