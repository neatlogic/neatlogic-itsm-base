package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.exception.core.ApiRuntimeException;

/**
 * @author longrf
 * @date 2022/1/10 2:54 下午
 */
public class WorkcenterCatalogNameRepeatsException extends ApiRuntimeException {
    public WorkcenterCatalogNameRepeatsException(String name) {
        super("工单中心菜单类型名称“{0}”重复", name);
    }
}
