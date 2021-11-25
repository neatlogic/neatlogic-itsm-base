package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

import java.util.Date;

public class ProcessTaskSlaTimeVo {
//    @ESKey(type = ESKeyType.PKEY, name ="processTaskId")
	@EntityField(name = "工单id", type = ApiParamType.LONG)
	private Long processTaskId;
	@EntityField(name = "步骤id", type = ApiParamType.LONG)
	private Long processTaskStepId;
	@EntityField(name = "slaId", type = ApiParamType.LONG)
	private Long slaId;
	@EntityField(name = "名称", type = ApiParamType.STRING)
	private String name;
	@EntityField(name = "超时日期（根据工作日历计算）", type = ApiParamType.LONG)
	private Date expireTime;
	@EntityField(name = "超时日期（不考虑工作日历）", type = ApiParamType.LONG)
	private Date realExpireTime;
	@EntityField(name = "总耗时，单位：毫秒", type = ApiParamType.LONG)
	private Long timeSum;
	@EntityField(name = "剩余时间（根据工作日历计算），单位：毫秒", type = ApiParamType.LONG)
	private Long timeLeft;
	@EntityField(name = "剩余时间（不考虑工作日历），单位：毫秒", type = ApiParamType.LONG)
	private Long realTimeLeft;
	@EntityField(name = "状态", type = ApiParamType.STRING)
	private String status;
	private Long expireTimeLong;
	private Long realExpireTimeLong;

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

	public Long getSlaId() {
		return slaId;
	}

	public void setSlaId(Long slaId) {
		this.slaId = slaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Date getRealExpireTime() {
		return realExpireTime;
	}

	public void setRealExpireTime(Date realExpireTime) {
		this.realExpireTime = realExpireTime;
	}

	public Long getTimeSum() {
		return timeSum;
	}

	public void setTimeSum(Long timeSum) {
		this.timeSum = timeSum;
	}

	public Long getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(Long timeLeft) {
		this.timeLeft = timeLeft;
	}

	public Long getRealTimeLeft() {
		return realTimeLeft;
	}

	public void setRealTimeLeft(Long realTimeLeft) {
		this.realTimeLeft = realTimeLeft;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getExpireTimeLong() {
		return expireTimeLong;
	}

	public void setExpireTimeLong(Long expireTimeLong) {
		this.expireTimeLong = expireTimeLong;
	}

	public Long getRealExpireTimeLong() {
		return realExpireTimeLong;
	}

	public void setRealExpireTimeLong(Long realExpireTimeLong) {
		this.realExpireTimeLong = realExpireTimeLong;
	}

    @Override
    public String toString() {
        return "[slaId=" + slaId + ", status=" + status + ", expireTime=" + expireTime
            + ", realExpireTime=" + realExpireTime + ", timeSum=" + timeSum + ", timeLeft=" + timeLeft
            + ", realTimeLeft=" + realTimeLeft  + "]";
    }

}
