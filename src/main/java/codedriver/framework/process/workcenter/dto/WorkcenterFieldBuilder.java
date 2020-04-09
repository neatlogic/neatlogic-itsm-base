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
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;

public class WorkcenterFieldBuilder {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<String> userWillDoList = new ArrayList<String>();
	private List<String> userDoneList = new ArrayList<String>();
	private List<String> userDoList = new ArrayList<String>();
	
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
		 List<String> transferUserIdList = new ArrayList<String>();
		 for(ProcessTaskStepAuditVo auditVo : transferAuditList) {
			 transferUserIdList.add(auditVo.getUserId());
		 }
		 dataJson.put(ProcessWorkcenterField.TRANSFER_FROM_USER.getValue(), transferUserIdList);
		 return this;
	}
	
	private JSONObject getUserType(String userType, String userTypeName, JSONArray stepUserArray) {
		 JSONObject userTypeJson = new JSONObject();
		 userTypeJson.put("usertype", userType);
		 userTypeJson.put("usertypename", userTypeName);
		 userTypeJson.put("userlist", stepUserArray);
		 return userTypeJson;
	}
	/*public WorkcenterFieldBuilder setCurrentStepList( List<ProcessTaskStepVo>  processTaskActiveStepList) {
		JSONArray currentStepList = new JSONArray();
		 for(ProcessTaskStepVo step : processTaskActiveStepList) {
			 JSONObject currentStepJson = new JSONObject();
			 JSONArray userTypeArray = new JSONArray();
			 JSONArray majorUserTypeArray = new JSONArray();
			 JSONArray minorUserTypeArray = new JSONArray();
			 JSONArray agentUserTypeArray = new JSONArray();
			 JSONArray pendingUserTypeArray = new JSONArray();
			 userTypeArray.add(getUserType(UserType.MAJOR.getValue(),UserType.MAJOR.getText(),majorUserTypeArray));
			 userTypeArray.add(getUserType(UserType.MINOR.getValue(),UserType.MINOR.getText(),minorUserTypeArray));
			 userTypeArray.add(getUserType(UserType.AGENT.getValue(),UserType.AGENT.getText(),agentUserTypeArray));
			 userTypeArray.add(getUserType("pending","pending",pendingUserTypeArray));
			 currentStepJson.put("id", step.getId());
			 currentStepJson.put("name", step.getName());
			 currentStepJson.put("status", step.getStatus());
			 currentStepJson.put("usertypelist", userTypeArray);
			 if(step.getStatus().equals(ProcessTaskStatus.PENDING.getValue())) {
				 for(ProcessTaskStepWorkerVo worker : step.getWorkerList()) {
					 pendingUserTypeArray.add(worker.getWorkerValue());
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
	}*/
	
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
			 userTypeArray.add(getUserType("pending","pending",pendingUserTypeArray));
			 stepJson.put("id", step.getId());
			 stepJson.put("name", step.getName());
			 stepJson.put("status", step.getStatus());
			 stepJson.put("isactive", step.getIsActive());
			 stepJson.put("usertypelist", userTypeArray);
			 //已激活未开始
			 if(step.getStatus().equals(ProcessTaskStatus.PENDING.getValue()) && step.getIsActive() == 1) {
				 for(ProcessTaskStepWorkerVo worker : step.getWorkerList()) {
					 pendingUserTypeArray.add(worker.getWorkerValue());
					 userWillDoList.add(worker.getWorkerValue());
					 userDoList.add(worker.getWorkerValue());
				 }
			 }else {
				 for(ProcessTaskStepUserVo userVo : step.getUserList()) {
					 String user = String.format("%s%s", GroupSearch.USER.getValuePlugin(),userVo.getUserId());
					 if(ProcessUserType.MAJOR.getValue().equals( userVo.getUserType())) {
						 majorUserTypeArray.add(user);
					 }
					 if(ProcessUserType.MINOR.getValue().equals( userVo.getUserType())) {
						 minorUserTypeArray.add(user);
					 }
					 if(ProcessUserType.AGENT.getValue().equals( userVo.getUserType())) {
						 agentUserTypeArray.add(user);
					 }
					 if(ProcessTaskStatus.RUNNING.getValue().equals(dataJson.getString("status"))&&(step.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())||step.getStatus().equals(ProcessTaskStatus.DRAFT.getValue())&&userVo.getStatus().equals("doing"))) {
						 userWillDoList.add(user); 
					 }else {
						 userDoneList.add(user);
					 }
					 userDoList.add(user);
				 }
			 }
			 stepList.add(stepJson);
		 }
		dataJson.put(ProcessWorkcenterField.STEP.getValue(), stepList);
		dataJson.put(ProcessWorkcenterField.USER_WILL_DO.getValue(), userWillDoList);
		dataJson.put(ProcessWorkcenterField.USER_DO.getValue(), userDoList);
		dataJson.put(ProcessWorkcenterField.USER_DONE.getValue(), userDoneList);
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
}