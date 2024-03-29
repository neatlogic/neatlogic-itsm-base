package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.exception.core.ApiRuntimeException;

/**
 * @author longrf
 * @date 2022/1/10 4:55 下午
 */
public class WorkcenterCatalogIsUsedException extends ApiRuntimeException {
    public WorkcenterCatalogIsUsedException(Long id) {
        super("菜单id为“{0}”的菜单类型已被引用，无法删除", id);
    }
}
