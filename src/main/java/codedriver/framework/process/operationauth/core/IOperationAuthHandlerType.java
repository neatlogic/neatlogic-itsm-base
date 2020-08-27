package codedriver.framework.process.operationauth.core;

import java.util.List;

import codedriver.framework.process.constvalue.OperationType;

public interface IOperationAuthHandlerType {
    
    default List<OperationType> getOperationTypeList() {
        return OperationAuthHandlerFactory.getHandler(this).getAllOperationTypeList();
    }
}
