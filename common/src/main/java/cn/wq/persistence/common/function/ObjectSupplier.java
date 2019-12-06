package cn.wq.persistence.common.function;

import java.util.function.Supplier;

/**
 * @author 万强
 * @version 1.0
 * @date 2019/11/23 14:45
 * @desc Object供应者容器，用于存储单例obj
 */
public class ObjectSupplier<T> implements Supplier<T> {

    private final Supplier<? extends T> instanceSupplier;

    private final Supplier<? extends T> defaultSupplier;

    private volatile T instance;

    public ObjectSupplier(T instance) {
        this.instanceSupplier = null;
        this.defaultSupplier = null;
        this.instance = instance;
    }

    public ObjectSupplier(Supplier<? extends T> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
        this.defaultSupplier = null;
    }

    public ObjectSupplier(T instance, Supplier<? extends T> defaultSupplier) {
        this.instanceSupplier = null;
        this.defaultSupplier = defaultSupplier;
        this.instance = instance;
    }

    public ObjectSupplier(Supplier<? extends T> instanceSupplier, Supplier<? extends T> defaultSupplier) {
        this.instanceSupplier = instanceSupplier;
        this.defaultSupplier = defaultSupplier;
    }

    @Override
    public T get() {
        T instance = this.instance;
        if (instance == null) {
            synchronized (this) {
                instance = this.instance;
                if (instance == null) {
                    if (instanceSupplier != null) {
                        instance = instanceSupplier.get();
                    }
                    if (instance == null && defaultSupplier != null) {
                        instance = defaultSupplier.get();
                    }
                    this.instance = instance;
                }
            }
        }
        return instance;
    }

    public static <T> ObjectSupplier<T> of(T instance){
        return new ObjectSupplier<>(instance);
    }

    public static <T> ObjectSupplier<T> of(Supplier<T> instanceSupplier){
        return new ObjectSupplier<>(instanceSupplier);
    }

}
 
