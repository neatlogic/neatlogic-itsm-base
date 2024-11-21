package neatlogic.framework.process.operationauth.core;

import java.util.List;

public interface IOperationAuthHandlerType {
    public String getValue();
    public String getText();
    default List<IOperationType> getOperationTypeList() {
        return OperationAuthHandlerFactory.getHandler(this.getValue()).getAllOperationTypeList();
    }
}
