/*
 * Copyright(c) 2022 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dto;

/**
 * @author linbq
 * @since 2022/2/22 15:07
 **/
public class ProcessTaskStepSlaTimeVo {
    private Long processTaskId;
    private Long processTaskStepId;
    private String type;
    private Long slaId;
    private Long timeSum;
    private Long timeCost;
    private Long realTimeCost;
    private Integer isTimeout;

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public Long getProcessTaskStepId() {
        return processTaskStepId;
    }

    public void setProcessTaskStepId(Long processTaskStepId) {
        this.processTaskStepId = processTaskStepId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSlaId() {
        return slaId;
    }

    public void setSlaId(Long slaId) {
        this.slaId = slaId;
    }

    public Long getTimeSum() {
        return timeSum;
    }

    public void setTimeSum(Long timeSum) {
        this.timeSum = timeSum;
    }

    public Long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(Long timeCost) {
        this.timeCost = timeCost;
    }

    public Long getRealTimeCost() {
        return realTimeCost;
    }

    public void setRealTimeCost(Long realTimeCost) {
        this.realTimeCost = realTimeCost;
    }

    public Integer getIsTimeout() {
        if (isTimeout == null && timeSum != null && timeCost != null) {
            isTimeout = timeSum > timeCost ? 0 : 1;
        }
        return isTimeout;
    }

    public void setIsTimeout(Integer isTimeout) {
        this.isTimeout = isTimeout;
    }
}
