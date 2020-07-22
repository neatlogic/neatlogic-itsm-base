package codedriver.framework.process.workcenter.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.process.constvalue.ProcessStepType;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.constvalue.ProcessWorkcenterField;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskSlaVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.util.HtmlUtil;

public class WorkcenterFieldBuilder {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	JSONObject dataJson = null;
	
	public WorkcenterFieldBuilder() {
		dataJson = new JSONObject();
	}
	
	public JSONObject build() {
		return dataJson;
	}
	public WorkcenterFieldBuilder setId(String id) {
		dataJson.put(ProcessWorkcenterField.ID.getValue(), id);
		return this;
	}
	public WorkcenterFieldBuilder setTitle(String title) {
		dataJson.put(ProcessWorkcenterField.TITLE.getValue(), title);
		return this;
	}
	public WorkcenterFieldBuilder setStatus(String status) {
		dataJson.put(ProcessWorkcenterField.STATUS.getValue(), status);
		return this;
	}
	public WorkcenterFieldBuilder setPriority(String priority) {
		dataJson.put(ProcessWorkcenterField.PRIORITY.getValue(),priority);
		return this;
	}
	public WorkcenterFieldBuilder setCatalog(String catalog) {
		dataJson.put(ProcessWorkcenterField.CATALOG.getValue(), catalog);
		return this;
	}
	public WorkcenterFieldBuilder setChannelType(String channelType) {
		dataJson.put(ProcessWorkcenterField.CHANNELTYPE.getValue(), channelType);
		return this;
	}
	public WorkcenterFieldBuilder setChannel(String channel) {
		dataJson.put(ProcessWorkcenterField.CHANNEL.getValue(), channel);
		return this;
	}
	public WorkcenterFieldBuilder setContent(ProcessTaskContentVo startContentVo) {
		String contentIncludeHtml = startContentVo== null?StringUtils.EMPTY:startContentVo.getContent();
		dataJson.put(ProcessWorkcenterField.CONTENT.getValue(), HtmlUtil.removeHtml(contentIncludeHtml, null));
		dataJson.put(ProcessWorkcenterField.CONTENT.getConditionValue(), contentIncludeHtml);
		return this;
	}
	public WorkcenterFieldBuilder setStartTime(Date startTime) {
		dataJson.put(ProcessWorkcenterField.STARTTIME.getValue(), sdf.format(startTime));
		return this;
	}
	public WorkcenterFieldBuilder setEndTime(Date endTime) {
		dataJson.put(ProcessWorkcenterField.ENDTIME.getValue(), endTime == null ?"":sdf.format(endTime));
		return this;
	}
	public WorkcenterFieldBuilder setOwner(String owner) {
		dataJson.put(ProcessWorkcenterField.OWNER.getValue(), String.format("user#%s", owner));
		return this;
	}
	public WorkcenterFieldBuilder setReporter(String reporter,String owner) {
		if(!reporter.equals(owner)) {
			dataJson.put(ProcessWorkcenterField.REPORTER.getValue(), String.format("user#%s", reporter));
		}
		return this;
	}
	public WorkcenterFieldBuilder setTransferFromUserList(List<ProcessTaskStepAuditVo> transferAuditList) {
		 List<String> transferUserUuidList = new ArrayList<String>();
		 for(ProcessTaskStepAuditVo auditVo : transferAuditList) {
			 transferUserUuidList.add(auditVo.getUserUuid());
		 }
		 dataJson.put(ProcessWorkcenterField.TRANSFER_FROM_USER.getValue(), transferUserUuidList);
		 return this;
	}
	
	private JSONObject getUserType(String userType, String userTypeName, JSONArray stepUserArray) {
		 JSONObject userTypeJson = new JSONObject();
		 userTypeJson.put("usertype", userType);
		 userTypeJson.put("usertypename", userTypeName);
		 userTypeJson.put("userlist", stepUserArray);
		 return userTypeJson;
	}
	
	public WorkcenterFieldBuilder setStepList( List<ProcessTaskStepVo>  processTaskStepList) {
		JSONArray stepList = new JSONArray();
		 for(ProcessTaskStepVo step : processTaskStepList) {
			 JSONObject stepJson = new JSONObject();
			 JSONArray userTypeArray = new JSONArray();
			 JSONArray majorUserTypeArray = new JSONArray();
			 JSONArray minorUserTypeArray = new JSONArray();
			 JSONArray agentUserTypeArray = new JSONArray();
			 JSONArray pendingUserTypeArray = new JSONArray();
			 userTypeArray.add(getUserType(ProcessUserType.MAJOR.getValue(),ProcessUserType.MAJOR.getText(),majorUserTypeArray));
			 userTypeArray.add(getUserType(ProcessUserType.MINOR.getValue(),ProcessUserType.MINOR.getText(),minorUserTypeArray));
			 userTypeArray.add(getUserType(ProcessUserType.AGENT.getValue(),ProcessUserType.AGENT.getText(),agentUserTypeArray));
			 userTypeArray.add(getUserType(ProcessTaskStatus.PENDING.getValue(),ProcessTaskStatus.PENDING.getText(),pendingUserTypeArray));
			 stepJson.put("id", step.getId());
			 stepJson.put("name", step.getName());
			 stepJson.put("status", step.getStatus());
			 stepJson.put("type", step.getType());
			 stepJson.put("isactive", step.getIsActive());
			 stepJson.put("usertypelist", userTypeArray);
			 //已激活未开始
			 if(step.getStatus().equals(ProcessTaskStatus.PENDING.getValue()) && step.getIsActive() == 1) {
				 for(ProcessTaskStepWorkerVo worker : step.getWorkerList()) {
					 pendingUserTypeArray.add(worker.getWorkerValue());
				 }
			 }else {
				 for(ProcessTaskStepUserVo userVo : step.getUserList()) {
					 String user = String.format("%s%s", GroupSearch.USER.getValuePlugin(),userVo.getUserUuid());
					 if(ProcessUserType.MAJOR.getValue().equals( userVo.getUserType())) {
						 majorUserTypeArray.add(user);
					 }
					 if(ProcessUserType.MINOR.getValue().equals( userVo.getUserType())) {
						 minorUserTypeArray.add(user);
					 }
					 if(ProcessUserType.AGENT.getValue().equals( userVo.getUserType())) {
						 agentUserTypeArray.add(user);
					 }
				 }
			 }
			//过滤上报节点
			 if(!(step.getType().equals(ProcessStepType.START.getValue()))) {
				 stepJson.put("filtstatus", step.getStatus());
			 }
			 stepList.add(stepJson);
		 }
		dataJson.put(ProcessWorkcenterField.STEP.getValue(), stepList);
		return this;
	}
	
	public WorkcenterFieldBuilder setWorktime(String worktime) {
		dataJson.put(ProcessWorkcenterField.WOKRTIME.getValue(), worktime);
		return this;
	}
	public WorkcenterFieldBuilder setExpiredTime(List<ProcessTaskSlaVo> processTaskSlaList) {
		for(ProcessTaskSlaVo processTaskSlaVo:processTaskSlaList) {
			processTaskSlaVo.setConfig(null); //es 同一个key 不支持存储不同类型的value
		}
		dataJson.put(ProcessWorkcenterField.EXPIRED_TIME.getValue(), JSONArray.toJSON(processTaskSlaList));
		return this;
	}
}
