package codedriver.framework.process.notify.schedule.plugin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.file.dao.mapper.FileMapper;
import codedriver.framework.file.dto.FileVo;
import codedriver.framework.notify.dao.mapper.NotifyMapper;
import codedriver.framework.notify.dto.NotifyPolicyVo;
import codedriver.framework.notify.dto.NotifyReceiverVo;
import codedriver.framework.notify.dto.ParamMappingVo;
import codedriver.framework.process.column.core.ProcessTaskUtil;
import codedriver.framework.process.constvalue.ProcessStepType;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dao.mapper.ChannelMapper;
import codedriver.framework.process.dao.mapper.PriorityMapper;
import codedriver.framework.process.dao.mapper.ProcessStepHandlerMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.WorktimeMapper;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.PriorityVo;
import codedriver.framework.process.dto.ProcessStepHandlerVo;
import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskFileVo;
import codedriver.framework.process.dto.ProcessTaskFormAttributeDataVo;
import codedriver.framework.process.dto.ProcessTaskFormVo;
import codedriver.framework.process.dto.ProcessTaskSlaNotifyVo;
import codedriver.framework.process.dto.ProcessTaskSlaTimeVo;
import codedriver.framework.process.dto.ProcessTaskSlaVo;
import codedriver.framework.process.dto.ProcessTaskStepCommentVo;
import codedriver.framework.process.dto.ProcessTaskStepContentVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
import codedriver.framework.process.exception.processtask.ProcessTaskNotFoundException;
import codedriver.framework.process.notify.core.NotifyTriggerType;
import codedriver.framework.scheduler.core.JobBase;
import codedriver.framework.scheduler.dto.JobObject;
import codedriver.framework.util.NotifyPolicyUtil;

@Component
public class ProcessTaskSlaNotifyJob extends JobBase {
	static Logger logger = LoggerFactory.getLogger(ProcessTaskSlaNotifyJob.class);

	@Autowired
	private ProcessTaskMapper processTaskMapper;

	@Autowired
	private ProcessStepHandlerMapper processStepHandlerMapper;
	
	@Autowired
	private FileMapper fileMapper;
	
	@Autowired
	private PriorityMapper priorityMapper;
	
	@Autowired
	private NotifyMapper notifyMapper;
	
	@Autowired
	private ChannelMapper channelMapper;
	
	@Autowired
	private WorktimeMapper worktimeMapper;
	
	@Override
	public Boolean checkCronIsExpired(JobObject jobObject) {
		Long slaTransferId = (Long) jobObject.getData("slaNotifyId");
		ProcessTaskSlaNotifyVo processTaskSlaNotifyVo = processTaskMapper.getProcessTaskNotifyById(slaTransferId);
		if (processTaskSlaNotifyVo == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void reloadJob(JobObject jobObject) {
		System.out.println(this.getClassName() + "::reloadJob");
		String tenantUuid = jobObject.getTenantUuid();
		TenantContext.get().switchTenant(tenantUuid);
		Long slaNotifyId = (Long) jobObject.getData("slaNotifyId");
		ProcessTaskSlaNotifyVo processTaskSlaNotifyVo = processTaskMapper.getProcessTaskNotifyById(slaNotifyId);
		boolean isJobLoaded = false;
		if (processTaskSlaNotifyVo != null) {
			ProcessTaskSlaTimeVo slaTimeVo = processTaskMapper.getProcessTaskSlaTimeBySlaId(processTaskSlaNotifyVo.getSlaId());
			if (slaTimeVo != null) {
				if (processTaskSlaNotifyVo != null && processTaskSlaNotifyVo.getConfigObj() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					JSONObject policyObj = processTaskSlaNotifyVo.getConfigObj();
					String expression = policyObj.getString("expression");
					int time = policyObj.getIntValue("time");
					String unit = policyObj.getString("unit");
					String executeType = policyObj.getString("executeType");
					int intervalTime = policyObj.getIntValue("intervalTime");
					String intervalUnit = policyObj.getString("intervalUnit");
					Integer repeatCount = null;
					if ("loop".equals(executeType) && intervalTime > 0) {
						if (intervalUnit.equalsIgnoreCase("day")) {
							intervalTime = intervalTime * 24 * 60 * 60;
						} else {
							intervalTime = intervalTime * 60 * 60;
						}
					}else {
						repeatCount = 0;
						intervalTime = 60 * 60;
					}
					Calendar notifyDate = Calendar.getInstance();
					notifyDate.setTime(slaTimeVo.getExpireTime());
					if (expression.equalsIgnoreCase("before")) {
						time = -time;
					}
					if (StringUtils.isNotBlank(unit) && time != 0) {
						if (unit.equalsIgnoreCase("day")) {
							notifyDate.add(Calendar.DAY_OF_MONTH, time);
						} else if (unit.equalsIgnoreCase("hour")) {
							notifyDate.add(Calendar.HOUR, time);
						} else {
							notifyDate.add(Calendar.MINUTE, time);
						}
					}
					System.out.println("now: " + sdf.format(new Date()));
					System.out.println("notifyDate: " + sdf.format(notifyDate.getTime()));
					System.out.println(time + unit);
					System.out.println("intervalTime: " + intervalTime);
					System.out.println("repeatCount: " + repeatCount);
					JobObject.Builder newJobObjectBuilder = new JobObject.Builder(processTaskSlaNotifyVo.getId().toString(), this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid())
							.withBeginTime(notifyDate.getTime())
							.withIntervalInSeconds(intervalTime)
							.withRepeatCount(repeatCount)
							.addData("slaNotifyId", processTaskSlaNotifyVo.getId());
					JobObject newJobObject = newJobObjectBuilder.build();
					Date triggerDate = schedulerManager.loadJob(newJobObject);
					System.out.println("triggerDate：" + triggerDate);
					if (triggerDate != null) {
						// 更新通知记录时间
						processTaskSlaNotifyVo.setTriggerTime(triggerDate);
						processTaskMapper.updateProcessTaskSlaNotify(processTaskSlaNotifyVo);
						isJobLoaded = true;
					}
				}
			}
		}
		if (!isJobLoaded) {
			// 没有加载到作业，则删除通知记录
			processTaskMapper.deleteProcessTaskSlaNotifyById(slaNotifyId);
		}
	}

	@Override
	public void initJob(String tenantUuid) {
		List<ProcessTaskSlaNotifyVo> slaNotifyList = processTaskMapper.getAllProcessTaskSlaNotify();
		for (ProcessTaskSlaNotifyVo processTaskSlaNotifyVo : slaNotifyList) {
			JobObject.Builder jobObjectBuilder = new JobObject.Builder(processTaskSlaNotifyVo.getSlaId().toString(), this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid()).addData("slaNotifyId", processTaskSlaNotifyVo.getId());
			JobObject jobObject = jobObjectBuilder.build();
			this.reloadJob(jobObject);
		}
	}

	@Override
	public void executeInternal(JobExecutionContext context, JobObject jobObject) throws JobExecutionException {
		System.out.println(new Date() + ":时效通知...");
		Long slaNotifyId = (Long) jobObject.getData("slaNotifyId");
		ProcessTaskSlaNotifyVo processTaskSlaNotifyVo = processTaskMapper.getProcessTaskNotifyById(slaNotifyId);
		if (processTaskSlaNotifyVo != null) {
			Long slaId = processTaskSlaNotifyVo.getSlaId();
			List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepBaseInfoBySlaId(slaId);
			Iterator<ProcessTaskStepVo> it = processTaskStepList.iterator();
			while (it.hasNext()) {
				ProcessTaskStepVo processTaskStepVo = it.next();
				// 未处理、处理中和挂起的步骤才需要计算SLA
				if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.PENDING.getValue()) || processTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue()) || processTaskStepVo.getStatus().equals(ProcessTaskStatus.HANG.getValue())) {
					// 找到所有未完成步骤的处理人
				} else {
					it.remove();
				}
			}
			ProcessTaskSlaTimeVo processTaskSlaTimeVo = processTaskMapper.getProcessTaskSlaTimeBySlaId(slaId);
			ProcessTaskSlaVo processTaskSlaVo = processTaskMapper.getProcessTaskSlaById(slaId);
			/** 存在未完成步骤才发超时通知，否则清除通知作业 **/
			if (processTaskSlaVo != null && processTaskSlaTimeVo != null && MapUtils.isNotEmpty(processTaskSlaNotifyVo.getConfigObj()) && processTaskStepList.size() > 0) {
				JSONObject policyObj = processTaskSlaNotifyVo.getConfigObj();
				JSONObject notifyPolicyConfig = policyObj.getJSONObject("notifyPolicyConfig");
				if (MapUtils.isNotEmpty(notifyPolicyConfig)) {
					Long policyId = notifyPolicyConfig.getLong("policyId");
					NotifyPolicyVo notifyPolicyVo = notifyMapper.getNotifyPolicyById(policyId);
					if (notifyPolicyVo != null) {
						JSONObject policyConfig = notifyPolicyVo.getConfig();
						List<ParamMappingVo> paramMappingList = JSON.parseArray(notifyPolicyConfig.getJSONArray("paramMappingList").toJSONString(), ParamMappingVo.class);
						ProcessTaskVo processTaskVo = getProcessTaskDetailInfoById(processTaskSlaVo.getProcessTaskId());
						JSONObject conditionParamData = ProcessTaskUtil.getProcessFieldData(processTaskVo, true);
						JSONObject templateParamData = ProcessTaskUtil.getProcessFieldData(processTaskVo, false);
						Map<String, List<NotifyReceiverVo>> receiverMap = new HashMap<>();
						for(ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
							getReceiverMapByProcessTaskStepId(processTaskStepVo.getId(), receiverMap);							
						}
						NotifyPolicyUtil.execute(policyConfig, paramMappingList, NotifyTriggerType.TIMEOUT, templateParamData, conditionParamData, receiverMap);
					}
				}
//				ProcessTaskVo processTaskVo = getProcessTaskDetailInfoById(processTaskSlaVo.getProcessTaskId());
//				JSONObject policyObj = processTaskSlaNotifyVo.getConfigObj();
//				JSONObject notifyPolicyConfig = policyObj.getJSONObject("notifyPolicyConfig");
//				if (MapUtils.isNotEmpty(notifyPolicyConfig)) {
//					Long policyId = notifyPolicyConfig.getLong("policyId");
//					NotifyPolicyVo notifyPolicyVo = notifyMapper.getNotifyPolicyById(policyId);
//					List<ParamMappingVo> paramMappingList = JSON.parseArray(notifyPolicyConfig.getJSONArray("paramMappingList").toJSONString(), ParamMappingVo.class);
//					if (notifyPolicyVo != null) {
//						JSONObject policyConfig = notifyPolicyVo.getConfig();
//						if (MapUtils.isNotEmpty(policyConfig)) {
//							List<String> adminUserUuidList = JSON.parseArray(policyConfig.getJSONArray("adminUserUuidList").toJSONString(), String.class);
//							JSONArray triggerList = policyConfig.getJSONArray("triggerList");
//							for (int i = 0; i < triggerList.size(); i++) {
//								JSONObject triggerObj = triggerList.getJSONObject(i);
//								if (NotifyTriggerType.TIMEOUT.getTrigger().equalsIgnoreCase(triggerObj.getString("trigger"))) {
//									JSONArray notifyList = triggerObj.getJSONArray("notifyList");
//									if (CollectionUtils.isNotEmpty(notifyList)) {
//										Map<Long, NotifyTemplateVo> templateMap = new HashMap<>();
//										List<NotifyTemplateVo> templateList = JSON.parseArray(policyConfig.getJSONArray("templateList").toJSONString(), NotifyTemplateVo.class);
//										for (NotifyTemplateVo notifyTemplateVo : templateList) {
//											templateMap.put(notifyTemplateVo.getId(), notifyTemplateVo);
//										}
//										for (int j = 0; j < notifyList.size(); j++) {
//											JSONObject notifyObj = notifyList.getJSONObject(j);
//											JSONObject conditionConfig = notifyObj.getJSONObject("conditionConfig");
//											if (MapUtils.isNotEmpty(conditionConfig)) {
//												JSONArray conditionGroupList = conditionConfig.getJSONArray("conditionGroupList");
//												if (CollectionUtils.isNotEmpty(conditionGroupList)) {
//													JSONObject processFieldData = ProcessTaskUtil.getProcessFieldData(processTaskVo, true);
//													// 参数映射
//													if (CollectionUtils.isNotEmpty(paramMappingList)) {
//														for (ParamMappingVo paramMappingVo : paramMappingList) {
//															if (ProcessFieldType.CONSTANT.getValue().equals(paramMappingVo.getType())) {
//																processFieldData.put(paramMappingVo.getName(), paramMappingVo.getValue());
//															} else if (Objects.equals(paramMappingVo.getName(), paramMappingVo.getValue())) {
//																if (!processFieldData.containsKey(paramMappingVo.getValue())) {
//																	logger.error("没有找到工单参数'" + paramMappingVo.getValue() + "'信息");
//																}
//															} else {
//																Object processFieldValue = processFieldData.get(paramMappingVo.getValue());
//																if (processFieldValue != null) {
//																	processFieldData.put(paramMappingVo.getName(), processFieldValue);
//																} else {
//																	logger.error("没有找到参数'" + paramMappingVo.getValue() + "'信息");
//																}
//															}
//														}
//													}
//
//													try {
//														ConditionParamContext.init(processFieldData);
//														ConditionConfigVo conditionConfigVo = new ConditionConfigVo(conditionConfig);
//														String script = conditionConfigVo.buildScript();
//														// System.out.println(script);
//														if (!RunScriptUtil.runScript(script)) {
//															continue;
//														}
//													} catch (Exception e) {
//														logger.error(e.getMessage(), e);
//													} finally {
//														ConditionParamContext.get().release();
//													}
//												}
//											}
//											JSONArray actionList = notifyObj.getJSONArray("actionList");
//											for (int k = 0; k < actionList.size(); k++) {
//												JSONObject actionObj = actionList.getJSONObject(k);
//												List<String> receiverList = JSON.parseArray(actionObj.getJSONArray("receiverList").toJSONString(), String.class);
//												if (CollectionUtils.isNotEmpty(receiverList)) {
//													String notifyHandler = actionObj.getString("notifyHandler");
//													INotifyHandler handler = NotifyHandlerFactory.getHandler(notifyHandler);
//													if (handler == null) {
//														throw new NotifyHandlerNotFoundException(notifyHandler);
//													}
//													NotifyVo.Builder notifyBuilder = new NotifyVo.Builder(NotifyTriggerType.TIMEOUT);
//													if (CollectionUtils.isNotEmpty(adminUserUuidList)) {
//														notifyBuilder.setExceptionNotifyUserUuidList(adminUserUuidList);
//													}
//													Long templateId = actionObj.getLong("templateId");
//													if (templateId != null) {
//														NotifyTemplateVo notifyTemplateVo = templateMap.get(templateId);
//														if (notifyTemplateVo != null) {
//															notifyBuilder.withContentTemplate(notifyTemplateVo.getContent());
//															notifyBuilder.withTitleTemplate(notifyTemplateVo.getTitle());
//														}
//													}
//													/** 注入流程作业信息 不够将来再补充 **/
//													JSONObject processFieldData = ProcessTaskUtil.getProcessFieldData(processTaskVo, false);
//													for (ProcessField processField : ProcessField.values()) {
//														Object processFieldValue = processFieldData.get(processField.getValue());
//														if (processFieldValue != null) {
//															notifyBuilder.addData(processField.getValue(), processFieldValue);
//														} else {
//															logger.error("没有找到工单参数'" + processField.getValue() + "'信息");
//														}
//													}
//													// 参数映射
//													if (CollectionUtils.isNotEmpty(paramMappingList)) {
//														for (ParamMappingVo paramMappingVo : paramMappingList) {
//															if (ProcessFieldType.CONSTANT.getValue().equals(paramMappingVo.getType())) {
//																notifyBuilder.addData(paramMappingVo.getName(), paramMappingVo.getValue());
//															} else if (Objects.equals(paramMappingVo.getName(), paramMappingVo.getValue())) {
//																if (!processFieldData.containsKey(paramMappingVo.getValue())) {
//																	logger.error("没有找到工单参数'" + paramMappingVo.getValue() + "'信息");
//																}
//															} else {
//																Object processFieldValue = processFieldData.get(paramMappingVo.getValue());
//																if (processFieldValue != null) {
//																	notifyBuilder.addData(paramMappingVo.getName(), processFieldValue);
//																} else {
//																	logger.error("没有找到参数'" + paramMappingVo.getValue() + "'信息");
//																}
//															}
//														}
//													}
//													/** 注入结束 **/
//													for(ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
//														ProcessTaskStepVo stepVo = getProcessTaskStepDetailInfoById(processTaskStepVo.getId());
//														for (String receiver : receiverList) {
//															String[] split = receiver.split("#");
//															if (ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue().equals(split[0])) {
//																if (ProcessUserType.MAJOR.getValue().equals(split[1])) {
//																	ProcessTaskStepUserVo majorUser = stepVo.getMajorUser();
//																	if (majorUser != null) {
//																		notifyBuilder.addUserUuid(majorUser.getUserUuid());
//																	}
//																} else if (ProcessUserType.MINOR.getValue().equals(split[1])) {
//																	List<ProcessTaskStepUserVo> minorUserList = stepVo.getMinorUserList();
//																	for (ProcessTaskStepUserVo processTaskStepUserVo : minorUserList) {
//																		notifyBuilder.addUserUuid(processTaskStepUserVo.getUserUuid());
//																	}
//																} else if (ProcessUserType.AGENT.getValue().equals(split[1])) {
//																	List<ProcessTaskStepUserVo> agentUserList = stepVo.getAgentUserList();
//																	for (ProcessTaskStepUserVo processTaskStepUserVo : agentUserList) {
//																		notifyBuilder.addUserUuid(processTaskStepUserVo.getUserUuid());
//																	}
//																} else if (ProcessUserType.OWNER.getValue().equals(split[1])) {
//																	notifyBuilder.addUserUuid(processTaskVo.getOwner());
//																} else if (ProcessUserType.REPORTER.getValue().equals(split[1])) {
//																	notifyBuilder.addUserUuid(processTaskVo.getReporter());
//																} else if (ProcessUserType.WORKER.getValue().equals(split[1])) {
//																	List<ProcessTaskStepWorkerVo> workerList = stepVo.getWorkerList();
//																	for (ProcessTaskStepWorkerVo workerVo : workerList) {
//																		if (GroupSearch.USER.getValue().equals(workerVo.getType())) {
//																			notifyBuilder.addUserUuid(workerVo.getUuid());
//																		} else if (GroupSearch.TEAM.getValue().equals(workerVo.getType())) {
//																			notifyBuilder.addTeamId(workerVo.getUuid());
//																		} else if (GroupSearch.ROLE.getValue().equals(workerVo.getType())) {
//																			notifyBuilder.addRoleUuid(workerVo.getUuid());
//																		}
//																	}
//																}
//															} else if (GroupSearch.USER.getValue().equals(split[0])) {
//																notifyBuilder.addUserUuid(split[1]);
//															} else if (GroupSearch.TEAM.getValue().equals(split[0])) {
//																notifyBuilder.addTeamId(split[1]);
//															} else if (GroupSearch.ROLE.getValue().equals(split[0])) {
//																notifyBuilder.addRoleUuid(split[1]);
//															}
//														}
//														NotifyVo notifyVo = notifyBuilder.build();
//														handler.execute(notifyVo);
//													}
//												}
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
				Date nextFireTime = context.getNextFireTime();
				if (nextFireTime != null) {
					processTaskSlaNotifyVo.setTriggerTime(nextFireTime);
					processTaskMapper.updateProcessTaskSlaNotify(processTaskSlaNotifyVo);
				} else {
					// 删除通知记录
					processTaskMapper.deleteProcessTaskSlaNotifyById(processTaskSlaNotifyVo.getId());
				}
			} else {
				schedulerManager.unloadJob(jobObject);
				if (processTaskSlaNotifyVo != null) {
					// 删除通知记录
					processTaskMapper.deleteProcessTaskSlaNotifyById(processTaskSlaNotifyVo.getId());
				}
			}
		} else {
			schedulerManager.unloadJob(jobObject);
		}
	}

	@Override
	public String getGroupName() {
		return TenantContext.get().getTenantUuid() + "-PROCESSTASK-SLA-NOTIFY";
	}
	
	private ProcessTaskVo getProcessTaskDetailInfoById(Long processTaskId) {
		ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
		if (processTaskVo == null) {
			throw new ProcessTaskNotFoundException(processTaskId.toString());
		}

		// 获取工单流程图信息
		ProcessTaskConfigVo processTaskConfig = processTaskMapper.getProcessTaskConfigByHash(processTaskVo.getConfigHash());
		if (processTaskConfig == null) {
			throw new ProcessTaskRuntimeException("没有找到工单：'" + processTaskId + "'的流程图配置信息");
		}
		processTaskVo.setConfig(processTaskConfig.getConfig());
		// 获取开始步骤id
		List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.START.getValue());
		if (processTaskStepList.size() != 1) {
			throw new ProcessTaskRuntimeException("工单：'" + processTaskId + "'有" + processTaskStepList.size() + "个开始步骤");
		}

		ProcessTaskStepVo startProcessTaskStepVo = processTaskStepList.get(0);
		String startStepConfig = processTaskMapper.getProcessTaskStepConfigByHash(startProcessTaskStepVo.getConfigHash());
		startProcessTaskStepVo.setConfig(startStepConfig);
		ProcessStepHandlerVo processStepHandlerConfig = processStepHandlerMapper.getProcessStepHandlerByHandler(startProcessTaskStepVo.getHandler());
		if(processStepHandlerConfig != null) {
			startProcessTaskStepVo.setGlobalConfig(processStepHandlerConfig.getConfig());					
		}
		Long startProcessTaskStepId = startProcessTaskStepVo.getId();
		ProcessTaskStepCommentVo comment = new ProcessTaskStepCommentVo();
		// 获取上报描述内容
		List<ProcessTaskStepContentVo> processTaskStepContentList = processTaskMapper.getProcessTaskStepContentProcessTaskStepId(startProcessTaskStepId);
		if (!processTaskStepContentList.isEmpty()) {
			ProcessTaskContentVo processTaskContentVo = processTaskMapper.getProcessTaskContentByHash(processTaskStepContentList.get(0).getContentHash());
			if (processTaskContentVo != null) {
				comment.setContent(processTaskContentVo.getContent());
			}
		}
		// 附件
		ProcessTaskFileVo processTaskFileVo = new ProcessTaskFileVo();
		processTaskFileVo.setProcessTaskId(processTaskId);
		processTaskFileVo.setProcessTaskStepId(startProcessTaskStepId);
		List<ProcessTaskFileVo> processTaskFileList = processTaskMapper.searchProcessTaskFile(processTaskFileVo);

		if (processTaskFileList.size() > 0) {
			List<FileVo> fileList = new ArrayList<>();
			for (ProcessTaskFileVo processTaskFile : processTaskFileList) {
				FileVo fileVo = fileMapper.getFileById(processTaskFile.getFileId());
				fileList.add(fileVo);
			}
			comment.setFileList(fileList);
		}
		startProcessTaskStepVo.setComment(comment);
		processTaskVo.setStartProcessTaskStep(startProcessTaskStepVo);

		// 优先级
		PriorityVo priorityVo = priorityMapper.getPriorityByUuid(processTaskVo.getPriorityUuid());
		processTaskVo.setPriority(priorityVo);
		// 上报服务路径
		ChannelVo channelVo = channelMapper.getChannelByUuid(processTaskVo.getChannelUuid());
		if (channelVo != null) {
			StringBuilder channelPath = new StringBuilder();
			List<String> ancestorNameList = channelMapper.getAllAncestorNameListByParentUuid(channelVo.getParentUuid());
			for (String name : ancestorNameList) {
				channelPath.append(name);
				channelPath.append("/");
			}
			channelPath.append(channelVo.getName());
			processTaskVo.setChannelPath(channelPath.toString());
			processTaskVo.setChannelType(channelMapper.getChannelTypeByUuid(channelVo.getChannelTypeUuid()));
		}
		// 耗时
		if (processTaskVo.getEndTime() != null) {
			long timeCost = worktimeMapper.calculateCostTime(processTaskVo.getWorktimeUuid(), processTaskVo.getStartTime().getTime(), processTaskVo.getEndTime().getTime());
			processTaskVo.setTimeCost(timeCost);
		}

		// 获取工单表单信息
		ProcessTaskFormVo processTaskFormVo = processTaskMapper.getProcessTaskFormByProcessTaskId(processTaskId);
		if (processTaskFormVo != null && StringUtils.isNotBlank(processTaskFormVo.getFormContent())) {
			processTaskVo.setFormConfig(processTaskFormVo.getFormContent());
			List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(processTaskId);
			if (CollectionUtils.isNotEmpty(processTaskFormAttributeDataList)) {
				Map<String, Object> formAttributeDataMap = new HashMap<>();
				for (ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo : processTaskFormAttributeDataList) {
					formAttributeDataMap.put(processTaskFormAttributeDataVo.getAttributeUuid(), processTaskFormAttributeDataVo.getDataObj());
				}
				processTaskVo.setFormAttributeDataMap(formAttributeDataMap);
			}
		}
		return processTaskVo;
	}

	private void getReceiverMapByProcessTaskStepId(Long processTaskStepId, Map<String, List<NotifyReceiverVo>> receiverMap) {

		List<ProcessTaskStepUserVo> majorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.MAJOR.getValue());
		if (CollectionUtils.isNotEmpty(majorUserList)) {
			List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.MAJOR.getValue());
			if(notifyReceiverList == null) {
				notifyReceiverList = new ArrayList<>();
				receiverMap.put(ProcessUserType.MAJOR.getValue(), notifyReceiverList);
			}
			notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), majorUserList.get(0).getUserUuid()));
		}
		List<ProcessTaskStepUserVo> minorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.MINOR.getValue());
		if(CollectionUtils.isNotEmpty(minorUserList)) {
			List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.MINOR.getValue());
			if(notifyReceiverList == null) {
				notifyReceiverList = new ArrayList<>();
				receiverMap.put(ProcessUserType.MINOR.getValue(), notifyReceiverList);
			}
			for(ProcessTaskStepUserVo processTaskStepUserVo : minorUserList) {
				notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskStepUserVo.getUserUuid()));
			}
		}
		List<ProcessTaskStepUserVo> agentUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.AGENT.getValue());
		if(CollectionUtils.isNotEmpty(agentUserList)) {
			List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.AGENT.getValue());
			if(notifyReceiverList == null) {
				notifyReceiverList = new ArrayList<>();
				receiverMap.put(ProcessUserType.AGENT.getValue(), notifyReceiverList);
			}
			for(ProcessTaskStepUserVo processTaskStepUserVo : agentUserList) {
				notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskStepUserVo.getUserUuid()));
			}
		}
		List<ProcessTaskStepWorkerVo> workerList = processTaskMapper.getProcessTaskStepWorkerByProcessTaskStepId(processTaskStepId);
		if(CollectionUtils.isNotEmpty(workerList)) {
			List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.WORKER.getValue());
			if(notifyReceiverList != null) {
				notifyReceiverList = new ArrayList<>();
				receiverMap.put(ProcessUserType.WORKER.getValue(), notifyReceiverList);
			}
			for(ProcessTaskStepWorkerVo processTaskStepWorkerVo : workerList) {
				notifyReceiverList.add(new NotifyReceiverVo(processTaskStepWorkerVo.getType(), processTaskStepWorkerVo.getUuid()));
			}
		}
	}

}
