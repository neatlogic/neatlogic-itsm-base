package neatlogic.framework.process.operationauth.core;
/**
 * 
* @Time:2020年11月27日
* @ClassName: TernaryPredicate 
* @Description: 三元断言函数式接口
* @param <T>
* @param <U>
* @param <V>
* @param <W>
 */
@FunctionalInterface
public interface TernaryPredicate<T, U, V, W, X> {
    
    boolean test(T t, U u, V v, W w, X x);
}
