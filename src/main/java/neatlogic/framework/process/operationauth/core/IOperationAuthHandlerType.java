package neatlogic.framework.process.operationauth.core;

import java.util.List;

import neatlogic.framework.process.constvalue.ProcessTaskOperationType;

public interface IOperationAuthHandlerType {
    public String getValue();
    public String getText();
    default List<ProcessTaskOperationType> getOperationTypeList() {
        return OperationAuthHandlerFactory.getHandler(this.getValue()).getAllOperationTypeList();
    }
}
