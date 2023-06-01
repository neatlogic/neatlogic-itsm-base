package neatlogic.framework.process.exception.processtaskserialnumberpolicy;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessTaskSerialNumberPolicyNotFoundException extends ApiRuntimeException {
    
    private static final long serialVersionUID = 8251360686469779326L;

    public ProcessTaskSerialNumberPolicyNotFoundException(String channelTypeUuid) {
        super("服务类型：“{0}”没有配置工单生成策略", channelTypeUuid);
    }
}
