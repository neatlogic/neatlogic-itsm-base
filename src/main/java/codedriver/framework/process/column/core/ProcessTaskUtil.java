package codedriver.framework.process.column.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.dto.TeamVo;
import codedriver.framework.process.constvalue.ProcessField;
import codedriver.framework.process.constvalue.ProcessTaskParams;
import codedriver.framework.process.dto.AttributeDataVo;
import codedriver.framework.process.dto.FormAttributeVo;
import codedriver.framework.process.dto.FormVersionVo;
import codedriver.framework.process.dto.ProcessTaskStepReplyVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.formattribute.core.FormAttributeHandlerFactory;
import codedriver.framework.process.formattribute.core.IFormAttributeHandler;

public class ProcessTaskUtil {
	/**
	 * 
	* @Time:2020年7月3日
	* @Description: 获取工单信息及表单信息数据
	* @param processTaskVo 工单信息
	* @param isValue isValue=true时获取对应value值，用于条件判断，isValue=false时获取对应text值，用于模板替换
	* @return JSONObject
	 */
	public static JSONObject getProcessFieldData(ProcessTaskVo processTaskVo,Boolean isValue) {
		JSONObject resultObj = new JSONObject();
		/** 工单信息数据 **/
		resultObj.put(ProcessField.ID.getValue(), processTaskVo.getId());
		resultObj.put(ProcessField.TITLE.getValue(), processTaskVo.getTitle());
		resultObj.put(ProcessField.CHANNELTYPE.getValue(), isValue?processTaskVo.getChannelType().getUuid():processTaskVo.getChannelType().getName());
		resultObj.put(ProcessField.OWNER.getValue(), isValue?processTaskVo.getOwner():processTaskVo.getOwnerName());
		resultObj.put(ProcessField.REPORTER.getValue(),isValue?processTaskVo.getReporter():processTaskVo.getReporterName());
		resultObj.put(ProcessField.PRIORITY.getValue(), isValue?processTaskVo.getPriority().getUuid():processTaskVo.getPriority().getName());
		resultObj.put(ProcessField.STATUS.getValue(), isValue?processTaskVo.getStatusVo().getStatus():processTaskVo.getStatusVo().getText());
		resultObj.put(ProcessField.OWNERCOMPANY.getValue(), isValue?processTaskVo.getOwnerCompanyList().stream().map(TeamVo::getUuid).collect(Collectors.toList()):processTaskVo.getOwnerCompanyList().stream().map(TeamVo::getName).collect(Collectors.toList()));
		resultObj.put(ProcessField.STEPID.getValue(), processTaskVo.getCurrentProcessTaskStep() != null?processTaskVo.getCurrentProcessTaskStep().getId():null);
		
		ProcessTaskStepVo startProcessTaskStep = processTaskVo.getStartProcessTaskStep();
		ProcessTaskStepReplyVo comment = startProcessTaskStep.getComment();
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
		
		/** 表单信息数据 **/
		Map<String, Object> formAttributeDataMap = processTaskVo.getFormAttributeDataMap();
		if(MapUtils.isNotEmpty(formAttributeDataMap)) {
			if(isValue) {
				resultObj.putAll(formAttributeDataMap);
			}else {
				if(StringUtils.isNotBlank(processTaskVo.getFormConfig())) {
				    Map<String, Object> formAttributeMap = new HashMap<>();
					FormVersionVo formVersionVo = new FormVersionVo();
					formVersionVo.setFormConfig(processTaskVo.getFormConfig());
					List<FormAttributeVo> formAttributeList = formVersionVo.getFormAttributeList();
					for(FormAttributeVo formAttribute : formAttributeList) {
						Object attributeValue = formAttributeDataMap.get(formAttribute.getUuid());
						if(attributeValue != null) {
							IFormAttributeHandler handler = FormAttributeHandlerFactory.getHandler(formAttribute.getHandler());
							if(handler != null) {
								AttributeDataVo attributeDataVo = new AttributeDataVo();
								attributeDataVo.setAttributeUuid(formAttribute.getUuid());
								if(attributeValue instanceof String) {
									attributeDataVo.setData((String)attributeValue);
								}else if(attributeValue instanceof JSONArray){
									attributeDataVo.setData(JSON.toJSONString(attributeValue));
								}else if(attributeValue instanceof JSONObject) {
									attributeDataVo.setData(JSON.toJSONString(attributeValue));
								}
								Object value = handler.valueConversionText(attributeDataVo, JSONObject.parseObject(formAttribute.getConfig()));
								resultObj.put(formAttribute.getUuid(), value);
								formAttributeMap.put(formAttribute.getLabel(), value.toString());
							}else {
								resultObj.put(formAttribute.getUuid(), attributeValue);
								formAttributeMap.put(formAttribute.getLabel(), attributeValue.toString());
							}
						}
					}
					resultObj.put("form", formAttributeMap);
				}
			}
		}
		
		return resultObj;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
    public static JSONObject getProcessTaskParamData(ProcessTaskVo processTaskVo) {
        JSONObject resultObj = new JSONObject();
        /** 工单信息数据 **/
        resultObj.put(ProcessTaskParams.ID.getValue(), processTaskVo.getId());
        resultObj.put(ProcessTaskParams.TITLE.getValue(), processTaskVo.getTitle());
        resultObj.put(ProcessTaskParams.CHANNELTYPENAME.getValue(), processTaskVo.getChannelType().getName());
        resultObj.put(ProcessTaskParams.CHANNELPATH.getValue(), processTaskVo.getChannelPath());
        resultObj.put(ProcessTaskParams.OWNERNAME.getValue(), processTaskVo.getOwnerName());
        resultObj.put(ProcessTaskParams.REPORTERNAME.getValue(), processTaskVo.getReporterName());
        resultObj.put(ProcessTaskParams.PRIORITYNAME.getValue(), processTaskVo.getPriority().getName());
        resultObj.put(ProcessTaskParams.STATUSTEXT.getValue(), processTaskVo.getStatusVo().getText());
        resultObj.put(ProcessTaskParams.OWNERCOMPANYLIST.getValue(), processTaskVo.getOwnerCompanyList().stream().map(TeamVo::getName).collect(Collectors.toList()));
        resultObj.put(ProcessTaskParams.STEPID.getValue(), processTaskVo.getCurrentProcessTaskStep() != null ? processTaskVo.getCurrentProcessTaskStep().getId() : null);
        resultObj.put(ProcessTaskParams.STEPNAME.getValue(), processTaskVo.getCurrentProcessTaskStep() != null ? processTaskVo.getCurrentProcessTaskStep().getName() : null);
        
        ProcessTaskStepVo startProcessTaskStep = processTaskVo.getStartProcessTaskStep();
        ProcessTaskStepReplyVo comment = startProcessTaskStep.getComment();
        if(comment != null && StringUtils.isNotBlank(comment.getContent())) {
            resultObj.put(ProcessTaskParams.CONTENT.getValue(), comment.getContent());
        }else {
            resultObj.put(ProcessTaskParams.CONTENT.getValue(), "");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = processTaskVo.getEndTime();
        if(endTime != null) {
            resultObj.put(ProcessTaskParams.ENDTIME.getValue(), sdf.format(endTime));
        }else {
            resultObj.put(ProcessTaskParams.ENDTIME.getValue(), "");
        }
        Date startTime = processTaskVo.getStartTime();
        if(startTime != null) {
            resultObj.put(ProcessTaskParams.STARTTIME.getValue(), sdf.format(startTime));
        }else {
            resultObj.put(ProcessTaskParams.STARTTIME.getValue(), "");
        }
//        Date expireTime = processTaskVo.getExpireTime();
//        if(expireTime != null) {
//            resultObj.put(ProcessTaskParams.EXPIREDTIME.getValue(), sdf.format(expireTime));
//        }else {
//            resultObj.put(ProcessTaskParams.EXPIREDTIME.getValue(), "");
//        }
        
        /** 表单信息数据 **/
        Map<String, Object> formAttributeDataMap = processTaskVo.getFormAttributeDataMap();
        if(MapUtils.isNotEmpty(formAttributeDataMap)) {
            if(StringUtils.isNotBlank(processTaskVo.getFormConfig())) {
                Map<String, Object> formAttributeMap = new HashMap<>();
                FormVersionVo formVersionVo = new FormVersionVo();
                formVersionVo.setFormConfig(processTaskVo.getFormConfig());
                List<FormAttributeVo> formAttributeList = formVersionVo.getFormAttributeList();
                for(FormAttributeVo formAttribute : formAttributeList) {
                    Object attributeValue = formAttributeDataMap.get(formAttribute.getUuid());
                    if(attributeValue != null) {
                        IFormAttributeHandler handler = FormAttributeHandlerFactory.getHandler(formAttribute.getHandler());
                        if(handler != null) {
                            AttributeDataVo attributeDataVo = new AttributeDataVo();
                            attributeDataVo.setAttributeUuid(formAttribute.getUuid());
                            if(attributeValue instanceof String) {
                                attributeDataVo.setData((String)attributeValue);
                            }else if(attributeValue instanceof JSONArray){
                                attributeDataVo.setData(JSON.toJSONString(attributeValue));
                            }else if(attributeValue instanceof JSONObject) {
                                attributeDataVo.setData(JSON.toJSONString(attributeValue));
                            }
                            Object value = handler.valueConversionText(attributeDataVo, JSONObject.parseObject(formAttribute.getConfig()));
                            
                            resultObj.put(formAttribute.getUuid(), value);
                            if(value instanceof List) {
                                formAttributeMap.put(formAttribute.getLabel(), String.join("、", (List)value));
                            }else if(value instanceof String){
                                formAttributeMap.put(formAttribute.getLabel(), (String)value);
                            }else {
                                formAttributeMap.put(formAttribute.getLabel(), value.toString());
                            }
                        }else {
                            resultObj.put(formAttribute.getUuid(), attributeValue);
                            if(attributeValue instanceof List) {
                                formAttributeMap.put(formAttribute.getLabel(), String.join("、", (List)attributeValue));
                            }else if(attributeValue instanceof String){
                                formAttributeMap.put(formAttribute.getLabel(), (String)attributeValue);
                            }else {
                                formAttributeMap.put(formAttribute.getLabel(), attributeValue.toString());
                            }
                        }
                    }
                }
                resultObj.put(ProcessTaskParams.FORM.getValue(), formAttributeMap);
            }
        }
        
        return resultObj;
    }
}
