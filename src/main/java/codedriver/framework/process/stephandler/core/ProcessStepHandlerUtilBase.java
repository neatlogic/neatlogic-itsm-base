package codedriver.framework.process.stephandler.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.asynchronization.thread.CodeDriverThread;
import codedriver.framework.asynchronization.threadlocal.ConditionParamContext;
import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.asynchronization.threadpool.CachedThreadPool;
import codedriver.framework.asynchronization.threadpool.CommonThreadPool;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.constvalue.SystemUser;
import codedriver.framework.common.constvalue.UserType;
import codedriver.framework.dao.mapper.TeamMapper;
import codedriver.framework.dao.mapper.UserMapper;
import codedriver.framework.dto.condition.ConditionConfigVo;
import codedriver.framework.exception.integration.IntegrationHandlerNotFoundException;
import codedriver.framework.exception.integration.IntegrationNotFoundException;
import codedriver.framework.exception.integration.IntegrationSendRequestException;
import codedriver.framework.file.dao.mapper.FileMapper;
import codedriver.framework.file.dto.FileVo;
import codedriver.framework.integration.core.IIntegrationHandler;
import codedriver.framework.integration.core.IntegrationHandlerFactory;
import codedriver.framework.integration.dao.mapper.IntegrationMapper;
import codedriver.framework.integration.dto.IntegrationResultVo;
import codedriver.framework.integration.dto.IntegrationVo;
import codedriver.framework.notify.dao.mapper.NotifyMapper;
import codedriver.framework.notify.dto.NotifyPolicyVo;
import codedriver.framework.notify.dto.NotifyReceiverVo;
import codedriver.framework.notify.dto.ParamMappingVo;
import codedriver.framework.process.column.core.ProcessTaskUtil;
import codedriver.framework.process.constvalue.ProcessFieldType;
import codedriver.framework.process.constvalue.ProcessFlowDirection;
import codedriver.framework.process.constvalue.ProcessStepMode;
import codedriver.framework.process.constvalue.ProcessStepType;
import codedriver.framework.process.constvalue.ProcessTaskAuditDetailType;
import codedriver.framework.process.constvalue.ProcessTaskGroupSearch;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessTaskStepAction;
import codedriver.framework.process.constvalue.ProcessTaskStepDataType;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dao.mapper.ChannelMapper;
import codedriver.framework.process.dao.mapper.FormMapper;
import codedriver.framework.process.dao.mapper.PriorityMapper;
import codedriver.framework.process.dao.mapper.ProcessMapper;
import codedriver.framework.process.dao.mapper.ProcessStepHandlerMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskStepDataMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskStepTimeAuditMapper;
import codedriver.framework.process.dao.mapper.WorktimeMapper;
import codedriver.framework.process.dto.ActionVo;
import codedriver.framework.process.dto.ChannelTypeVo;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.FormAttributeVo;
import codedriver.framework.process.dto.FormVersionVo;
import codedriver.framework.process.dto.PriorityVo;
import codedriver.framework.process.dto.ProcessStepHandlerVo;
import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskFileVo;
import codedriver.framework.process.dto.ProcessTaskFormAttributeDataVo;
import codedriver.framework.process.dto.ProcessTaskFormVo;
import codedriver.framework.process.dto.ProcessTaskSlaNotifyVo;
import codedriver.framework.process.dto.ProcessTaskSlaTimeVo;
import codedriver.framework.process.dto.ProcessTaskSlaTransferVo;
import codedriver.framework.process.dto.ProcessTaskSlaVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditDetailVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepCommentVo;
import codedriver.framework.process.dto.ProcessTaskStepContentVo;
import codedriver.framework.process.dto.ProcessTaskStepDataVo;
import codedriver.framework.process.dto.ProcessTaskStepFormAttributeVo;
import codedriver.framework.process.dto.ProcessTaskStepNotifyPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepTimeAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.dto.WorktimeRangeVo;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
import codedriver.framework.process.exception.process.ProcessStepHandlerNotFoundException;
import codedriver.framework.process.exception.processtask.ProcessTaskNoPermissionException;
import codedriver.framework.process.exception.processtask.ProcessTaskNotFoundException;
import codedriver.framework.process.exception.worktime.WorktimeNotFoundException;
import codedriver.framework.process.integration.handler.ProcessRequestFrom;
import codedriver.framework.process.notify.core.NotifyTriggerType;
import codedriver.framework.process.notify.schedule.plugin.ProcessTaskSlaNotifyJob;
import codedriver.framework.process.notify.schedule.plugin.ProcessTaskSlaTransferJob;
import codedriver.framework.scheduler.core.IJob;
import codedriver.framework.scheduler.core.SchedulerManager;
import codedriver.framework.scheduler.dto.JobObject;
import codedriver.framework.scheduler.exception.ScheduleHandlerNotFoundException;
import codedriver.framework.util.ConditionUtil;
import codedriver.framework.util.NotifyPolicyUtil;
import codedriver.framework.util.RunScriptUtil;

public abstract class ProcessStepHandlerUtilBase {
	static Logger logger = LoggerFactory.getLogger(ProcessStepHandlerUtilBase.class);

	private static final ThreadLocal<List<AuditHandler>> AUDIT_HANDLERS = new ThreadLocal<>();
	private static final ThreadLocal<List<SlaHandler>> SLA_HANDLERS = new ThreadLocal<>();
	private static final ThreadLocal<List<NotifyHandler>> NOTIFY_HANDLERS = new ThreadLocal<>();
	private static final ThreadLocal<List<ActionHandler>> ACTION_HANDLERS = new ThreadLocal<>();
	protected static ProcessMapper processMapper;
	protected static ProcessTaskMapper processTaskMapper;
	protected static FormMapper formMapper;
	protected static UserMapper userMapper;
	protected static ProcessTaskStepTimeAuditMapper processTaskStepTimeAuditMapper;
	private static WorktimeMapper worktimeMapper;
	protected static ChannelMapper channelMapper;
	protected static NotifyMapper notifyMapper;
	protected static FileMapper fileMapper;
	protected static TeamMapper teamMapper;
	protected static ProcessStepHandlerMapper processStepHandlerMapper;
	private static PriorityMapper priorityMapper;
	private static IntegrationMapper integrationMapper;
	protected static ProcessTaskStepDataMapper processTaskStepDataMapper;

	@Autowired
	public void setProcessTaskStepDataMapperr(ProcessTaskStepDataMapper _processTaskStepDataMapper) {
		processTaskStepDataMapper = _processTaskStepDataMapper;
	}

	@Autowired
	public void setProcessMapper(ProcessMapper _processMapper) {
		processMapper = _processMapper;
	}

	@Autowired
	public void setProcessTaskMapper(ProcessTaskMapper _processTaskMapper) {
		processTaskMapper = _processTaskMapper;
	}

	@Autowired
	public void setFormMapper(FormMapper _formMapper) {
		formMapper = _formMapper;
	}

	@Autowired
	public void setUserMapper(UserMapper _userMapper) {
		userMapper = _userMapper;
	}

	@Autowired
	public void setProcessTaskStepTimeAuditMapper(ProcessTaskStepTimeAuditMapper _processTaskStepTimeAuditMapper) {
		processTaskStepTimeAuditMapper = _processTaskStepTimeAuditMapper;
	}

	@Autowired
	public void setWorktimeMapper(WorktimeMapper _worktimeMapper) {
		worktimeMapper = _worktimeMapper;
	}

	@Autowired
	public void setChannelMapper(ChannelMapper _channelMapper) {
		channelMapper = _channelMapper;
	}

	@Autowired
	public void setNotifyMapper(NotifyMapper _notifyMapper) {
		notifyMapper = _notifyMapper;
	}

	@Autowired
	public void setFileMapper(FileMapper _fileMapper) {
		fileMapper = _fileMapper;
	}

	@Autowired
	public void setTeamMapper(TeamMapper _teamMapper) {
		teamMapper = _teamMapper;
	}

	@Autowired
	public void setProcessStepHandlerMapper(ProcessStepHandlerMapper _processStepHandlerMapper) {
		processStepHandlerMapper = _processStepHandlerMapper;
	}

	@Autowired
	public void setPriorityMapper(PriorityMapper _priorityMapper) {
		priorityMapper = _priorityMapper;
	}

	@Autowired
	public void setIntegrationMapper(IntegrationMapper _integrationMapper) {
		integrationMapper = _integrationMapper;
	}

	protected static class ActionHandler extends CodeDriverThread {

		private ProcessTaskStepVo currentProcessTaskStepVo;
		private NotifyTriggerType triggerType;

		public ActionHandler(ProcessTaskStepVo _currentProcessTaskStepVo, NotifyTriggerType _trigger) {
			currentProcessTaskStepVo = _currentProcessTaskStepVo;
			triggerType = _trigger;
			if (_currentProcessTaskStepVo != null) {
				this.setThreadName("PROCESSTASK-ACTION-" + _currentProcessTaskStepVo.getId());
			}
		}

		protected static void action(ProcessTaskStepVo currentProcessTaskStepVo, NotifyTriggerType trigger) {
			if (!TransactionSynchronizationManager.isSynchronizationActive()) {
				CachedThreadPool.execute(new ActionHandler(currentProcessTaskStepVo, trigger));
			} else {
				List<ActionHandler> handlerList = ACTION_HANDLERS.get();
				if (handlerList == null) {
					handlerList = new ArrayList<>();
					ACTION_HANDLERS.set(handlerList);
					TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
						@Override
						public void afterCommit() {
							List<ActionHandler> handlerList = ACTION_HANDLERS.get();
							for (ActionHandler handler : handlerList) {
								CachedThreadPool.execute(handler);
							}
						}

						@Override
						public void afterCompletion(int status) {
							ACTION_HANDLERS.remove();
						}
					});
				}
				handlerList.add(new ActionHandler(currentProcessTaskStepVo, trigger));
			}
		}

		@Override
		protected void execute() {
			try {
				/** 获取步骤配置信息 **/
				ProcessTaskStepVo stepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
				String stepConfig = processTaskMapper.getProcessTaskStepConfigByHash(stepVo.getConfigHash());
				stepVo.setConfig(stepConfig);
				ProcessStepHandlerVo processStepHandlerVo = processStepHandlerMapper.getProcessStepHandlerByHandler(stepVo.getHandler());
				if(processStepHandlerVo != null) {
					stepVo.setGlobalConfig(processStepHandlerVo.getConfig());					
				}
				/** 从步骤配置信息中获取动作列表 **/
				JSONArray actionList = stepVo.getActionList();
				if (CollectionUtils.isNotEmpty(actionList)) {
					for (int i = 0; i < actionList.size(); i++) {
						JSONObject actionObj = actionList.getJSONObject(i);
						if (triggerType.getTrigger().equals(actionObj.getString("trigger"))) {
							String integrationUuid = actionObj.getString("integrationUuid");
							IntegrationVo integrationVo = integrationMapper.getIntegrationByUuid(integrationUuid);
							if (integrationVo == null) {
								throw new IntegrationNotFoundException(integrationUuid);
							}
							IIntegrationHandler iIntegrationHandler = IntegrationHandlerFactory.getHandler(integrationVo.getHandler());
							if (iIntegrationHandler == null) {
								throw new IntegrationHandlerNotFoundException(integrationVo.getHandler());
							}
							/** 参数映射 **/
							List<ParamMappingVo> paramMappingList = JSON.parseArray(actionObj.getJSONArray("paramMappingList").toJSONString(), ParamMappingVo.class);
							if (CollectionUtils.isNotEmpty(paramMappingList)) {
								ProcessTaskVo processTaskVo = ProcessTaskHandlerUtil.getProcessTaskDetailInfoById(currentProcessTaskStepVo.getProcessTaskId());
								JSONObject processFieldData = ProcessTaskUtil.getProcessFieldData(processTaskVo, true);
								for (ParamMappingVo paramMappingVo : paramMappingList) {
									if (ProcessFieldType.CONSTANT.getValue().equals(paramMappingVo.getType())) {
										integrationVo.getParamObj().put(paramMappingVo.getName(), paramMappingVo.getValue());
									} else if (StringUtils.isNotBlank(paramMappingVo.getType())) {
										Object processFieldValue = processFieldData.get(paramMappingVo.getValue());
										if (processFieldValue != null) {
											integrationVo.getParamObj().put(paramMappingVo.getName(), processFieldValue);
										} else {
											logger.error("没有找到参数'" + paramMappingVo.getValue() + "'信息");
										}
									}
								}
							}
							boolean isSucceed = false;
							IntegrationResultVo integrationResultVo = iIntegrationHandler.sendRequest(integrationVo, ProcessRequestFrom.PROCESS);
							if (StringUtils.isNotBlank(integrationResultVo.getError())) {
								logger.error(integrationResultVo.getError());
								throw new IntegrationSendRequestException(integrationVo.getUuid());
							}
							JSONObject successConditionObj = actionObj.getJSONObject("successCondition");
							if (MapUtils.isNotEmpty(successConditionObj)) {
								String name = successConditionObj.getString("name");
								if (StringUtils.isNotBlank(name)) {
									String resultValue = null;
									String transformedResult = integrationResultVo.getTransformedResult();
									if (StringUtils.isNotBlank(transformedResult)) {
										JSONObject transformedResultObj = JSON.parseObject(transformedResult);
										if (MapUtils.isNotEmpty(transformedResultObj)) {
											resultValue = transformedResultObj.getString(name);
										}
									}
									if (resultValue == null) {
										String rawResult = integrationResultVo.getRawResult();
										if (StringUtils.isNotEmpty(rawResult)) {
											JSONObject rawResultObj = JSON.parseObject(rawResult);
											if (MapUtils.isNotEmpty(rawResultObj)) {
												resultValue = rawResultObj.getString(name);
											}
										}
									}
									if (resultValue != null) {
										List<String> curentValueList = new ArrayList<>();
										curentValueList.add(resultValue);
										String value = successConditionObj.getString("value");
										List<String> targetValueList = new ArrayList<>();
										targetValueList.add(value);
										String expression = successConditionObj.getString("expression");
										isSucceed = ConditionUtil.predicate(curentValueList, expression, targetValueList);
									}
								}
							} else {
								String statusCode = String.valueOf(integrationResultVo.getStatusCode());
								if (statusCode.startsWith("2") || statusCode.startsWith("3")) {
									isSucceed = true;
								}
							}
							ActionVo actionVo = new ActionVo();
							actionVo.setProcessTaskStepId(stepVo.getId());
							actionVo.setProcessTaskStepName(stepVo.getName());
							actionVo.setIntegrationUuid(integrationUuid);
							actionVo.setIntegrationName(integrationVo.getName());
							actionVo.setTrigger(triggerType.getTrigger());
							actionVo.setTriggerText(triggerType.getText());
							actionVo.setSucceed(isSucceed);
							JSONObject paramObj = new JSONObject();
							paramObj.put(ProcessTaskAuditDetailType.RESTFULACTION.getParamName(), JSON.toJSONString(actionVo));
							stepVo.setParamObj(paramObj);
							AuditHandler.audit(stepVo, ProcessTaskStepAction.RESTFULACTION);
						}
					}
				}
			} catch (Exception ex) {
				logger.error("动作执行失败：" + ex.getMessage(), ex);
			}
		}

	}

	protected static class NotifyHandler extends CodeDriverThread {
		private ProcessTaskStepVo currentProcessTaskStepVo;
		private NotifyTriggerType notifyTriggerType;

		public NotifyHandler(ProcessTaskStepVo _currentProcessTaskStepVo, NotifyTriggerType _trigger) {
			currentProcessTaskStepVo = _currentProcessTaskStepVo;
			notifyTriggerType = _trigger;
			if (_currentProcessTaskStepVo != null) {
				this.setThreadName("PROCESSTASK-NOTIFY-" + _currentProcessTaskStepVo.getId());
			}
		}

		protected static void notify(ProcessTaskStepVo currentProcessTaskStepVo, NotifyTriggerType trigger) {
			if (!TransactionSynchronizationManager.isSynchronizationActive()) {
				CachedThreadPool.execute(new NotifyHandler(currentProcessTaskStepVo, trigger));
			} else {
				List<NotifyHandler> handlerList = NOTIFY_HANDLERS.get();
				if (handlerList == null) {
					handlerList = new ArrayList<>();
					NOTIFY_HANDLERS.set(handlerList);
					TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
						@Override
						public void afterCommit() {
							List<NotifyHandler> handlerList = NOTIFY_HANDLERS.get();
							for (NotifyHandler handler : handlerList) {
								CachedThreadPool.execute(handler);
							}
						}

						@Override
						public void afterCompletion(int status) {
							NOTIFY_HANDLERS.remove();
						}
					});
				}
				handlerList.add(new NotifyHandler(currentProcessTaskStepVo, trigger));
			}
		}

		@Override
		protected void execute() {
			try {
				/** 获取步骤配置信息 **/
				ProcessTaskStepVo stepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
				String stepConfig = processTaskMapper.getProcessTaskStepConfigByHash(stepVo.getConfigHash());
				stepVo.setConfig(stepConfig);
				ProcessStepHandlerVo processStepHandlerVo = processStepHandlerMapper.getProcessStepHandlerByHandler(stepVo.getHandler());
				if(processStepHandlerVo != null) {
					stepVo.setGlobalConfig(processStepHandlerVo.getConfig());					
				}
				/** 从步骤配置信息中获取通知策略信息 **/
				JSONObject notifyPolicyConfig = stepVo.getNotifyPolicyConfig();
				if (MapUtils.isNotEmpty(notifyPolicyConfig)) {
					Long policyId = notifyPolicyConfig.getLong("policyId");
					/** 参数映射列表**/
					List<ParamMappingVo> paramMappingList = JSON.parseArray(notifyPolicyConfig.getJSONArray("paramMappingList").toJSONString(), ParamMappingVo.class);
					if (policyId != null) {
						JSONObject policyConfig = null;
						ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo = new ProcessTaskStepNotifyPolicyVo();
						processTaskStepNotifyPolicyVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
						processTaskStepNotifyPolicyVo.setPolicyId(policyId);
						processTaskStepNotifyPolicyVo = processTaskMapper.getProcessTaskStepNotifyPolicy(processTaskStepNotifyPolicyVo);
						if (processTaskStepNotifyPolicyVo != null) {
							policyConfig = JSON.parseObject(processTaskStepNotifyPolicyVo.getPolicyConfig());

						} else {
							NotifyPolicyVo notifyPolicyVo = notifyMapper.getNotifyPolicyById(policyId);
							if (notifyPolicyVo != null) {
								policyConfig = notifyPolicyVo.getConfig();
							}
						}
						if(MapUtils.isNotEmpty(policyConfig)) {
							ProcessTaskVo processTaskVo = ProcessTaskHandlerUtil.getProcessTaskDetailInfoById(currentProcessTaskStepVo.getProcessTaskId());
							JSONObject conditionParamData = ProcessTaskUtil.getProcessFieldData(processTaskVo, true);
							JSONObject templateParamData = ProcessTaskUtil.getProcessFieldData(processTaskVo, false);
							Map<String, List<NotifyReceiverVo>> receiverMap = new HashMap<>();
							ProcessTaskHandlerUtil.getReceiverMap(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), receiverMap);
							NotifyPolicyUtil.execute(policyConfig, paramMappingList, notifyTriggerType, templateParamData, conditionParamData, receiverMap);						
						}
					}
				}
			} catch (Exception ex) {
				logger.error("通知失败：" + ex.getMessage(), ex);
			}
		}

	}

	protected static class SlaHandler extends CodeDriverThread {
		private ProcessTaskStepVo currentProcessTaskStepVo;

		public SlaHandler(ProcessTaskStepVo _currentProcessTaskStepVo) {
			currentProcessTaskStepVo = _currentProcessTaskStepVo;
			if (_currentProcessTaskStepVo != null) {
				this.setThreadName("PROCESSTASK-SLA-" + _currentProcessTaskStepVo.getId());
			}
		}

		private static long calculateExpireTime(long activeTime, long timeLimit, String worktimeUuid) {
			if (worktimeMapper.checkWorktimeIsExists(worktimeUuid) == 0) {
				throw new WorktimeNotFoundException(worktimeUuid);
			}
			if (timeLimit <= 0) {
				return activeTime;
			}
			WorktimeRangeVo worktimeRangeVo = new WorktimeRangeVo();
			WorktimeRangeVo recentWorktimeRange = null;
			long startTime = 0;
			long endTime = 0;
			long duration = 0;
			while (true) {
				worktimeRangeVo.setWorktimeUuid(worktimeUuid);
				worktimeRangeVo.setStartTime(activeTime);
				recentWorktimeRange = worktimeMapper.getRecentWorktimeRange(worktimeRangeVo);
				if (recentWorktimeRange == null) {
					return activeTime;
				}
				startTime = recentWorktimeRange.getStartTime();
				endTime = recentWorktimeRange.getEndTime();
				if (startTime > activeTime) {
					activeTime = startTime;
				}
				duration = endTime - activeTime;
				if (duration >= timeLimit) {
					return activeTime + timeLimit;
				} else {
					timeLimit -= duration;
					activeTime = endTime;
				}
			}
		}

		private static long getTimeCost(List<ProcessTaskStepTimeAuditVo> processTaskStepTimeAuditList, String worktimeUuid) {
			List<Map<String, Long>> timeList = new ArrayList<>();
			for (ProcessTaskStepTimeAuditVo auditVo : processTaskStepTimeAuditList) {
				Long startTime = null, endTime = null;
				if (auditVo.getActiveTimeLong() != null) {
					startTime = auditVo.getActiveTimeLong();
				}
				if (auditVo.getCompleteTimeLong() != null) {
					endTime = auditVo.getCompleteTimeLong();
				} else if (auditVo.getAbortTimeLong() != null) {
					endTime = auditVo.getAbortTimeLong();
				} else if (auditVo.getBackTimeLong() != null) {
					endTime = auditVo.getBackTimeLong();
				}
				if (startTime != null && endTime != null) {
					Map<String, Long> stimeMap = new HashMap<>();
					stimeMap.put("s", startTime);
					timeList.add(stimeMap);
					Map<String, Long> etimeMap = new HashMap<>();
					etimeMap.put("e", endTime);
					timeList.add(etimeMap);
				}
			}
			timeList.sort(new Comparator<Map<String, Long>>() {
				@Override
				public int compare(Map<String, Long> o1, Map<String, Long> o2) {
					Long t1 = null, t2 = null;
					if (o1.containsKey("s")) {
						t1 = o1.get("s");
					} else if (o1.containsKey("e")) {
						t1 = o1.get("e");
					}

					if (o2.containsKey("s")) {
						t2 = o2.get("s");
					} else if (o2.containsKey("e")) {
						t2 = o2.get("e");
					}
					return t1.compareTo(t2);
				}
			});
			Stack<Long> timeStack = new Stack<>();
			List<Map<String, Long>> newTimeList = new ArrayList<>();
			for (Map<String, Long> timeMap : timeList) {
				if (timeMap.containsKey("s")) {
					timeStack.push(timeMap.get("s"));
				} else if (timeMap.containsKey("e")) {
					if (!timeStack.isEmpty()) {
						Long currentStartTimeLong = timeStack.pop();
						if (timeStack.isEmpty()) {// 栈被清空时计算时间段
							Map<String, Long> newTimeMap = new HashMap<>();
							newTimeMap.put("s", currentStartTimeLong);
							newTimeMap.put("e", timeMap.get("e"));
							newTimeList.add(newTimeMap);
						}
					}
				}
			}

			long sum = 0;
			for (Map<String, Long> timeMap : newTimeList) {
				sum += worktimeMapper.calculateCostTime(worktimeUuid, timeMap.get("s"), timeMap.get("e"));
			}
			return sum;
		}

		private static long getRealTimeCost(List<ProcessTaskStepTimeAuditVo> processTaskStepTimeAuditList) {
			int timeCost = 0;
			if (processTaskStepTimeAuditList != null && processTaskStepTimeAuditList.size() > 0) {
				List<Map<String, Long>> timeZoneList = new ArrayList<>();
				for (ProcessTaskStepTimeAuditVo auditVo : processTaskStepTimeAuditList) {
					Long startTime = null, endTime = null;
					if (auditVo.getActiveTimeLong() != null) {
						startTime = auditVo.getActiveTimeLong();
					}
					if (auditVo.getCompleteTimeLong() != null) {
						endTime = auditVo.getCompleteTimeLong();
					} else if (auditVo.getAbortTimeLong() != null) {
						endTime = auditVo.getAbortTimeLong();
					} else if (auditVo.getBackTimeLong() != null) {
						endTime = auditVo.getBackTimeLong();
					}
					if (startTime != null && endTime != null) {
						Map<String, Long> smap = new HashMap<>();
						smap.put("s", startTime);
						timeZoneList.add(smap);
						Map<String, Long> emap = new HashMap<>();
						emap.put("e", endTime);
						timeZoneList.add(emap);
					}
				}
				timeZoneList.sort(new Comparator<Map<String, Long>>() {
					@Override
					public int compare(Map<String, Long> o1, Map<String, Long> o2) {
						Long t1 = null, t2 = null;
						if (o1.containsKey("s")) {
							t1 = o1.get("s");
						} else if (o1.containsKey("e")) {
							t1 = o1.get("e");
						}

						if (o2.containsKey("s")) {
							t2 = o2.get("s");
						} else if (o2.containsKey("e")) {
							t2 = o2.get("e");
						}
						return t1.compareTo(t2);
					}
				});

				Stack<Long> timeStack = new Stack<>();
				for (Map<String, Long> timeMap : timeZoneList) {
					if (timeMap.containsKey("s")) {
						timeStack.push(timeMap.get("s"));
					} else if (timeMap.containsKey("e")) {
						if (!timeStack.isEmpty()) {
							Long currentStartTimeLong = timeStack.pop();
							if (timeStack.isEmpty()) {
								Long tmp = timeMap.get("e") - currentStartTimeLong;
								timeCost += tmp.intValue();
							}
						}
					}
				}
			}
			return timeCost;
		}

		protected static void calculate(ProcessTaskStepVo currentProcessTaskStepVo) {
			if (!TransactionSynchronizationManager.isSynchronizationActive()) {
				CachedThreadPool.execute(new SlaHandler(currentProcessTaskStepVo));
			} else {
				List<SlaHandler> handlerList = SLA_HANDLERS.get();
				if (handlerList == null) {
					handlerList = new ArrayList<>();
					SLA_HANDLERS.set(handlerList);
					TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
						@Override
						public void afterCommit() {
							List<SlaHandler> handlerList = SLA_HANDLERS.get();
							for (SlaHandler handler : handlerList) {
								CachedThreadPool.execute(handler);
							}
						}

						@Override
						public void afterCompletion(int status) {
							SLA_HANDLERS.remove();
						}
					});
				}
				handlerList.add(new SlaHandler(currentProcessTaskStepVo));
			}
		}

		private static long getRealtime(int time, String unit) {
			if ("hour".equals(unit)) {
				return time * 60 * 60 * 1000;
			} else if ("day".equals(unit)) {
				return time * 24 * 60 * 60 * 1000;
			} else {
				return time * 60 * 1000;
			}
		}

		@Override
		protected void execute() {
			List<ProcessTaskSlaVo> slaList = processTaskMapper.getProcessTaskSlaByProcessTaskStepId(currentProcessTaskStepVo.getId());
			if (slaList != null && slaList.size() > 0) {
				long now = System.currentTimeMillis();
				ProcessTaskVo processTaskVo = ProcessTaskHandlerUtil.getProcessTaskDetailInfoById(currentProcessTaskStepVo.getProcessTaskId());
				String worktimeUuid = processTaskVo.getWorktimeUuid();
				for (ProcessTaskSlaVo slaVo : slaList) {
					/** 如果没有超时时间，证明第一次进入SLA标签范围，开始计算超时时间 **/
					ProcessTaskSlaTimeVo slaTimeVo = slaVo.getSlaTimeVo();
					boolean isSlaTimeExists = false;
					if (slaTimeVo == null) {
						if (slaVo.getConfigObj() != null) {
							JSONArray policyList = slaVo.getConfigObj().getJSONArray("calculatePolicyList");
							if (CollectionUtils.isNotEmpty(policyList)) {
								POLICY: for (int i = 0; i < policyList.size(); i++) {
									JSONObject policyObj = policyList.getJSONObject(i);
									int enablePriority = policyObj.getIntValue("enablePriority");
									int time = policyObj.getIntValue("time");
									String unit = policyObj.getString("unit");
									JSONArray priorityList = policyObj.getJSONArray("priorityList");
									JSONArray conditionGroupList = policyObj.getJSONArray("conditionGroupList");
									/** 如果没有规则，则默认生效，如果有规则，以规则计算结果判断是否生效 **/
									boolean isHit = true;
									if (CollectionUtils.isNotEmpty(conditionGroupList)) {
										try {
										JSONObject conditionParamData = ProcessTaskUtil.getProcessFieldData(processTaskVo, true);
										ConditionParamContext.init(conditionParamData);
										ConditionConfigVo conditionConfigVo = new ConditionConfigVo(policyObj);
										String script = conditionConfigVo.buildScript();
										// ((false || true) || (true && false) || (true || false))
										isHit = RunScriptUtil.runScript(script); 
										}catch(Exception e) {
											logger.error(e.getMessage(), e);
										}finally {
											ConditionParamContext.get().release();											
										}
									}
									if (isHit) {
										slaTimeVo = new ProcessTaskSlaTimeVo();
										if (enablePriority == 0) {
											long timecost = getRealtime(time, unit);
											slaTimeVo.setTimeSum(timecost);
											slaTimeVo.setRealTimeLeft(timecost);
											slaTimeVo.setTimeLeft(timecost);
										} else {//关联优先级
											if (CollectionUtils.isNotEmpty(priorityList)) {
												for (int p = 0; p < priorityList.size(); p++) {
													JSONObject priorityObj = priorityList.getJSONObject(p);
													if (priorityObj.getString("priorityUuid").equals(processTaskVo.getPriorityUuid())) {
														long timecost = getRealtime(priorityObj.getIntValue("time"), priorityObj.getString("unit"));
														slaTimeVo.setTimeSum(timecost);
														slaTimeVo.setRealTimeLeft(timecost);
														slaTimeVo.setTimeLeft(timecost);
														break POLICY;
													}
												}
											}
										}
										break;
									}
								}
							}
						}
					} else {
						isSlaTimeExists = true;
						// 非第一次进入，进行时间扣减
						List<ProcessTaskStepTimeAuditVo> processTaskStepTimeAuditList = processTaskStepTimeAuditMapper.getProcessTaskStepTimeAuditBySlaId(slaVo.getId());
						long realTimeCost = getRealTimeCost(processTaskStepTimeAuditList);
						long timeCost = realTimeCost;
						if (StringUtils.isNotBlank(worktimeUuid)) {// 如果有工作时间，则计算实际消耗的工作时间
							timeCost = getTimeCost(processTaskStepTimeAuditList, worktimeUuid);
						}
						slaTimeVo.setRealTimeLeft(slaTimeVo.getRealTimeLeft() - realTimeCost);
						slaTimeVo.setTimeLeft(slaTimeVo.getTimeLeft() - timeCost);

					}

					// 修正最终超时日期
					if (slaTimeVo != null) {
						slaTimeVo.setRealExpireTime(new Date(now + slaTimeVo.getRealTimeLeft()));
						if (StringUtils.isNotBlank(worktimeUuid)) {
							if (slaTimeVo.getTimeLeft() != null) {
								long expireTime = calculateExpireTime(now, slaTimeVo.getTimeLeft(), worktimeUuid);
								slaTimeVo.setExpireTime(new Date(expireTime));
							} else {
								throw new RuntimeException("计算剩余时间失败");
							}
						} else {
							if (slaTimeVo.getTimeLeft() != null) {
								slaTimeVo.setExpireTime(new Date(now + slaTimeVo.getTimeLeft()));
							} else {
								throw new RuntimeException("计算剩余时间失败");
							}
						}
						slaTimeVo.setSlaId(slaVo.getId());
						if (isSlaTimeExists) {
							processTaskMapper.updateProcessTaskSlaTime(slaTimeVo);
						} else {
							processTaskMapper.insertProcessTaskSlaTime(slaTimeVo);
						}
						/** 第一次进入时需要加载定时作业 **/
						if (!isSlaTimeExists && slaTimeVo.getExpireTime() != null && slaVo.getConfigObj() != null) {
							// 加载定时作业，执行超时通知操作
							JSONArray notifyPolicyList = slaVo.getConfigObj().getJSONArray("notifyPolicyList");
							if (CollectionUtils.isNotEmpty(notifyPolicyList)) {
								for (int i = 0; i < notifyPolicyList.size(); i++) {
									JSONObject notifyPolicyObj = notifyPolicyList.getJSONObject(i);
									ProcessTaskSlaNotifyVo processTaskSlaNotifyVo = new ProcessTaskSlaNotifyVo();
									processTaskSlaNotifyVo.setSlaId(slaVo.getId());
									processTaskSlaNotifyVo.setConfig(notifyPolicyObj.toJSONString());
									// 需要发通知时写入数据，执行完毕后清除
									processTaskMapper.insertProcessTaskSlaNotify(processTaskSlaNotifyVo);
									IJob jobHandler = SchedulerManager.getHandler(ProcessTaskSlaNotifyJob.class.getName());
									if (jobHandler != null) {
										JobObject.Builder jobObjectBuilder = new JobObject.Builder(processTaskSlaNotifyVo.getId().toString(), jobHandler.getGroupName(), jobHandler.getClassName(), TenantContext.get().getTenantUuid()).addData("slaNotifyId", processTaskSlaNotifyVo.getId());
										JobObject jobObject = jobObjectBuilder.build();
										jobHandler.reloadJob(jobObject);
									} else {
										throw new ScheduleHandlerNotFoundException(ProcessTaskSlaNotifyJob.class.getName());
									}

								}
							}
							// 加载定时作业，执行超时转交操作
							JSONArray transferPolicyList = slaVo.getConfigObj().getJSONArray("transferPolicyList");
							if (CollectionUtils.isNotEmpty(transferPolicyList)) {
								for (int i = 0; i < transferPolicyList.size(); i++) {
									JSONObject transferPolicyObj = transferPolicyList.getJSONObject(i);
									ProcessTaskSlaTransferVo processTaskSlaTransferVo = new ProcessTaskSlaTransferVo();
									processTaskSlaTransferVo.setSlaId(slaVo.getId());
									processTaskSlaTransferVo.setConfig(transferPolicyObj.toJSONString());
									// 需要转交时写入数据，执行完毕后清除
									processTaskMapper.insertProcessTaskSlaTransfer(processTaskSlaTransferVo);
									IJob jobHandler = SchedulerManager.getHandler(ProcessTaskSlaTransferJob.class.getName());
									if (jobHandler != null) {
										JobObject.Builder jobObjectBuilder = new JobObject.Builder(processTaskSlaTransferVo.getId().toString(), jobHandler.getGroupName(), jobHandler.getClassName(), TenantContext.get().getTenantUuid()).addData("slaTransferId", processTaskSlaTransferVo.getId());
										JobObject jobObject = jobObjectBuilder.build();
										jobHandler.reloadJob(jobObject);
									} else {
										throw new ScheduleHandlerNotFoundException(ProcessTaskSlaTransferVo.class.getName());
									}

								}
							}
						}
					}
				}
			}
		}
	}

	protected static class TimeAuditHandler {
		@SuppressWarnings("incomplete-switch")
		protected static void audit(ProcessTaskStepVo currentProcessTaskStepVo, ProcessTaskStepAction action) {
			ProcessTaskStepTimeAuditVo processTaskStepTimeAuditVo = processTaskStepTimeAuditMapper.getLastProcessTaskStepTimeAuditByStepId(currentProcessTaskStepVo.getId());
			ProcessTaskStepTimeAuditVo newAuditVo = new ProcessTaskStepTimeAuditVo();
			newAuditVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
			switch (action) {
			case ACTIVE:
				newAuditVo.setActiveTime("now");
				if (processTaskStepTimeAuditVo == null || StringUtils.isNotBlank(processTaskStepTimeAuditVo.getActiveTime())) {
					processTaskStepTimeAuditMapper.insertProcessTaskStepTimeAudit(newAuditVo);
				}
				break;
			case START:
				newAuditVo.setStartTime("now");
				if (processTaskStepTimeAuditVo == null || StringUtils.isNotBlank(processTaskStepTimeAuditVo.getStartTime())) {
					processTaskStepTimeAuditMapper.insertProcessTaskStepTimeAudit(newAuditVo);
				} else if (StringUtils.isBlank(processTaskStepTimeAuditVo.getStartTime())) {// 如果starttime为空，则更新starttime
					newAuditVo.setId(processTaskStepTimeAuditVo.getId());
					processTaskStepTimeAuditMapper.updateProcessTaskStepTimeAudit(newAuditVo);
				}
				break;
			case COMPLETE:
				/** 如果找不到审计记录并且completetime不为空，则新建审计记录 **/
				newAuditVo.setCompleteTime("now");
				if (processTaskStepTimeAuditVo == null || StringUtils.isNotBlank(processTaskStepTimeAuditVo.getCompleteTime())) {
					processTaskStepTimeAuditMapper.insertProcessTaskStepTimeAudit(newAuditVo);
				} else if (StringUtils.isBlank(processTaskStepTimeAuditVo.getCompleteTime())) {// 如果completetime为空，则更新completetime
					newAuditVo.setId(processTaskStepTimeAuditVo.getId());
					processTaskStepTimeAuditMapper.updateProcessTaskStepTimeAudit(newAuditVo);
				}
				break;
			case ABORT:
				/** 如果找不到审计记录并且aborttime不为空，则新建审计记录 **/
				newAuditVo.setAbortTime("now");
				if (processTaskStepTimeAuditVo == null || StringUtils.isNotBlank(processTaskStepTimeAuditVo.getAbortTime())) {
					processTaskStepTimeAuditMapper.insertProcessTaskStepTimeAudit(newAuditVo);
				} else if (StringUtils.isBlank(processTaskStepTimeAuditVo.getAbortTime())) {// 如果aborttime为空，则更新aborttime
					newAuditVo.setId(processTaskStepTimeAuditVo.getId());
					processTaskStepTimeAuditMapper.updateProcessTaskStepTimeAudit(newAuditVo);
				}
				break;
			case BACK:
				/** 如果找不到审计记录并且backtime不为空，则新建审计记录 **/
				newAuditVo.setBackTime("now");
				if (processTaskStepTimeAuditVo == null || StringUtils.isNotBlank(processTaskStepTimeAuditVo.getBackTime())) {
					processTaskStepTimeAuditMapper.insertProcessTaskStepTimeAudit(newAuditVo);
				} else if (StringUtils.isBlank(processTaskStepTimeAuditVo.getBackTime())) {// 如果backtime为空，则更新backtime
					newAuditVo.setId(processTaskStepTimeAuditVo.getId());
					processTaskStepTimeAuditMapper.updateProcessTaskStepTimeAudit(newAuditVo);
				}
				break;
			case RECOVER:
				if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.PENDING.getValue())) {
					newAuditVo.setActiveTime("now");
					if (processTaskStepTimeAuditVo == null || StringUtils.isNotBlank(processTaskStepTimeAuditVo.getActiveTime())) {
						processTaskStepTimeAuditMapper.insertProcessTaskStepTimeAudit(newAuditVo);
					}
				} else if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
					newAuditVo.setStartTime("now");
					if (processTaskStepTimeAuditVo == null || StringUtils.isNotBlank(processTaskStepTimeAuditVo.getStartTime())) {
						processTaskStepTimeAuditMapper.insertProcessTaskStepTimeAudit(newAuditVo);
					} else if (StringUtils.isBlank(processTaskStepTimeAuditVo.getStartTime())) {// 如果starttime为空，则更新starttime
						newAuditVo.setId(processTaskStepTimeAuditVo.getId());
						processTaskStepTimeAuditMapper.updateProcessTaskStepTimeAudit(newAuditVo);
					}
				}
				break;
			}
		}
	}

	protected static class ActionRoleChecker {

		protected static boolean verifyActionAuthoriy(Long processTaskId, ProcessTaskStepAction action) {
			return verifyActionAuthoriy(processTaskId, null, action);
		}

		protected static boolean verifyActionAuthoriy(Long processTaskId, Long processTaskStepId, ProcessTaskStepAction action) {
			List<String> verifyActionList = new ArrayList<>();
			verifyActionList.add(action.getValue());
			// TODO 临时跳过 作业无用户，后续再修改
			if (UserContext.get().getUserUuid() != null) {
				List<String> actionList = getProcessTaskStepActionList(processTaskId, processTaskStepId, verifyActionList);
				if (!actionList.contains(action.getValue())) {
					throw new ProcessTaskNoPermissionException(action.getText());
				}
			}
			return true;
		}

		protected static List<String> getProcessTaskStepActionList(Long processTaskId, Long processTaskStepId) {
			return getProcessTaskStepActionList(processTaskId, processTaskStepId, new ArrayList<>());
		}

		/**
		 * 
		 * @Time:2020年4月3日
		 * @Description: 获取当前用户对当前步骤的部分操作权限列表（verifyActionList包含的那部分）
		 * @param processTaskId
		 * @param processTaskStepId
		 * @param verifyActionList
		 * @return List<String>
		 */
		protected static List<String> getProcessTaskStepActionList(Long processTaskId, Long processTaskStepId, List<String> verifyActionList) {
			//系统用户拥有所有权限
			if(SystemUser.SYSTEM.getUserUuid().equals(UserContext.get().getUserUuid())) {
				return verifyActionList;
			}
			Set<String> resultList = new HashSet<>();
			ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskById(processTaskId);
			// 工单信息查看权限，有本工单对应服务的上报权限或者工单干系人，才有该工单信息查看权限;
			if (CollectionUtils.isEmpty(verifyActionList) || verifyActionList.contains(ProcessTaskStepAction.POCESSTASKVIEW.getValue())) {
				if (UserContext.get().getUserUuid(true).equals(processTaskVo.getOwner())) {
					resultList.add(ProcessTaskStepAction.POCESSTASKVIEW.getValue());
				} else if (UserContext.get().getUserUuid(true).equals(processTaskVo.getReporter())) {
					resultList.add(ProcessTaskStepAction.POCESSTASKVIEW.getValue());
				} else {
					List<String> currentUserTeamList = teamMapper.getTeamUuidListByUserUuid(UserContext.get().getUserUuid(true));
					List<String> channelList = channelMapper.getAuthorizedChannelUuidList(UserContext.get().getUserUuid(true), currentUserTeamList, UserContext.get().getRoleUuidList(), processTaskVo.getChannelUuid());
					if (channelList.contains(processTaskVo.getChannelUuid())) {
						resultList.add(ProcessTaskStepAction.POCESSTASKVIEW.getValue());
					} else if (processTaskMapper.checkIsWorker(processTaskId, null, UserContext.get().getUserUuid(true), currentUserTeamList, UserContext.get().getRoleUuidList()) > 0) {
						resultList.add(ProcessTaskStepAction.POCESSTASKVIEW.getValue());
					} else if (processTaskMapper.checkIsProcessTaskStepUser(processTaskId, null, UserContext.get().getUserUuid(true)) > 0) {
						resultList.add(ProcessTaskStepAction.POCESSTASKVIEW.getValue());
					}
				}
			}
			// 上报提交
			if (CollectionUtils.isEmpty(verifyActionList) || verifyActionList.contains(ProcessTaskStepAction.STARTPROCESS.getValue())) {
				if (ProcessTaskStatus.DRAFT.getValue().equals(processTaskVo.getStatus())) {
					if (UserContext.get().getUserUuid(true).equals(processTaskVo.getOwner()) || UserContext.get().getUserUuid(true).equals(processTaskVo.getReporter())) {
						resultList.add(ProcessTaskStepAction.STARTPROCESS.getValue());
					}
				}
			}
			if (processTaskStepId != null) {
				// 获取步骤信息
				ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepId);
				if (processTaskStepVo != null) {
					List<String> currentUserProcessUserTypeList = new ArrayList<>();
					List<String> actionList = new ArrayList<>();
					actionList.add(ProcessTaskStepAction.VIEW.getValue());
					actionList.add(ProcessTaskStepAction.TRANSFER.getValue());
					if (CollectionUtils.isEmpty(verifyActionList) || actionList.removeAll(verifyActionList)) {
						actionList = new ArrayList<>();
						actionList.add(ProcessTaskStepAction.VIEW.getValue());
						actionList.add(ProcessTaskStepAction.TRANSFER.getValue());
						if (CollectionUtils.isEmpty(currentUserProcessUserTypeList)) {
							currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStepId);
						}
						List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStepVo, actionList, currentUserProcessUserTypeList);
						// 根据流程设置和步骤状态判断当前用户权限
						for (String action : configActionList) {
							if (ProcessTaskStepAction.VIEW.getValue().equals(action)) {
								resultList.add(action);
							} else if (ProcessTaskStepAction.TRANSFER.getValue().equals(action)) {
								// 步骤状态为已激活的才能转交
								if (processTaskStepVo.getIsActive() == 1) {
									resultList.add(action);
								}
							}
						}
						if (UserContext.get().getUserUuid(true).equals(processTaskVo.getOwner())) {
							resultList.add(ProcessTaskStepAction.VIEW.getValue());
						} else if (UserContext.get().getUserUuid(true).equals(processTaskVo.getReporter())) {
							resultList.add(ProcessTaskStepAction.VIEW.getValue());
						} else if (processTaskMapper.checkIsProcessTaskStepUser(processTaskId, processTaskStepId, UserContext.get().getUserUuid(true)) > 0) {
							resultList.add(ProcessTaskStepAction.VIEW.getValue());
						}
					}

					actionList = new ArrayList<>();
					actionList.add(ProcessTaskStepAction.COMPLETE.getValue());
					actionList.add(ProcessTaskStepAction.SAVE.getValue());
					actionList.add(ProcessTaskStepAction.COMMENT.getValue());
					actionList.add(ProcessTaskStepAction.CREATESUBTASK.getValue());
					actionList.add(ProcessTaskStepAction.ACCEPT.getValue());
					actionList.add(ProcessTaskStepAction.START.getValue());
					actionList.add(ProcessTaskStepAction.BACK.getValue());
					if (CollectionUtils.isEmpty(verifyActionList) || actionList.removeAll(verifyActionList)) {
						if (processTaskStepVo.getIsActive() == 1) {
							if (CollectionUtils.isEmpty(currentUserProcessUserTypeList)) {
								currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStepId);
							}
							if (currentUserProcessUserTypeList.contains(ProcessUserType.WORKER.getValue())) {
								if (ProcessTaskStatus.RUNNING.getValue().equals(processTaskStepVo.getStatus()) || ProcessTaskStatus.DRAFT.getValue().equals(processTaskStepVo.getStatus())) {
									// 完成complete 暂存save 评论comment 创建子任务createsubtask
									if (currentUserProcessUserTypeList.contains(ProcessUserType.MAJOR.getValue()) || currentUserProcessUserTypeList.contains(ProcessUserType.AGENT.getValue())) {
										List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getToProcessTaskStepByFromIdAndType(processTaskStepId,null);
										for (ProcessTaskStepVo processTaskStep : processTaskStepList) {
											if (processTaskStep.getIsActive() != null) {
												if (ProcessFlowDirection.FORWARD.getValue().equals(processTaskStep.getFlowDirection())) {
													resultList.add(ProcessTaskStepAction.COMPLETE.getValue());
												} else if (ProcessFlowDirection.BACKWARD.getValue().equals(processTaskStep.getFlowDirection()) && processTaskStep.getIsActive().intValue() != 0) {
													resultList.add(ProcessTaskStepAction.BACK.getValue());
												}
											}
										}
										resultList.add(ProcessTaskStepAction.SAVE.getValue());
										resultList.add(ProcessTaskStepAction.COMMENT.getValue());
										resultList.add(ProcessTaskStepAction.CREATESUBTASK.getValue());
									}
								} else if (ProcessTaskStatus.PENDING.getValue().equals(processTaskStepVo.getStatus())) {// 已激活未处理
									if (currentUserProcessUserTypeList.contains(ProcessUserType.MAJOR.getValue())) {
										resultList.add(ProcessTaskStepAction.START.getValue());
									} else {// 没有主处理人时是accept
										resultList.add(ProcessTaskStepAction.ACCEPT.getValue());
									}
								}
							}
						}
					}
				}
			}

			List<String> actionList = new ArrayList<>();
			actionList.add(ProcessTaskStepAction.ABORT.getValue());
			actionList.add(ProcessTaskStepAction.RECOVER.getValue());
			actionList.add(ProcessTaskStepAction.UPDATE.getValue());
			actionList.add(ProcessTaskStepAction.URGE.getValue());
			if (CollectionUtils.isEmpty(verifyActionList) || actionList.removeAll(verifyActionList)) {
				// 终止/恢复流程abort、修改上报内容update取工单当前所有正在处理的节点权限配置的并集
				List<ProcessTaskStepVo> startProcessTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.START.getValue());
				List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.PROCESS.getValue());
				processTaskStepList.addAll(startProcessTaskStepList);
				for (ProcessTaskStepVo processTaskStep : processTaskStepList) {
					if (processTaskStep.getIsActive().intValue() == 1) {
						List<String> currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStep.getId());
						actionList = new ArrayList<>();
						actionList.add(ProcessTaskStepAction.ABORT.getValue());
						actionList.add(ProcessTaskStepAction.UPDATE.getValue());
						actionList.add(ProcessTaskStepAction.URGE.getValue());
						List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStep, actionList, currentUserProcessUserTypeList);
						// 根据流程设置和步骤状态判断当前用户权限
						for (String action : configActionList) {
							if (ProcessTaskStepAction.ABORT.getValue().equals(action)) {
								// 工单状态为进行中的才能终止
								if (ProcessTaskStatus.RUNNING.getValue().equals(processTaskVo.getStatus())) {
									resultList.add(action);
								}
							} else if (ProcessTaskStepAction.UPDATE.getValue().equals(action)) {
								resultList.add(action);
							} else if (ProcessTaskStepAction.URGE.getValue().equals(action)) {
								resultList.add(action);
							}
						}
					} else if (processTaskStep.getIsActive().intValue() == -1) {
						List<String> currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStep.getId());
						actionList = new ArrayList<>();
						actionList.add(ProcessTaskStepAction.ABORT.getValue());
						List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStep, actionList, currentUserProcessUserTypeList);
						if (configActionList.contains(ProcessTaskStepAction.ABORT.getValue())) {
							configActionList.add(ProcessTaskStepAction.RECOVER.getValue());
						}
						// 根据流程设置和步骤状态判断当前用户权限
						for (String action : configActionList) {
							if (ProcessTaskStepAction.RECOVER.getValue().equals(action)) {
								// 工单状态为已终止的才能恢复
								if (ProcessTaskStatus.ABORTED.getValue().equals(processTaskVo.getStatus())) {
									resultList.add(action);
								}
							}
						}
					}
				}
			}

			if (CollectionUtils.isEmpty(verifyActionList) || verifyActionList.contains(ProcessTaskStepAction.WORK.getValue())) {
				List<String> currentUserTeamList = teamMapper.getTeamUuidListByUserUuid(UserContext.get().getUserUuid(true));
				// 有可处理步骤work
				if (processTaskMapper.checkIsWorker(processTaskId, null, UserContext.get().getUserUuid(true), currentUserTeamList, UserContext.get().getRoleUuidList()) > 0) {
					resultList.add(ProcessTaskStepAction.WORK.getValue());
				}
			}

			if (CollectionUtils.isEmpty(verifyActionList) || verifyActionList.contains(ProcessTaskStepAction.RETREAT.getValue())) {
				// 撤销权限retreat
				Set<ProcessTaskStepVo> retractableStepSet = getRetractableStepListByProcessTaskId(processTaskId);
				if (CollectionUtils.isNotEmpty(retractableStepSet)) {
					if (processTaskStepId != null && verifyActionList.contains(ProcessTaskStepAction.RETREAT.getValue())) {
						for (ProcessTaskStepVo processTaskStepVo : retractableStepSet) {
							if (processTaskStepId.equals(processTaskStepVo.getId())) {
								resultList.add(ProcessTaskStepAction.RETREAT.getValue());
							}
						}
					} else {
						resultList.add(ProcessTaskStepAction.RETREAT.getValue());
					}
				}
			}

			return new ArrayList<String>(resultList);
		}

		/**
		 * 
		 * @Time:2020年4月3日
		 * @Description: 获取当前用户在当前步骤中工单干系人列表
		 * @param processTaskVo     工单信息
		 * @param processTaskStepId 步骤id
		 * @return List<String>
		 */
		private static List<String> getCurrentUserProcessUserTypeList(ProcessTaskVo processTaskVo, Long processTaskStepId) {
			List<String> currentUserProcessUserTypeList = new ArrayList<>();
			currentUserProcessUserTypeList.add(UserType.ALL.getValue());
			if (UserContext.get().getUserUuid(true).equals(processTaskVo.getOwner())) {
				currentUserProcessUserTypeList.add(ProcessUserType.OWNER.getValue());
			}
			if (UserContext.get().getUserUuid(true).equals(processTaskVo.getReporter())) {
				currentUserProcessUserTypeList.add(ProcessUserType.REPORTER.getValue());
			}
			List<ProcessTaskStepUserVo> majorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.MAJOR.getValue());
			List<String> majorUserUuidList = majorUserList.stream().map(ProcessTaskStepUserVo::getUserUuid).collect(Collectors.toList());
			if (majorUserUuidList.contains(UserContext.get().getUserUuid(true))) {
				currentUserProcessUserTypeList.add(ProcessUserType.MAJOR.getValue());
			}
			List<ProcessTaskStepUserVo> minorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.MINOR.getValue());
			List<String> minorUserUuidList = minorUserList.stream().map(ProcessTaskStepUserVo::getUserUuid).collect(Collectors.toList());
			if (minorUserUuidList.contains(UserContext.get().getUserUuid(true))) {
				currentUserProcessUserTypeList.add(ProcessUserType.MINOR.getValue());
			}
			List<ProcessTaskStepUserVo> agentUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.AGENT.getValue());
			List<String> agentUserUuidList = agentUserList.stream().map(ProcessTaskStepUserVo::getUserUuid).collect(Collectors.toList());
			if (agentUserUuidList.contains(UserContext.get().getUserUuid(true))) {
				currentUserProcessUserTypeList.add(ProcessUserType.AGENT.getValue());
			}
			List<ProcessTaskStepWorkerVo> workerList = processTaskMapper.getProcessTaskStepWorkerByProcessTaskStepId(processTaskStepId);
			if (CollectionUtils.isNotEmpty(workerList)) {
				List<String> currentUserTeamList = teamMapper.getTeamUuidListByUserUuid(UserContext.get().getUserUuid(true));
				for (ProcessTaskStepWorkerVo worker : workerList) {
					if (GroupSearch.USER.getValue().equals(worker.getType()) && UserContext.get().getUserUuid(true).equals(worker.getUuid())) {
						currentUserProcessUserTypeList.add(ProcessUserType.WORKER.getValue());
						break;
					} else if (GroupSearch.TEAM.getValue().equals(worker.getType()) && currentUserTeamList.contains(worker.getUuid())) {
						currentUserProcessUserTypeList.add(ProcessUserType.WORKER.getValue());
						break;
					} else if (GroupSearch.ROLE.getValue().equals(worker.getType()) && UserContext.get().getRoleUuidList().contains(worker.getUuid())) {
						currentUserProcessUserTypeList.add(ProcessUserType.WORKER.getValue());
						break;
					}
				}
			}

			return currentUserProcessUserTypeList;
		}

		/**
		 * 
		 * @Time:2020年4月2日
		 * @Description: 获取流程节点配置中的当前用户的拥有的权限
		 * @param processTaskVo
		 * @param processTaskStepVo
		 * @param actionList                     要获取的权限集合
		 * @param currentUserProcessUserTypeList 当前用户工单干系人列表
		 * @return List<String>
		 */
		private static List<String> getProcessTaskStepConfigActionList(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, List<String> actionList, List<String> currentUserProcessUserTypeList) {
			String stepConfig = processTaskMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());
			JSONObject stepConfigObj = JSON.parseObject(stepConfig);
			JSONArray authorityList = stepConfigObj.getJSONArray("authorityList");
			// 如果步骤自定义权限设置为空，则用组件的全局权限设置
			if (CollectionUtils.isEmpty(authorityList)) {
				ProcessStepHandlerVo processStepHandlerVo = processStepHandlerMapper.getProcessStepHandlerByHandler(processTaskStepVo.getHandler());
				if(processStepHandlerVo != null) {
					JSONObject handlerConfigObj = processStepHandlerVo.getConfig();
					if(MapUtils.isNotEmpty(handlerConfigObj)) {
						authorityList = handlerConfigObj.getJSONArray("authorityList");
					}
				}
			}
			List<String> configActionList = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(authorityList)) {
				List<String> currentUserTeamList = teamMapper.getTeamUuidListByUserUuid(UserContext.get().getUserUuid(true));
				for (int i = 0; i < authorityList.size(); i++) {
					JSONObject authorityObj = authorityList.getJSONObject(i);
					JSONArray acceptList = authorityObj.getJSONArray("acceptList");
					if (CollectionUtils.isNotEmpty(acceptList)) {
						for (int j = 0; j < acceptList.size(); j++) {
							String accept = acceptList.getString(j);
							String[] split = accept.split("#");
							if (GroupSearch.COMMON.getValue().equals(split[0])) {
								if (currentUserProcessUserTypeList.contains(split[1])) {
									configActionList.add(authorityObj.getString("action"));
									break;
								}
							} else if (ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue().equals(split[0])) {
								if (currentUserProcessUserTypeList.contains(split[1])) {
									configActionList.add(authorityObj.getString("action"));
									break;
								}
							} else if (GroupSearch.USER.getValue().equals(split[0])) {
								if (UserContext.get().getUserUuid(true).equals(split[1])) {
									configActionList.add(authorityObj.getString("action"));
									break;
								}
							} else if (GroupSearch.TEAM.getValue().equals(split[0])) {
								if (currentUserTeamList.contains(split[1])) {
									configActionList.add(authorityObj.getString("action"));
									break;
								}
							} else if (GroupSearch.ROLE.getValue().equals(split[0])) {
								if (UserContext.get().getRoleUuidList().contains(split[1])) {
									configActionList.add(authorityObj.getString("action"));
									break;
								}
							}
						}
					}
				}
				configActionList.retainAll(actionList);
			}
			return configActionList;
		}

		/**
		 * 
		 * @Time:2020年4月3日
		 * @Description: 获取工单中当前用户能撤回的步骤列表
		 * @param processTaskId
		 * @return Set<ProcessTaskStepVo>
		 */
		protected static Set<ProcessTaskStepVo> getRetractableStepListByProcessTaskId(Long processTaskId) {
			Set<ProcessTaskStepVo> resultSet = new HashSet<>();
			List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskId(processTaskId);
			for (ProcessTaskStepVo stepVo : processTaskStepList) {
				/** 找到所有已激活步骤 **/
				if (stepVo.getIsActive().equals(1)) {
					resultSet.addAll(getRetractableStepListByProcessTaskStepId(stepVo.getId()));
				}
			}
			return resultSet;
		}

		/**
		 * 
		 * @Author: 14378
		 * @Time:2020年4月3日
		 * @Description: 获取当前步骤的前置步骤列表中处理人是当前用户的步骤列表
		 * @param processTaskStepId 已激活的步骤id
		 * @return List<ProcessTaskStepVo>
		 */
		private static List<ProcessTaskStepVo> getRetractableStepListByProcessTaskStepId(Long processTaskStepId) {
			List<ProcessTaskStepVo> resultList = new ArrayList<>();
			/** 所有前置步骤 **/
			List<ProcessTaskStepVo> fromStepList = processTaskMapper.getFromProcessTaskStepByToId(processTaskStepId);
			/** 找到所有已完成步骤 **/
			for (ProcessTaskStepVo fromStep : fromStepList) {
				IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(fromStep.getHandler());
				if (handler != null) {
					if (ProcessStepMode.MT == handler.getMode()) {// 手动处理节点
						// 获取步骤处理人
						List<ProcessTaskStepUserVo> majorUserList = processTaskMapper.getProcessTaskStepUserByStepId(fromStep.getId(), ProcessUserType.MAJOR.getValue());
						List<String> majorUserUuidList = majorUserList.stream().map(ProcessTaskStepUserVo::getUserUuid).collect(Collectors.toList());
						if (majorUserUuidList.contains(UserContext.get().getUserUuid(true))) {
							resultList.add(fromStep);
						}
					} else {// 自动处理节点，继续找前置节点
						resultList.addAll(getRetractableStepListByProcessTaskStepId(fromStep.getId()));
					}
				} else {
					throw new ProcessStepHandlerNotFoundException(fromStep.getHandler());
				}
			}
			return resultList;
		}

		/**
		 * 
		 * @Time:2020年4月3日
		 * @Description: 获取工单中当前用户能处理的步骤列表
		 * @param processTaskId
		 * @return List<ProcessTaskStepVo>
		 */
		protected static List<ProcessTaskStepVo> getProcessableStepList(Long processTaskId) {
			List<ProcessTaskStepVo> resultList = new ArrayList<>();
			List<String> currentUserTeamList = teamMapper.getTeamUuidListByUserUuid(UserContext.get().getUserUuid(true));
			List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskId(processTaskId);
			for (ProcessTaskStepVo stepVo : processTaskStepList) {
				/** 找到所有已激活未处理的步骤 **/
				if (stepVo.getIsActive().equals(1)) {
					List<ProcessTaskStepWorkerVo> processTaskStepWorkerList = processTaskMapper.getProcessTaskStepWorkerByProcessTaskStepId(stepVo.getId());
					for (ProcessTaskStepWorkerVo processTaskStepWorkerVo : processTaskStepWorkerList) {
						if (GroupSearch.USER.getValue().equals(processTaskStepWorkerVo.getType()) && UserContext.get().getUserUuid(true).equals(processTaskStepWorkerVo.getUuid())) {
							resultList.add(stepVo);
							break;
						} else if (GroupSearch.TEAM.getValue().equals(processTaskStepWorkerVo.getType()) && currentUserTeamList.contains(processTaskStepWorkerVo.getUuid())) {
							resultList.add(stepVo);
							break;
						} else if (GroupSearch.ROLE.getValue().equals(processTaskStepWorkerVo.getType()) && UserContext.get().getRoleUuidList().contains(processTaskStepWorkerVo.getUuid())) {
							resultList.add(stepVo);
							break;
						}
					}
				}
			}
			return resultList;
		}

		/**
		 * 
		 * @Time:2020年4月18日
		 * @Description: 获取工单中当前用户能催办的步骤列表
		 * @param processTaskId
		 * @return List<ProcessTaskStepVo>
		 */
		protected static List<ProcessTaskStepVo> getUrgeableStepList(Long processTaskId) {
			ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskById(processTaskId);
			List<ProcessTaskStepVo> resultList = new ArrayList<>();
			List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.PROCESS.getValue());
			for (ProcessTaskStepVo processTaskStep : processTaskStepList) {
				if (processTaskStep.getIsActive().intValue() == 1) {
					List<String> currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStep.getId());
					List<String> actionList = new ArrayList<>();
					actionList.add(ProcessTaskStepAction.URGE.getValue());
					List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStep, actionList, currentUserProcessUserTypeList);
					if (configActionList.contains(ProcessTaskStepAction.URGE.getValue())) {
						resultList.add(processTaskStep);
					}
				}
			}
			return resultList;
		}
	}

	protected static class AuditHandler extends CodeDriverThread {
		private ProcessTaskStepVo currentProcessTaskStepVo;
		private ProcessTaskStepAction action;

		public AuditHandler(ProcessTaskStepVo _currentProcessTaskStepVo, ProcessTaskStepAction _action) {
			currentProcessTaskStepVo = _currentProcessTaskStepVo;
			action = _action;
		}

		protected static synchronized void audit(ProcessTaskStepVo currentProcessTaskStepVo, ProcessTaskStepAction action) {
			if (!TransactionSynchronizationManager.isSynchronizationActive()) {
				AuditHandler handler = new AuditHandler(currentProcessTaskStepVo, action);
				CommonThreadPool.execute(handler);
			} else {
				List<AuditHandler> handlerList = AUDIT_HANDLERS.get();
				if (handlerList == null) {
					handlerList = new ArrayList<>();
					AUDIT_HANDLERS.set(handlerList);
					TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
						@Override
						public void afterCommit() {
							List<AuditHandler> handlerList = AUDIT_HANDLERS.get();
							for (AuditHandler handler : handlerList) {
								CommonThreadPool.execute(handler);
							}
						}

						@Override
						public void afterCompletion(int status) {
							AUDIT_HANDLERS.remove();
						}
					});
				}
				handlerList.add(new AuditHandler(currentProcessTaskStepVo, action));
			}
		}

		@Override
		public void execute() {
			String oldName = Thread.currentThread().getName();
			Thread.currentThread().setName("PROCESSTASK-AUDIT-" + currentProcessTaskStepVo.getId() + "-" + action.getValue());
			try {
				ProcessTaskStepAuditVo processTaskStepAuditVo = new ProcessTaskStepAuditVo();
				processTaskStepAuditVo.setAction(action.getValue());
				processTaskStepAuditVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
				processTaskStepAuditVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
				processTaskStepAuditVo.setUserUuid(UserContext.get().getUserUuid());// 兼容automatic作业无用户
				processTaskStepAuditVo.setStepStatus(currentProcessTaskStepVo.getStatus());
				processTaskMapper.insertProcessTaskStepAudit(processTaskStepAuditVo);
				JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
				if (MapUtils.isNotEmpty(paramObj)) {
					for (ProcessTaskAuditDetailType auditDetailType : ProcessTaskAuditDetailType.values()) {
						String newDataHash = null;
						String newData = paramObj.getString(auditDetailType.getParamName());
						if (StringUtils.isNotBlank(newData)) {
							ProcessTaskContentVo contentVo = new ProcessTaskContentVo(newData);
							processTaskMapper.replaceProcessTaskContent(contentVo);
							newDataHash = contentVo.getHash();
						}
						String oldDataHash = paramObj.getString(auditDetailType.getOldDataParamName());
						if (!Objects.equals(oldDataHash, newDataHash)) {
							processTaskMapper.insertProcessTaskStepAuditDetail(new ProcessTaskStepAuditDetailVo(processTaskStepAuditVo.getId(), auditDetailType.getValue(), oldDataHash, newDataHash));
						}
					}
				}

			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			} finally {
				Thread.currentThread().setName(oldName);
			}
		}
	}

	protected static class DataValid {

		public static boolean formAttributeDataValid(ProcessTaskStepVo currentProcessTaskStepVo) {

			ProcessTaskFormVo processTaskFormVo = processTaskMapper.getProcessTaskFormByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			if (processTaskFormVo == null) {
				return true;
			}
			if (StringUtils.isBlank(processTaskFormVo.getFormContent())) {
				return true;
			}
			FormVersionVo formVersionVo = new FormVersionVo();
			formVersionVo.setFormConfig(processTaskFormVo.getFormContent());
			formVersionVo.setFormUuid(processTaskFormVo.getFormUuid());
			List<FormAttributeVo> formAttributeList = formVersionVo.getFormAttributeList();
			if (formAttributeList == null || formAttributeList.isEmpty()) {
				return true;
			}
			Map<String, Object> formAttributeDataMap = new HashMap<>();
			List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			for (ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo : processTaskFormAttributeDataList) {
				formAttributeDataMap.put(processTaskFormAttributeDataVo.getAttributeUuid(), processTaskFormAttributeDataVo.getDataObj());
			}
			Map<String, String> formAttributeActionMap = new HashMap<>();
			List<ProcessTaskStepFormAttributeVo> processTaskStepFormAttributeList = processTaskMapper.getProcessTaskStepFormAttributeByProcessTaskStepId(currentProcessTaskStepVo.getId());
			for (ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo : processTaskStepFormAttributeList) {
				formAttributeActionMap.put(processTaskStepFormAttributeVo.getAttributeUuid(), processTaskStepFormAttributeVo.getAction());
			}
			ProcessTaskStepDataVo processTaskStepDataVo = new ProcessTaskStepDataVo();
			processTaskStepDataVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			processTaskStepDataVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
			processTaskStepDataVo.setType(ProcessTaskStepDataType.STEPDRAFTSAVE.getValue());
			processTaskStepDataVo.setFcu(UserContext.get().getUserUuid(true));
			processTaskStepDataVo = processTaskStepDataMapper.getProcessTaskStepData(processTaskStepDataVo);
			List<String> hidecomponentList = new ArrayList<>();
			if(processTaskStepDataVo != null) {
				JSONObject dataObj = processTaskStepDataVo.getData();
				if (MapUtils.isNotEmpty(dataObj)) {
					JSONArray hidecomponentArray = dataObj.getJSONArray("hidecomponentList");
					if (CollectionUtils.isNotEmpty(hidecomponentArray)) {
						hidecomponentList = JSON.parseArray(JSON.toJSONString(hidecomponentArray), String.class);
					}
				}
			}
			
			for (FormAttributeVo formAttributeVo : formAttributeList) {
				if (!formAttributeVo.isRequired()) {
					continue;
				}
				if (formAttributeActionMap.containsKey(formAttributeVo.getUuid())) {
					continue;
				}
				if (hidecomponentList.contains(formAttributeVo.getUuid())) {
					continue;
				}
				Object data = formAttributeDataMap.get(formAttributeVo.getUuid());
				if(data != null) {
					if(data instanceof String) {
						if (StringUtils.isBlank(data.toString())) {
							throw new ProcessTaskRuntimeException("表单属性：'" + formAttributeVo.getLabel() + "'不能为空");
						}
					}else if(data instanceof JSONArray) {
						if(CollectionUtils.isEmpty((JSONArray) data)){
							throw new ProcessTaskRuntimeException("表单属性：'" + formAttributeVo.getLabel() + "'不能为空");
						}
					}else if(data instanceof JSONObject) {
						if(MapUtils.isEmpty((JSONObject) data)) {
							throw new ProcessTaskRuntimeException("表单属性：'" + formAttributeVo.getLabel() + "'不能为空");
						}
					}
				}else {
					throw new ProcessTaskRuntimeException("表单属性：'" + formAttributeVo.getLabel() + "'不能为空");
				}
			}
			return true;
		}
	}

	protected static class ProcessTaskHandlerUtil {
		public static ProcessTaskVo getProcessTaskDetailInfoById(Long processTaskId) {
			/** 工单基本信息 **/
			ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
			if (processTaskVo == null) {
				throw new ProcessTaskNotFoundException(processTaskId.toString());
			}

			/** 工单流程图信息 **/
			ProcessTaskConfigVo processTaskConfig = processTaskMapper.getProcessTaskConfigByHash(processTaskVo.getConfigHash());
			if (processTaskConfig == null) {
				throw new ProcessTaskRuntimeException("没有找到工单：'" + processTaskId + "'的流程图配置信息");
			}
			processTaskVo.setConfig(processTaskConfig.getConfig());
			/** 开始步骤信息 **/
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
			/** 上报内容 **/
			List<ProcessTaskStepContentVo> processTaskStepContentList = processTaskMapper.getProcessTaskStepContentProcessTaskStepId(startProcessTaskStepId);
			if (!processTaskStepContentList.isEmpty()) {
				ProcessTaskContentVo processTaskContentVo = processTaskMapper.getProcessTaskContentByHash(processTaskStepContentList.get(0).getContentHash());
				if (processTaskContentVo != null) {
					comment.setContent(processTaskContentVo.getContent());
				}
			}
			/** 附件 **/
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

			/** 优先级 **/
			PriorityVo priorityVo = priorityMapper.getPriorityByUuid(processTaskVo.getPriorityUuid());
			if(priorityVo == null) {
				priorityVo = new PriorityVo();
				priorityVo.setUuid(processTaskVo.getPriorityUuid());
				priorityVo.setName(processTaskVo.getPriorityUuid());
			}
			processTaskVo.setPriority(priorityVo);
			/** 上报服务路径 **/
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
				ChannelTypeVo channelTypeVo = channelMapper.getChannelTypeByUuid(channelVo.getChannelTypeUuid());
				if(channelTypeVo == null) {
					channelTypeVo = new ChannelTypeVo();
					channelTypeVo.setUuid(channelVo.getChannelTypeUuid());
					channelTypeVo.setName(channelVo.getChannelTypeUuid());
				}
				processTaskVo.setChannelType(channelTypeVo);
			}
			/** 计算耗时 **/ 
			if (processTaskVo.getEndTime() != null) {
				long timeCost = worktimeMapper.calculateCostTime(processTaskVo.getWorktimeUuid(), processTaskVo.getStartTime().getTime(), processTaskVo.getEndTime().getTime());
				processTaskVo.setTimeCost(timeCost);
			}

			/** 表单数据 **/
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
		
		/**
		 * 
		* @Author: 14378
		* @Time:2020年7月3日
		* @Description: 获取所有工单干系人信息，用于通知接收人
		* @param processTaskId 工单id
		* @param processTaskStepId 步骤id
		* @param receiverMap 工单干系人信息
		* @return void
		 */
		public static void getReceiverMap(Long processTaskId, Long processTaskStepId, Map<String, List<NotifyReceiverVo>> receiverMap) {
			ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
			if (processTaskVo != null) {
				/** 上报人 **/
				if(StringUtils.isNotBlank(processTaskVo.getOwner())) {
					List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.OWNER.getValue());
					if(notifyReceiverList == null) {
						notifyReceiverList = new ArrayList<>();
						receiverMap.put(ProcessUserType.OWNER.getValue(), notifyReceiverList);
					}
					notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskVo.getOwner()));
				}
				/** 代报人 **/
				if(StringUtils.isNotBlank(processTaskVo.getReporter())) {
					List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.REPORTER.getValue());
					if(notifyReceiverList == null) {
						notifyReceiverList = new ArrayList<>();
						receiverMap.put(ProcessUserType.REPORTER.getValue(), notifyReceiverList);
					}
					notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), processTaskVo.getReporter()));
				}
			}
			/** 主处理人 **/
			List<ProcessTaskStepUserVo> majorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.MAJOR.getValue());
			if (CollectionUtils.isNotEmpty(majorUserList)) {
				List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.MAJOR.getValue());
				if(notifyReceiverList == null) {
					notifyReceiverList = new ArrayList<>();
					receiverMap.put(ProcessUserType.MAJOR.getValue(), notifyReceiverList);
				}
				notifyReceiverList.add(new NotifyReceiverVo(GroupSearch.USER.getValue(), majorUserList.get(0).getUserUuid()));
			}
			/** 子任务处理人 **/
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
			/** 待办人 **/
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
			/** 待处理人 **/
			List<ProcessTaskStepWorkerVo> workerList = processTaskMapper.getProcessTaskStepWorkerByProcessTaskStepId(processTaskStepId);
			if(CollectionUtils.isNotEmpty(workerList)) {
				List<NotifyReceiverVo> notifyReceiverList = receiverMap.get(ProcessUserType.WORKER.getValue());
				if(notifyReceiverList == null) {
					notifyReceiverList = new ArrayList<>();
					receiverMap.put(ProcessUserType.WORKER.getValue(), notifyReceiverList);
				}
				for(ProcessTaskStepWorkerVo processTaskStepWorkerVo : workerList) {
					notifyReceiverList.add(new NotifyReceiverVo(processTaskStepWorkerVo.getType(), processTaskStepWorkerVo.getUuid()));
				}
			}
		}
	}
}
