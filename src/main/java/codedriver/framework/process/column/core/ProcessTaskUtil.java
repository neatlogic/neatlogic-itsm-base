package codedriver.framework.process.column.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.process.constvalue.ProcessField;
import codedriver.framework.process.dto.ProcessTaskStepCommentVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;

public class ProcessTaskUtil {
	public static JSONObject getProcessFieldData(ProcessTaskVo processTaskVo,Boolean isValue) {
		JSONObject resultObj = new JSONObject();
		resultObj.put(ProcessField.ID.getValue(), processTaskVo.getId());
		resultObj.put(ProcessField.TITLE.getValue(), processTaskVo.getTitle());
		resultObj.put(ProcessField.CHANNELTYPE.getValue(), isValue?processTaskVo.getChannelType().getUuid():processTaskVo.getChannelType().getName());
		resultObj.put(ProcessField.OWNER.getValue(), isValue?processTaskVo.getOwner():processTaskVo.getOwnerName());
		resultObj.put(ProcessField.REPORTER.getValue(),isValue?processTaskVo.getReporter():processTaskVo.getReporterName());
		resultObj.put(ProcessField.PRIORITY.getValue(), isValue?processTaskVo.getPriority().getUuid():processTaskVo.getPriority().getName());
		resultObj.put(ProcessField.STATUS.getValue(), isValue?processTaskVo.getStatusVo().getStatus():processTaskVo.getStatusVo().getText());
		
		ProcessTaskStepVo startProcessTaskStep = processTaskVo.getStartProcessTaskStep();
		ProcessTaskStepCommentVo comment = startProcessTaskStep.getComment();
		if(comment != null && StringUtils.isNotBlank(comment.getContent())) {
			resultObj.put(ProcessField.CONTENT.getValue(), comment.getContent());
		}else {
			resultObj.put(ProcessField.CONTENT.getValue(), "");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date endTime = processTaskVo.getEndTime();
		if(endTime != null) {
			resultObj.put(ProcessField.ENDTIME.getValue(), isValue?endTime.getTime():sdf.format(endTime));
		}else {
			resultObj.put(ProcessField.ENDTIME.getValue(), "");
		}
		Date startTime = processTaskVo.getStartTime();
		if(startTime != null) {
			resultObj.put(ProcessField.STARTTIME.getValue(), isValue?startTime.getTime():sdf.format(startTime));
		}else {
			resultObj.put(ProcessField.STARTTIME.getValue(), "");
		}
		Date expireTime = processTaskVo.getExpireTime();
		if(expireTime != null) {
			resultObj.put(ProcessField.EXPIREDTIME.getValue(), isValue?expireTime.getTime():sdf.format(expireTime));
		}else {
			resultObj.put(ProcessField.EXPIREDTIME.getValue(), "");
		}
		
		Map<String, Object> formAttributeDataMap = processTaskVo.getFormAttributeDataMap();
		if(MapUtils.isNotEmpty(formAttributeDataMap)) {
			resultObj.putAll(formAttributeDataMap);
		}
		
		return resultObj;
	}
}
