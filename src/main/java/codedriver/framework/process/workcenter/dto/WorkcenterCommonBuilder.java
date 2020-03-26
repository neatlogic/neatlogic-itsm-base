package codedriver.framework.process.workcenter.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessWorkcenterColumn;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;

public class WorkcenterCommonBuilder {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	JSONObject dataJson = null;
	
	public WorkcenterCommonBuilder() {
		dataJson = new JSONObject();
	}
	
	public JSONObject build() {
		return dataJson;
	}
	public WorkcenterCommonBuilder setTitle(String title) {
		dataJson.put(ProcessWorkcenterColumn.TITLE.getValueEs(), title);
		return this;
	}
	public WorkcenterCommonBuilder setStatus(String status) {
		dataJson.put(ProcessWorkcenterColumn.STATUS.getValueEs(), status);
		return this;
	}
	public WorkcenterCommonBuilder setPriority(String priority) {
		dataJson.put(ProcessWorkcenterColumn.PRIORITY.getValueEs(), priority);
		return this;
	}
	public WorkcenterCommonBuilder setCatalog(String catalog) {
		dataJson.put(ProcessWorkcenterColumn.CATALOG.getValueEs(), catalog);
		return this;
	}
	public WorkcenterCommonBuilder setChannelType(String channelType) {
		dataJson.put(ProcessWorkcenterColumn.CHANNELTYPE.getValueEs(), channelType);
		return this;
	}
	public WorkcenterCommonBuilder setChannel(String channel) {
		dataJson.put(ProcessWorkcenterColumn.CHANNEL.getValueEs(), channel);
		return this;
	}
	public WorkcenterCommonBuilder setContent(ProcessTaskContentVo startContentVo) {
		dataJson.put(ProcessWorkcenterColumn.CONTENT.getValueEs(), startContentVo== null?StringUtils.EMPTY:startContentVo.getContent());
		return this;
	}
	public WorkcenterCommonBuilder setStartTime(Date startTime) {
		dataJson.put(ProcessWorkcenterColumn.STARTTIME.getValueEs(), sdf.format(startTime));
		return this;
	}
	public WorkcenterCommonBuilder setEndTime(Date endTime) {
		dataJson.put(ProcessWorkcenterColumn.ENDTIME.getValueEs(), endTime == null ?"":sdf.format(endTime));
		return this;
	}
	public WorkcenterCommonBuilder setOwner(String owner) {
		dataJson.put(ProcessWorkcenterColumn.OWNER.getValueEs(), owner);
		return this;
	}
	public WorkcenterCommonBuilder setReporter(String reporter) {
		dataJson.put(ProcessWorkcenterColumn.REPORTER.getValueEs(), reporter);
		return this;
	}
	public WorkcenterCommonBuilder setTransferFromUserList(List<ProcessTaskStepAuditVo> transferAuditList) {
		 List<String> transferUserIdList = new ArrayList<String>();
		 for(ProcessTaskStepAuditVo auditVo : transferAuditList) {
			 transferUserIdList.add(auditVo.getUserId());
		 }
		 dataJson.put(ProcessWorkcenterColumn.TRANSFER_FROM_USER.getValueEs(), transferUserIdList);
		 return this;
	}
	public WorkcenterCommonBuilder setCurrentStepList( List<ProcessTaskStepVo>  processTaskActiveStepList) {
		JSONArray currentStepList = new JSONArray();
		 for(ProcessTaskStepVo step : processTaskActiveStepList) {
			 JSONObject currentStepJson = new JSONObject();
			 JSONArray stepUserArray = new JSONArray();
			 currentStepJson.put("id", step.getId());
			 currentStepJson.put("name", step.getName());
			 currentStepJson.put("status", step.getStatus());
			 currentStepJson.put("handlerList", stepUserArray);
			 if(step.getStatus().equals(ProcessTaskStatus.PENDING.getValue())) {
				 for(ProcessTaskStepWorkerVo worker : step.getWorkerList()) {
					 JSONObject currentStepUserJson = new JSONObject();
					 currentStepUserJson.put("workerId", worker.getWorkerValue());
					 stepUserArray.add(currentStepUserJson);
				 }
			 }else {
				 for(ProcessTaskStepUserVo userVo : step.getUserList()) {
					 JSONObject currentStepUserJson = new JSONObject();
					 currentStepUserJson.put("userId", String.format("%s#%s", GroupSearch.USER.getValue(),userVo.getUserId()));
					 currentStepUserJson.put("userType", userVo.getUserType());
					 stepUserArray.add(currentStepUserJson);
				 }
			 }
			 currentStepList.add(currentStepJson);
		 }
		dataJson.put(ProcessWorkcenterColumn.CURRENT_STEP.getValueEs(), currentStepList);
		return this;
	}
	public WorkcenterCommonBuilder setWorktime(String worktime) {
		dataJson.put(ProcessWorkcenterColumn.WOKRTIME.getValueEs(), worktime);
		return this;
	}
	public WorkcenterCommonBuilder setExpiredTime(Date expiredTime) {
		dataJson.put(ProcessWorkcenterColumn.EXPIRED_TIME.getValueEs(), expiredTime == null?"":sdf.format(expiredTime));
		return this;
	}
}
