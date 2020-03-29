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
import codedriver.framework.process.constvalue.ProcessWorkcenterField;
import codedriver.framework.process.constvalue.UserType;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;

public class WorkcenterFieldBuilder {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<String> userWillDoList = new ArrayList<String>();
	
	JSONObject dataJson = null;
	
	public WorkcenterFieldBuilder() {
		dataJson = new JSONObject();
	}
	
	public JSONObject build() {
		return dataJson;
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
		dataJson.put(ProcessWorkcenterField.CONTENT.getValue(), startContentVo== null?StringUtils.EMPTY:startContentVo.getContent());
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
		dataJson.put(ProcessWorkcenterField.OWNER.getValue(), owner);
		return this;
	}
	public WorkcenterFieldBuilder setReporter(String reporter,String owner) {
		if(!reporter.equals(owner)) {
			dataJson.put(ProcessWorkcenterField.REPORTER.getValue(), String.format("user#%s", reporter));
		}
		return this;
	}
	public WorkcenterFieldBuilder setTransferFromUserList(List<ProcessTaskStepAuditVo> transferAuditList) {
		 List<String> transferUserIdList = new ArrayList<String>();
		 for(ProcessTaskStepAuditVo auditVo : transferAuditList) {
			 transferUserIdList.add(auditVo.getUserId());
		 }
		 dataJson.put(ProcessWorkcenterField.TRANSFER_FROM_USER.getValue(), transferUserIdList);
		 return this;
	}
	
	private JSONArray getUserType(UserType userType,JSONArray stepUserArray) {
		 JSONArray userTypeArray = new JSONArray();
		 JSONObject userTypeJson = new JSONObject();
		 userTypeJson.put("usertype", userType.getValue());
		 userTypeJson.put("usertypename", userType.getText());
		 userTypeJson.put("userlist", stepUserArray);
		 userTypeArray.add(userTypeJson);
		 return userTypeArray;
	}
	public WorkcenterFieldBuilder setCurrentStepList( List<ProcessTaskStepVo>  processTaskActiveStepList) {
		JSONArray currentStepList = new JSONArray();
		 for(ProcessTaskStepVo step : processTaskActiveStepList) {
			 JSONObject currentStepJson = new JSONObject();
			 JSONArray userTypeArray = new JSONArray();
			 JSONArray majorUserTypeArray = new JSONArray();
			 JSONArray minorUserTypeArray = new JSONArray();
			 JSONArray agentUserTypeArray = new JSONArray();
			 userTypeArray.add(getUserType(UserType.MAJOR,majorUserTypeArray));
			 userTypeArray.add(getUserType(UserType.MINOR,minorUserTypeArray));
			 userTypeArray.add(getUserType(UserType.AGENT,agentUserTypeArray));
			 currentStepJson.put("id", step.getId());
			 currentStepJson.put("name", step.getName());
			 currentStepJson.put("status", step.getStatus());
			 currentStepJson.put("usertypelist", userTypeArray);
			 if(step.getStatus().equals(ProcessTaskStatus.PENDING.getValue())) {
				 for(ProcessTaskStepWorkerVo worker : step.getWorkerList()) {
					 //JSONObject currentStepUserJson = new JSONObject();
					 //currentStepUserJson.put("handler", worker.getWorkerValue());
					 //stepUserArray.add(currentStepUserJson);
					 userWillDoList.add(worker.getWorkerValue());
				 }
			 }else {
				 for(ProcessTaskStepUserVo userVo : step.getUserList()) {
					 String user = String.format("%s#%s", GroupSearch.USER.getValue(),userVo.getUserId());
					 if(UserType.MAJOR.getValue().equals( userVo.getUserType())) {
						 majorUserTypeArray.add(user);
					 }
					 if(UserType.MINOR.getValue().equals( userVo.getUserType())) {
						 minorUserTypeArray.add(user);
					 }
					 if(UserType.AGENT.getValue().equals( userVo.getUserType())) {
						 agentUserTypeArray.add(user);
					 }
					 userWillDoList.add(user);
				 }
			 }
			 currentStepList.add(currentStepJson);
		 }
		dataJson.put(ProcessWorkcenterField.CURRENT_STEP.getValue(), currentStepList);
		dataJson.put(ProcessWorkcenterField.USER_WILL_DO.getValue(), userWillDoList);
		return this;
	}
	public WorkcenterFieldBuilder setWorktime(String worktime) {
		dataJson.put(ProcessWorkcenterField.WOKRTIME.getValue(), worktime);
		return this;
	}
	public WorkcenterFieldBuilder setExpiredTime(Date expiredTime) {
		dataJson.put(ProcessWorkcenterField.EXPIRED_TIME.getValue(), expiredTime == null?"":sdf.format(expiredTime));
		return this;
	}
	
	public WorkcenterFieldBuilder setUserWillDo() {
		dataJson.put(ProcessWorkcenterField.USER_WILL_DO.getValue(), userWillDoList);
		return this;
	}
}
