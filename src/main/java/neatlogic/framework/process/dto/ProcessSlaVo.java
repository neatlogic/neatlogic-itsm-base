package neatlogic.framework.process.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import neatlogic.framework.notify.dto.InvokeNotifyPolicyConfigVo;
import org.apache.commons.lang3.StringUtils;

public class ProcessSlaVo implements Serializable {
    private static final long serialVersionUID = 2183891795903221664L;
    private String processUuid;
    private String uuid;
    private String name;
    private String calculateHandler;
    private String config;
    private List<String> processStepUuidList;
    private List<InvokeNotifyPolicyConfigVo> notifyPolicyConfigList = new ArrayList<>();

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalculateHandler() {
        return calculateHandler;
    }

    public void setCalculateHandler(String calculateHandler) {
        this.calculateHandler = calculateHandler;
    }

    public String getProcessUuid() {
        return processUuid;
    }

    public void setProcessUuid(String processUuid) {
        this.processUuid = processUuid;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public List<String> getProcessStepUuidList() {
        return processStepUuidList;
    }

    public void setProcessStepUuidList(List<String> processStepUuidList) {
        this.processStepUuidList = processStepUuidList;
    }

    public void addProcessStepUuid(String processStepUuid) {
        if (this.processStepUuidList == null) {
            this.processStepUuidList = new ArrayList<>();
        }
        if (StringUtils.isNotBlank(processStepUuid) && !this.processStepUuidList.contains(processStepUuid)) {
            this.processStepUuidList.add(processStepUuid);
        }
    }

    public List<InvokeNotifyPolicyConfigVo> getNotifyPolicyConfigList() {
        return notifyPolicyConfigList;
    }

    public void setNotifyPolicyConfigList(List<InvokeNotifyPolicyConfigVo> notifyPolicyConfigList) {
        this.notifyPolicyConfigList = notifyPolicyConfigList;
    }
}
