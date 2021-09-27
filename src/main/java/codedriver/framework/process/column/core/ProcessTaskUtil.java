package codedriver.framework.process.column.core;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.condition.core.ConditionHandlerFactory;
import codedriver.framework.dto.TeamVo;
import codedriver.framework.dto.UrlInfoVo;
import codedriver.framework.dto.UserVo;
import codedriver.framework.form.attribute.core.FormAttributeHandlerFactory;
import codedriver.framework.form.attribute.core.IFormAttributeHandler;
import codedriver.framework.form.dto.AttributeDataVo;
import codedriver.framework.form.dto.FormAttributeVo;
import codedriver.framework.form.dto.FormVersionVo;
import codedriver.framework.process.condition.core.IProcessTaskCondition;
import codedriver.framework.process.constvalue.ProcessField;
import codedriver.framework.process.constvalue.ProcessTaskParams;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dto.*;
import codedriver.framework.util.HtmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ProcessTaskUtil {
    /**
     * @param processTaskVo 工单信息
     * @param isValue       isValue=true时获取对应value值，用于条件判断，isValue=false时获取对应text值，用于模板替换
     * @return JSONObject
     * @Time:2020年7月3日
     * @Description: 获取工单信息及表单信息数据
     */
    public static JSONObject getProcessFieldData(ProcessTaskVo processTaskVo, Boolean isValue) {
        JSONObject resultObj = new JSONObject();
        /** 工单信息数据 **/
        resultObj.put(ProcessField.ID.getValue(), processTaskVo.getId());
        resultObj.put(ProcessField.TITLE.getValue(), processTaskVo.getTitle());
        resultObj.put(ProcessField.CHANNELTYPE.getValue(), isValue ? processTaskVo.getChannelType().getUuid() : processTaskVo.getChannelType().getName());
        resultObj.put(ProcessField.OWNER.getValue(), isValue ? processTaskVo.getOwner() : processTaskVo.getOwnerVo().getUserName());
        resultObj.put(ProcessField.OWNERLEVEL.getValue(), processTaskVo.getOwnerVo().getVipLevel());
        resultObj.put(ProcessField.REPORTER.getValue(), isValue ? processTaskVo.getReporter() : processTaskVo.getReporterVo().getUserName());
        resultObj.put(ProcessField.PRIORITY.getValue(), isValue ? processTaskVo.getPriority().getUuid() : processTaskVo.getPriority().getName());
        resultObj.put(ProcessField.STATUS.getValue(), isValue ? processTaskVo.getStatusVo().getStatus() : processTaskVo.getStatusVo().getText());
        resultObj.put(ProcessField.OWNERCOMPANY.getValue(), isValue ? processTaskVo.getOwnerCompanyList().stream().map(TeamVo::getUuid).collect(Collectors.toList()) : processTaskVo.getOwnerCompanyList().stream().map(TeamVo::getName).collect(Collectors.toList()));
        resultObj.put(ProcessField.OWNERDEPARTMENT.getValue(), isValue ? processTaskVo.getOwnerDepartmentList().stream().map(TeamVo::getUuid).collect(Collectors.toList()) : processTaskVo.getOwnerDepartmentList().stream().map(TeamVo::getName).collect(Collectors.toList()));
        resultObj.put(ProcessField.STEPID.getValue(), processTaskVo.getCurrentProcessTaskStep() != null ? processTaskVo.getCurrentProcessTaskStep().getId() : null);
        resultObj.put(ProcessField.OWNERROLE.getValue(), ((IProcessTaskCondition)ConditionHandlerFactory.getHandler(ProcessField.OWNERROLE.getValue())).getConditionParamData(processTaskVo));
        resultObj.put(ProcessField.STEPTASK.getValue(), ((IProcessTaskCondition)ConditionHandlerFactory.getHandler(ProcessField.STEPTASK.getValue())).getConditionParamData(processTaskVo));
        ProcessTaskStepVo startProcessTaskStep = processTaskVo.getStartProcessTaskStep();
        ProcessTaskStepReplyVo comment = startProcessTaskStep.getComment();
        if (comment != null && StringUtils.isNotBlank(comment.getContent())) {
            resultObj.put(ProcessField.CONTENT.getValue(), comment.getContent());
        } else {
            resultObj.put(ProcessField.CONTENT.getValue(), "");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = processTaskVo.getEndTime();
        if (endTime != null) {
            resultObj.put(ProcessField.ENDTIME.getValue(), isValue ? endTime.getTime() : sdf.format(endTime));
        } else {
            resultObj.put(ProcessField.ENDTIME.getValue(), "");
        }
        Date startTime = processTaskVo.getStartTime();
        if (startTime != null) {
            resultObj.put(ProcessField.STARTTIME.getValue(), isValue ? startTime.getTime() : sdf.format(startTime));
        } else {
            resultObj.put(ProcessField.STARTTIME.getValue(), "");
        }
        Date expireTime = processTaskVo.getExpireTime();
        if (expireTime != null) {
            resultObj.put(ProcessField.EXPIREDTIME.getValue(), isValue ? expireTime.getTime() : sdf.format(expireTime));
        } else {
            resultObj.put(ProcessField.EXPIREDTIME.getValue(), "");
        }
        // 当前子任务id
        resultObj.put(ProcessField.SUBTASKID.getValue(), processTaskVo.getCurrentProcessTaskStep() != null ? processTaskVo.getCurrentProcessTaskStep().getCurrentSubtaskId() : null);

        /** 表单信息数据 **/
        Map<String, Object> formAttributeDataMap = processTaskVo.getFormAttributeDataMap();
        if (MapUtils.isNotEmpty(formAttributeDataMap)) {
            if (isValue) {
                resultObj.putAll(formAttributeDataMap);
            } else {
                if (MapUtils.isNotEmpty(processTaskVo.getFormConfig())) {
                    Map<String, Object> formAttributeMap = new HashMap<>();
                    FormVersionVo formVersionVo = new FormVersionVo();
                    formVersionVo.setFormConfig(processTaskVo.getFormConfig().toJSONString());
                    List<FormAttributeVo> formAttributeList = formVersionVo.getFormAttributeList();
                    for (FormAttributeVo formAttribute : formAttributeList) {
                        Object attributeValue = formAttributeDataMap.get(formAttribute.getUuid());
                        if (attributeValue != null) {
                            IFormAttributeHandler handler = FormAttributeHandlerFactory.getHandler(formAttribute.getHandler());
                            if (handler != null) {
                                AttributeDataVo attributeDataVo = new AttributeDataVo();
                                attributeDataVo.setAttributeUuid(formAttribute.getUuid());
                                if (attributeValue instanceof String) {
                                    attributeDataVo.setData((String) attributeValue);
                                } else if (attributeValue instanceof JSONArray) {
                                    attributeDataVo.setData(JSON.toJSONString(attributeValue));
                                } else if (attributeValue instanceof JSONObject) {
                                    attributeDataVo.setData(JSON.toJSONString(attributeValue));
                                }
                                Object value = handler.valueConversionText(attributeDataVo, JSONObject.parseObject(formAttribute.getConfig()));
                                resultObj.put(formAttribute.getUuid(), value);
                                formAttributeMap.put(formAttribute.getLabel(), value.toString());
                            } else {
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


    public static JSONObject getProcessTaskParamData(ProcessTaskVo processTaskVo) {
        JSONObject resultObj = new JSONObject();
        /** 工单信息数据 **/
        resultObj.put(ProcessTaskParams.ID.getValue(), processTaskVo.getId());
        resultObj.put(ProcessTaskParams.SERIALNUMBER.getValue(), processTaskVo.getSerialNumber());
        resultObj.put(ProcessTaskParams.TITLE.getValue(), processTaskVo.getTitle());
        resultObj.put(ProcessTaskParams.CHANNELTYPENAME.getValue(), processTaskVo.getChannelType().getName());
        resultObj.put(ProcessTaskParams.CHANNELPATH.getValue(), processTaskVo.getChannelPath());
        resultObj.put(ProcessTaskParams.OWNERNAME.getValue(), processTaskVo.getOwnerVo().getUserName());
        resultObj.put(ProcessTaskParams.REPORTERNAME.getValue(), processTaskVo.getReporterVo().getUserName());
        resultObj.put(ProcessTaskParams.PRIORITYNAME.getValue(), processTaskVo.getPriority().getName());
        resultObj.put(ProcessTaskParams.STATUSTEXT.getValue(), processTaskVo.getStatusVo().getText());
        resultObj.put(ProcessTaskParams.OWNERCOMPANYLIST.getValue(), processTaskVo.getOwnerCompanyList().stream().map(TeamVo::getName).collect(Collectors.toList()));
        resultObj.put(ProcessTaskParams.OPERATOR.getValue(), UserContext.get().getUserName());

        ProcessTaskStepVo startProcessTaskStep = processTaskVo.getStartProcessTaskStep();
        ProcessTaskStepReplyVo comment = startProcessTaskStep.getComment();
        if (comment != null && StringUtils.isNotBlank(comment.getContent())) {
            List<UrlInfoVo> urlInfoVoList = HtmlUtil.getUrlInfoList(comment.getContent(), "<img src=\"", "\"");
            String content = HtmlUtil.urlReplace(comment.getContent(), urlInfoVoList);
            resultObj.put(ProcessTaskParams.CONTENT.getValue(), content);
        } else {
            resultObj.put(ProcessTaskParams.CONTENT.getValue(), "");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endTime = processTaskVo.getEndTime();
        if (endTime != null) {
            resultObj.put(ProcessTaskParams.ENDTIME.getValue(), sdf.format(endTime));
        } else {
            resultObj.put(ProcessTaskParams.ENDTIME.getValue(), "");
        }
        Date startTime = processTaskVo.getStartTime();
        if (startTime != null) {
            resultObj.put(ProcessTaskParams.STARTTIME.getValue(), sdf.format(startTime));
        } else {
            resultObj.put(ProcessTaskParams.STARTTIME.getValue(), "");
        }

        /** 表单信息数据 **/
        List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = processTaskVo.getProcessTaskFormAttributeDataList();
        if (CollectionUtils.isNotEmpty(processTaskFormAttributeDataList)) {
            Map<String, ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataMap = processTaskFormAttributeDataList.stream().collect(Collectors.toMap(e -> e.getAttributeUuid(), e -> e));
            if (MapUtils.isNotEmpty(processTaskVo.getFormConfig())) {
                List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataVoList = new ArrayList<>();
                FormVersionVo formVersionVo = new FormVersionVo();
                formVersionVo.setFormConfig(processTaskVo.getFormConfig().toJSONString());
                List<FormAttributeVo> formAttributeList = formVersionVo.getFormAttributeList();
                for (FormAttributeVo formAttribute : formAttributeList) {
                    ProcessTaskFormAttributeDataVo attributeDataVo = processTaskFormAttributeDataMap.get(formAttribute.getUuid());
                    if (attributeDataVo != null) {
                        attributeDataVo.setLabel(formAttribute.getLabel());
                        if (attributeDataVo.getData() != null) {
                            IFormAttributeHandler handler = FormAttributeHandlerFactory.getHandler(formAttribute.getHandler());
                            if (handler != null) {
                                Object value = handler.dataTransformationForEmail(attributeDataVo, JSONObject.parseObject(formAttribute.getConfig()));
                                attributeDataVo.setDataObj(value);
                                processTaskFormAttributeDataVoList.add(attributeDataVo);
                            }
                        }
                    }
                }
                resultObj.put(ProcessTaskParams.FORM.getValue(), processTaskFormAttributeDataVoList);
            }
        }
        ProcessTaskStepVo currentProcessTaskStep = processTaskVo.getCurrentProcessTaskStep();
        if (currentProcessTaskStep != null) {
            resultObj.put(ProcessTaskParams.STEPID.getValue(), currentProcessTaskStep.getId());
            resultObj.put(ProcessTaskParams.STEPNAME.getValue(), currentProcessTaskStep.getName());
            List<ProcessTaskStepWorkerVo> processTaskStepWorkerList = currentProcessTaskStep.getWorkerList();
            if (CollectionUtils.isNotEmpty(processTaskStepWorkerList)) {
                List<String> workerNameList = processTaskStepWorkerList.stream().filter(e -> e.getUserType().equals(ProcessUserType.MAJOR.getValue())).map(ProcessTaskStepWorkerVo::getName).collect(Collectors.toList());
                resultObj.put(ProcessTaskParams.STEPWORKER.getValue(), String.join("、", workerNameList));
            } else if (CollectionUtils.isNotEmpty(currentProcessTaskStep.getUserList())) {
                List<String> userNameList = currentProcessTaskStep.getUserList().stream().filter(e -> e.getUserType().equals(ProcessUserType.MAJOR.getValue())).map(e -> e.getUserVo().getUserName()).collect(Collectors.toList());
                resultObj.put(ProcessTaskParams.STEPWORKER.getValue(), String.join("、", userNameList));
            }
            ProcessTaskStepSubtaskVo subtaskVo = currentProcessTaskStep.getCurrentSubtaskVo();
            if (subtaskVo != null) {
                resultObj.put(ProcessTaskParams.SUBTASKWORKER.getValue(), subtaskVo.getUserName());
                resultObj.put(ProcessTaskParams.SUBTASKCONTENT.getValue(), subtaskVo.getContent());
                resultObj.put(ProcessTaskParams.SUBTASKDEADLINE.getValue(), sdf.format(subtaskVo.getTargetTime()));
            }
            JSONObject paramObj = currentProcessTaskStep.getParamObj();
            if (MapUtils.isNotEmpty(paramObj)) {
                String reason = paramObj.getString("content");
                if (StringUtils.isNotBlank(reason)) {
                    List<UrlInfoVo> urlInfoVoList = HtmlUtil.getUrlInfoList(reason, "<img src=\"", "\"");
                    if (CollectionUtils.isNotEmpty(urlInfoVoList)) {
                        reason = HtmlUtil.urlReplace(reason, urlInfoVoList);
                    }
                }
                resultObj.put(ProcessTaskParams.REASON.getValue(), reason);
                Long changeStepId = paramObj.getLong("changeStepId");
                if (changeStepId != null) {
                    Object handlerStepInfo = currentProcessTaskStep.getHandlerStepInfo();
                    if (handlerStepInfo != null) {
                        JSONObject jsonObj = (JSONObject) JSON.toJSON(handlerStepInfo);
                        JSONArray changeStepList = jsonObj.getJSONArray("changeStepList");
                        if (CollectionUtils.isNotEmpty(changeStepList)) {
                            for (int i = 0; i < changeStepList.size(); i++) {
                                JSONObject changeStepObj = changeStepList.getJSONObject(i);
                                if (changeStepId.equals(changeStepObj.getLong("id"))) {
                                    resultObj.put(ProcessTaskParams.CHANGESTEPNAME.getValue(), changeStepObj.getString("name"));
                                    resultObj.put(ProcessTaskParams.CHANGESTEPWORKER.getValue(), changeStepObj.getJSONObject("workerVo").getString("name"));
                                }
                            }
                        }
                    }
                }
                //任务
                ProcessTaskStepTaskVo stepTaskVo = currentProcessTaskStep.getProcessTaskStepTaskVo();
                if(stepTaskVo != null ){
                    resultObj.put(ProcessTaskParams.TASKCONFIGNAME.getValue(),stepTaskVo.getTaskConfigName());
                    resultObj.put(ProcessTaskParams.TASKCONTENT.getValue(),stepTaskVo.getContent());
                    resultObj.put(ProcessTaskParams.TASKUSERCONTENT.getValue(),stepTaskVo.getTaskStepTaskUserContent());
                    if(CollectionUtils.isNotEmpty(stepTaskVo.getStepTaskUserVoList())){
                        List<UserVo> userVoList = stepTaskVo.getStepTaskUserVoList().stream().map(ProcessTaskStepTaskUserVo::getUserVo).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(userVoList)){
                            List<String> users = userVoList.stream().map(u->u.getName()+"("+u.getUserId()+")").collect(Collectors.toList());
                            resultObj.put(ProcessTaskParams.TASKWORKER.getValue(),String.join(",",users));
                        }
                    }

                }
            }
        }
        return resultObj;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static String joinToString(Object obj) {
        if (obj instanceof List) {
            return String.join("、", (List) obj);
        } else if (obj instanceof String) {
            return (String) obj;
        } else {
            return obj.toString();
        }
    }
}
