package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

import java.io.Serializable;

public class ProcessTaskInvokeVo implements Serializable {
    @EntityField(name = "nfpd.processtaskinvokevo.entityfield.processtaskid.name", type = ApiParamType.LONG)
    private Long processTaskId;
    @EntityField(name = "nfpd.processtaskinvokevo.entityfield.source.name", type = ApiParamType.STRING)
    private String source;
    @EntityField(name = "nfpd.processtaskinvokevo.entityfield.type.name", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "nfpd.processtaskinvokevo.entityfield.invokeid.name", type = ApiParamType.LONG)
    private Long invokeId;

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(Long invokeId) {
        this.invokeId = invokeId;
    }
}
