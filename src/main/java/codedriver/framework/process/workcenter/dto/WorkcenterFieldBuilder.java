package codedriver.framework.process.workcenter.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
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
	
	public WorkcenterFieldBuilder setSerialNumber(String serialNumber) {
        dataJson.put(ProcessWorkcenterField.SERIAL_NUMBER.getValue(), serialNumber);
        return this;
    }
	
	public WorkcenterFieldBuilder setTitle(String title) {
		dataJson.put(ProcessWorkcenterField.TITLE.getValue(), title);
		return this;
	}
	public WorkcenterFieldBuilder setProcessUuid(String process) {
        dataJson.put(ProcessWorkcenterField.PROCESS.getValue(), process);
        return this;
    }
	
	public WorkcenterFieldBuilder setConfigHash(String configHash) {
        dataJson.put(ProcessWorkcenterField.CONFIGHASH.getValue(), configHash);
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
		dataJson.put(ProcessWorkcenterField.CONTENT_INCLUDE_HTML.getValue(), contentIncludeHtml);
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
		dataJson.put(ProcessWorkcenterField.OWNER.getValue(), String.format("%s%s", GroupSearch.USER.getValuePlugin(),owner));
		return this;
	}
	public WorkcenterFieldBuilder setReporter(String reporter,String owner) {
		if(!reporter.equals(owner)) {
			dataJson.put(ProcessWorkcenterField.REPORTER.getValue(), String.format("%s%s", GroupSearch.USER.getValuePlugin(), reporter));
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
	
	private void getUserType(Map<String,JSONObject> userTypeMap,JSONArray userTypeArray) {
		
		for (ProcessUserType s : ProcessUserType.values()) {
			List<String> userlist = new ArrayList<String>();
			JSONObject userTypeJson = new JSONObject();
			userTypeJson.put("usertype", s.getValue());
			userTypeJson.put("usertypename", s.getText());
			userTypeJson.put("userlist", userlist);
			userTypeJson.put("list", new JSONArray());
			userTypeMap.put(s.getValue(), userTypeJson);
			userTypeArray.add(userTypeJson);
		}
	}
	
	public WorkcenterFieldBuilder setStepList( List<ProcessTaskStepVo>  processTaskStepList) {
		JSONArray stepList = new JSONArray();
		 for(ProcessTaskStepVo step : processTaskStepList) {
			 Map<String,JSONObject> userTypeMap = new HashMap<String, JSONObject>();
			 JSONObject stepJson = new JSONObject();
			 JSONArray userTypeArray = new JSONArray();
			 getUserType(userTypeMap,userTypeArray);
			 stepJson.put("id", step.getId());
			 stepJson.put("name", step.getName());
			 stepJson.put("status", step.getStatus());
			 stepJson.put("type", step.getType());
			 stepJson.put("handler", step.getHandler());
			 stepJson.put("isactive", step.getIsActive());
			 stepJson.put("confighash", step.getConfigHash());
			 stepJson.put("starttime", step.getStartTime());
			 stepJson.put("endtime", step.getEndTime());
			 stepJson.put("usertypelist", userTypeArray);
			 //已激活未开始
			 if(step.getStatus().equals(ProcessTaskStatus.PENDING.getValue()) && step.getIsActive() == 1) {
				 for(ProcessTaskStepWorkerVo worker : step.getWorkerList()) {
					 JSONObject userTypeJson = userTypeMap.get(ProcessUserType.WORKER.getValue());
					 userTypeJson.getJSONArray("userlist").add(worker.getWorkerValue());
					 JSONObject userStatusJson = new JSONObject();
					 userStatusJson.put("value", worker.getWorkerValue());
					 userStatusJson.put("status", ProcessTaskStatus.PENDING.getValue());
					 userTypeJson.getJSONArray("list").add(userStatusJson);
				 }
			 }else {
				 for(ProcessTaskStepUserVo userVo : step.getUserList()) {
					 String user = String.format("%s%s", GroupSearch.USER.getValuePlugin(),userVo.getUserVo().getUuid());
					 JSONObject userTypeJson = userTypeMap.get(userVo.getUserType());
					 userTypeJson.getJSONArray("userlist").add(user);
					 JSONObject userStatusJson = new JSONObject();
					 userStatusJson.put("value", user);
					 userStatusJson.put("status", userVo.getStatus());
					 userTypeJson.getJSONArray("list").add(userStatusJson);
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
			JSONObject configJson = processTaskSlaVo.getConfigObj();
			JSONObject expireTimeConifg = new JSONObject();
			if(configJson.containsKey("notifyPolicyList")&&CollectionUtils.isNotEmpty(configJson.getJSONArray("notifyPolicyList"))) {
				JSONArray notifyPolicyList = configJson.getJSONArray("notifyPolicyList");
				Integer time = -1;
				for(int i =0;i<notifyPolicyList.size();i++) {
					JSONObject notifyPolicyJson = notifyPolicyList.getJSONObject(i);
					if(notifyPolicyJson.getString("expression").equals("before")) {
						if(time == -1|| time > notifyPolicyJson.getIntValue("time")) {
							time = notifyPolicyJson.getIntValue("time");
						}
					}
				}
				expireTimeConifg.put("willOverTimeRule", time);
			}
			processTaskSlaVo.setConfig(expireTimeConifg.toJSONString()); //es 同一个key 不支持存储不同类型的value,所以存处理过的数据结构
			processTaskSlaVo.setConfigObj(expireTimeConifg); //es 同一个key 不支持存储不同类型的value,所以存处理过的数据结构
			
		}
		dataJson.put(ProcessWorkcenterField.EXPIRED_TIME.getValue(), JSONArray.toJSON(processTaskSlaList));
		return this;
	}

	public WorkcenterFieldBuilder setFocusUsers(List<String> focusUsers){
		dataJson.put(ProcessWorkcenterField.FOCUS_USERS.getValue(),focusUsers);
		return this;
	}
	
	public WorkcenterFieldBuilder setIsShow(Integer isShow) {
	    dataJson.put(ProcessWorkcenterField.IS_SHOW.getValue(),isShow);
        return this;
	}
}
