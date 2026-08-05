package neatlogic.framework.process.exception.priority;

import neatlogic.framework.exception.core.ApiRuntimeException;

/**
 * @Title: PriorityIsInvokedException
 * @Package neatlogic.framework.process.exception.priority
 * @Description: 优先级被引用异常
 * @Author: linbq
 * @Date: 2021/1/8 15:08

 **/
public class PriorityIsInvokedException extends ApiRuntimeException {

    private static final long serialVersionUID = -5334268132696017059L;

    public PriorityIsInvokedException(String priorityUuid) {
        super("nfpep.priorityisinvokedexception.priorityisinvokedexception", priorityUuid);
    }
}
