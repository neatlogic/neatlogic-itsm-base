package codedriver.framework.process.operationauth.core;
/**
 * 
* @Time:2020年11月27日
* @ClassName: TernaryPredicate 
* @Description: 三元断言函数式接口
* @param <T>
* @param <U>
* @param <S>
 */
@FunctionalInterface
public interface TernaryPredicate<T, U, V> {
    
    boolean test(T t, U u, V v);
}
