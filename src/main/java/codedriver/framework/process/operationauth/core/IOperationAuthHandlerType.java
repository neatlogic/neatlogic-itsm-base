package codedriver.framework.process.operationauth.core;

import java.util.List;

import codedriver.framework.process.constvalue.ProcessTaskOperationType;

public interface IOperationAuthHandlerType {
    
    default List<ProcessTaskOperationType> getOperationTypeList() {
        return OperationAuthHandlerFactory.getHandler(this).getAllOperationTypeList();
    }
}
