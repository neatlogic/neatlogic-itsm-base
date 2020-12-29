package codedriver.framework.process.exception.processtaskserialnumberpolicy;

import codedriver.framework.exception.core.ApiRuntimeException;

public class ProcessTaskSerialNumberPolicyNotFoundException extends ApiRuntimeException {
    
    private static final long serialVersionUID = 8251360686469779326L;

    public ProcessTaskSerialNumberPolicyNotFoundException(String channelTypeUuid) {
        super("服务类型：'" + channelTypeUuid + "'没有配置工单生成策略");
    }
}
