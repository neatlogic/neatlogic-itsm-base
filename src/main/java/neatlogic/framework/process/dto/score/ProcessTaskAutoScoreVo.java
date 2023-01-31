package neatlogic.framework.process.dto.score;

import java.util.Date;

public class ProcessTaskAutoScoreVo {

    public Long processTaskId;
    public Date triggerTime;
    public String config;
    public Long getProcessTaskId() {
        return processTaskId;
    }
    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }
    public Date getTriggerTime() {
        return triggerTime;
    }
    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }
    public String getConfig() {
        return config;
    }
    public void setConfig(String config) {
        this.config = config;
    }
}
