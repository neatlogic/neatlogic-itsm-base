package neatlogic.framework.process.exception.workcenter;

import neatlogic.framework.exception.core.ApiRuntimeException;

/**
 * @author longrf
 * @date 2022/1/10 4:29 下午
 */
public class WorkcenterCatalogIdNotFoundException extends ApiRuntimeException {
    public WorkcenterCatalogIdNotFoundException(Long id) {
        super("工单中心菜单类型id“" + id + "”不存在");
    }
}
