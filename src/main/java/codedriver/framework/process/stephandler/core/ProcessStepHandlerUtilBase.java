package codedriver.framework.process.stephandler.core;

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
import codedriver.framework.integration.core.IIntegrationHandler;
import codedriver.framework.integration.core.IntegrationHandlerFactory;
import codedriver.framework.integration.dao.mapper.IntegrationMapper;
import codedriver.framework.integration.dto.IntegrationResultVo;
import codedriver.framework.integration.dto.IntegrationVo;
import codedriver.framework.notify.dao.mapper.NotifyMapper;
import codedriver.framework.notify.dto.NotifyPolicyVo;
import codedriver.framework.notify.dto.NotifyReceiverVo;
import codedriver.framework.notify.dto.ParamMappingVo;
import codedriver.framework.process.audithandler.core.IProcessTaskAuditDetailType;
import codedriver.framework.process.audithandler.core.IProcessTaskAuditType;
import codedriver.framework.process.audithandler.core.ProcessTaskAuditDetailTypeFactory;
import codedriver.framework.process.column.core.ProcessTaskUtil;
import codedriver.framework.process.constvalue.*;
import codedriver.framework.process.dao.mapper.*;
import codedriver.framework.process.dao.mapper.score.ProcesstaskScoreMapper;
import codedriver.framework.process.dao.mapper.score.ScoreTemplateMapper;
import codedriver.framework.process.dto.*;
import codedriver.framework.process.dto.score.ProcessScoreTemplateVo;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
import codedriver.framework.process.exception.process.ProcessStepHandlerNotFoundException;
import codedriver.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import codedriver.framework.process.exception.processtask.ProcessTaskNoPermissionException;
import codedriver.framework.process.integration.handler.ProcessRequestFrom;
import codedriver.framework.process.notify.core.NotifyTriggerType;
import codedriver.framework.process.notify.schedule.plugin.ProcessTaskSlaNotifyJob;
import codedriver.framework.process.notify.schedule.plugin.ProcessTaskSlaTransferJob;
import codedriver.framework.process.score.schedule.plugin.ProcessTaskAutoScoreJob;
import codedriver.framework.process.util.WorkTimeUtil;
import codedriver.framework.scheduler.core.IJob;
import codedriver.framework.scheduler.core.SchedulerManager;
import codedriver.framework.scheduler.dto.JobObject;
import codedriver.framework.scheduler.exception.ScheduleHandlerNotFoundException;
import codedriver.framework.util.ConditionUtil;
import codedriver.framework.util.NotifyPolicyUtil;
import codedriver.framework.util.RunScriptUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ProcessStepHandlerUtilBase {
	static Logger logger = LoggerFactory.getLogger(ProcessStepHandlerUtilBase.class);

	private static final ThreadLocal<List<AuditHandler>> AUDIT_HANDLERS = new ThreadLocal<>();
	private static final ThreadLocal<List<SlaHandler>> SLA_HANDLERS = new ThreadLocal<>();
	private static final ThreadLocal<List<NotifyHandler>> NOTIFY_HANDLERS = new ThreadLocal<>();
	private static final ThreadLocal<List<ActionHandler>> ACTION_HANDLERS = new ThreadLocal<>();
	private static final ThreadLocal<List<AutoScoreHandler>> AUTOSCORE_HANDLERS = new ThreadLocal<>();
	protected static ProcessMapper processMapper;
	protected static ProcessTaskMapper processTaskMapper;
	protected static FormMapper formMapper;
	protected static UserMapper userMapper;
	protected static ProcessTaskStepTimeAuditMapper processTaskStepTimeAuditMapper;
	protected static WorktimeMapper worktimeMapper;
	protected static ChannelMapper channelMapper;
	protected static NotifyMapper notifyMapper;
	protected static FileMapper fileMapper;
	protected static TeamMapper teamMapper;
	protected static ProcessStepHandlerMapper processStepHandlerMapper;
	protected static PriorityMapper priorityMapper;
	private static IntegrationMapper integrationMapper;
    protected static CatalogMapper catalogMapper;
    protected static SelectContentByHashMapper selectContentByHashMapper;
	protected static ScoreTemplateMapper scoreTemplateMapper;
	protected static ProcesstaskScoreMapper processtaskScoreMapper;

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
	
	@Autowired
    public void setCatalogMapper(CatalogMapper _catalogMapper) {
	    catalogMapper = _catalogMapper;
    }
	@Autowired
	public void setSelectContentByHashMapper(SelectContentByHashMapper _selectContentByHashMapper) {
	    selectContentByHashMapper = _selectContentByHashMapper;
	}

	@Autowired
	public void setScoreTemplateMapper(ScoreTemplateMapper _scoreTemplateMapper) {
		scoreTemplateMapper = _scoreTemplateMapper;
	}

	@Autowired
	public void setProcesstaskScoreMapper(ProcesstaskScoreMapper _processtaskScoreMapper) {
		processtaskScoreMapper = _processtaskScoreMapper;
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
				String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(stepVo.getConfigHash());
				stepVo.setConfig(stepConfig);
                IProcessStepUtilHandler processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(stepVo.getHandler());
                if(processStepUtilHandler == null) {
                    throw new ProcessStepUtilHandlerNotFoundException(stepVo.getHandler());
                }
				ProcessStepHandlerVo processStepHandlerVo = processStepHandlerMapper.getProcessStepHandlerByHandler(stepVo.getHandler());
                stepVo.setGlobalConfig(processStepUtilHandler.makeupConfig(processStepHandlerVo != null ? processStepHandlerVo.getConfig() : null));
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
								ProcessTaskVo processTaskVo = ProcessStepUtilHandlerFactory.getHandler().getProcessTaskDetailById(currentProcessTaskStepVo.getProcessTaskId());
								processTaskVo.setCurrentProcessTaskStep(currentProcessTaskStepVo);
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
							AuditHandler.audit(stepVo, ProcessTaskAuditType.RESTFULACTION);
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
				String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(stepVo.getConfigHash());
				stepVo.setConfig(stepConfig);
	            IProcessStepUtilHandler processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(stepVo.getHandler());
	            if(processStepUtilHandler == null) {
	                throw new ProcessStepUtilHandlerNotFoundException(stepVo.getHandler());
	            }
				ProcessStepHandlerVo processStepHandlerVo = processStepHandlerMapper.getProcessStepHandlerByHandler(stepVo.getHandler());
				stepVo.setGlobalConfig(processStepUtilHandler.makeupConfig(processStepHandlerVo != null ? processStepHandlerVo.getConfig() : null));

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
							ProcessTaskVo processTaskVo = processStepUtilHandler.getProcessTaskDetailById(currentProcessTaskStepVo.getProcessTaskId());
							processTaskVo.setCurrentProcessTaskStep(currentProcessTaskStepVo);
							JSONObject conditionParamData = ProcessTaskUtil.getProcessFieldData(processTaskVo, true);
							JSONObject templateParamData = ProcessTaskUtil.getProcessFieldData(processTaskVo, false);
							Map<String, List<NotifyReceiverVo>> receiverMap = new HashMap<>();
							processStepUtilHandler.getReceiverMap(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), receiverMap);
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

		private static long getTimeCost(List<ProcessTaskStepTimeAuditVo> processTaskStepTimeAuditList, String worktimeUuid) {
			List<Map<String, Long>> timeList = new ArrayList<>();
			for (ProcessTaskStepTimeAuditVo auditVo : processTaskStepTimeAuditList) {
				Long startTime = null, endTime = null;
				if (auditVo.getActiveTimeLong() != null) {
					startTime = auditVo.getActiveTimeLong();
				}else if(auditVo.getStartTimeLong() != null) {
                    startTime = auditVo.getStartTimeLong();
                }
				if (auditVo.getCompleteTimeLong() != null) {
					endTime = auditVo.getCompleteTimeLong();
				} else if (auditVo.getAbortTimeLong() != null) {
					endTime = auditVo.getAbortTimeLong();
				} else if (auditVo.getBackTimeLong() != null) {
					endTime = auditVo.getBackTimeLong();
				}else if(auditVo.getPauseTimeLong() != null) {
                    endTime = auditVo.getPauseTimeLong();
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
			if (CollectionUtils.isNotEmpty(processTaskStepTimeAuditList)) {
				List<Map<String, Long>> timeZoneList = new ArrayList<>();
				for (ProcessTaskStepTimeAuditVo auditVo : processTaskStepTimeAuditList) {
					Long startTime = null, endTime = null;
					if (auditVo.getActiveTimeLong() != null) {
						startTime = auditVo.getActiveTimeLong();
					}else if(auditVo.getStartTimeLong() != null) {
					    startTime = auditVo.getStartTimeLong();
					}
					
					if (auditVo.getCompleteTimeLong() != null) {
						endTime = auditVo.getCompleteTimeLong();
					} else if (auditVo.getAbortTimeLong() != null) {
						endTime = auditVo.getAbortTimeLong();
					} else if (auditVo.getBackTimeLong() != null) {
						endTime = auditVo.getBackTimeLong();
					}else if(auditVo.getPauseTimeLong() != null) {
					    endTime = auditVo.getPauseTimeLong();
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
			List<Long> slaIdList = processTaskMapper.getSlaIdListByProcessTaskStepId(currentProcessTaskStepVo.getId());
			if (CollectionUtils.isNotEmpty(slaIdList)) {
				ProcessTaskVo processTaskVo = ProcessStepUtilHandlerFactory.getHandler().getProcessTaskDetailById(currentProcessTaskStepVo.getProcessTaskId());
				processTaskVo.setCurrentProcessTaskStep(currentProcessTaskStepVo);
				String worktimeUuid = processTaskVo.getWorktimeUuid();
				for (Long slaId : slaIdList) {
				    String config = processTaskMapper.getProcessTaskSlaConfigById(slaId);
				    JSONObject slaConfigObj = JSON.parseObject(config);
				    if(MapUtils.isNotEmpty(slaConfigObj)) {
				        /** 旧的超时时间点 **/
				        Date oldExpireTime = null;
                        boolean isSlaTimeExists = false;
				        /** 如果没有超时时间，证明第一次进入SLA标签范围，开始计算超时时间 **/
	                    ProcessTaskSlaTimeVo slaTimeVo = processTaskMapper.getProcessTaskSlaTimeBySlaId(slaId);
	                    if (slaTimeVo == null) {
                            JSONArray policyList = slaConfigObj.getJSONArray("calculatePolicyList");
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
	                    } else {
	                        /** 记录旧的超时时间点 **/
	                        oldExpireTime = slaTimeVo.getExpireTime();
	                        isSlaTimeExists = true;
	                        // 非第一次进入，进行时间扣减
	                        List<ProcessTaskStepTimeAuditVo> processTaskStepTimeAuditList = processTaskStepTimeAuditMapper.getProcessTaskStepTimeAuditBySlaId(slaId);
	                        long realTimeCost = getRealTimeCost(processTaskStepTimeAuditList);
	                        long timeCost = realTimeCost;
	                        if (StringUtils.isNotBlank(worktimeUuid)) {// 如果有工作时间，则计算实际消耗的工作时间
	                            timeCost = getTimeCost(processTaskStepTimeAuditList, worktimeUuid);
	                        }
	                        slaTimeVo.setRealTimeLeft(slaTimeVo.getTimeSum() - realTimeCost);
	                        slaTimeVo.setTimeLeft(slaTimeVo.getTimeSum() - timeCost);

	                    }

	                    // 修正最终超时日期
	                    if (slaTimeVo != null) {
	                        long now = System.currentTimeMillis();
	                        System.out.println(new Date() + "\t now:"+ now);
	                        slaTimeVo.setRealExpireTime(new Date(now + slaTimeVo.getRealTimeLeft()));
	                        if (StringUtils.isNotBlank(worktimeUuid)) {
	                            if (slaTimeVo.getTimeLeft() != null) {
	                                System.out.println("now + slaTimeVo.getTimeLeft():"+ (now + slaTimeVo.getTimeLeft()));
	                                long expireTime = WorkTimeUtil.calculateExpireTime(now, slaTimeVo.getTimeLeft(), worktimeUuid);
	                                System.out.println("expireTime:"+ expireTime);
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
	                        slaTimeVo.setSlaId(slaId);
	                        slaTimeVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
	                        System.out.println(slaTimeVo);
	                        if (isSlaTimeExists) {
	                            processTaskMapper.updateProcessTaskSlaTime(slaTimeVo);
	                        } else {
	                            processTaskMapper.insertProcessTaskSlaTime(slaTimeVo);
	                        }
	                        /** 有超时时间点 **/
	                        if (slaTimeVo.getExpireTime() != null) {
	                            /** 是否需要启动作业 **/
	                            boolean isStartJob = false;
	                            List<Long> processTaskStepIdList = processTaskMapper.getProcessTaskStepIdListBySlaId(slaId);
	                            if(CollectionUtils.isNotEmpty(processTaskStepIdList)) {
	                                List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepListByIdList(processTaskStepIdList);
	                                for(ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
	                                    if(Objects.equals(processTaskStepVo.getIsActive(), 1)) {
	                                        if(ProcessTaskStatus.PENDING.getValue().equals(processTaskStepVo.getStatus()) || ProcessTaskStatus.PENDING.getValue().equals(processTaskStepVo.getStatus())) {
	                                            isStartJob = true;
	                                            break;
	                                        }
	                                    }
	                                }
	                            }
	                            /** 作业是否已启动 **/
	                            boolean jobStarted = false;
                                List<ProcessTaskSlaNotifyVo> processTaskSlaNotifyList = processTaskMapper.getProcessTaskSlaNotifyBySlaId(slaId);
                                List<ProcessTaskSlaTransferVo> processTaskSlaTransferList = processTaskMapper.getProcessTaskSlaTransferBySlaId(slaId);
                                if(CollectionUtils.isNotEmpty(processTaskSlaNotifyList) || CollectionUtils.isNotEmpty(processTaskSlaTransferList)) {
                                    jobStarted = true;
                                }
                                if(jobStarted) {
                                    if(!isStartJob || !slaTimeVo.getExpireTime().equals(oldExpireTime)) {
                                        jobStarted = false;
                                        for(ProcessTaskSlaNotifyVo processTaskSlaNotifyVo : processTaskSlaNotifyList) {
                                            processTaskMapper.deleteProcessTaskSlaNotifyById(processTaskSlaNotifyVo.getId());
                                        }
                                        for(ProcessTaskSlaTransferVo processTaskSlaTransferVo : processTaskSlaTransferList) {
                                            processTaskMapper.deleteProcessTaskSlaTransferById(processTaskSlaTransferVo.getId());
                                        }
                                    }
                                }
                                /** 作业需要启动，且未启动时，加载定时作业**/
	                            if(isStartJob && !jobStarted) {
	                                // 加载定时作业，执行超时通知操作
	                                JSONArray notifyPolicyList = slaConfigObj.getJSONArray("notifyPolicyList");
	                                if (CollectionUtils.isNotEmpty(notifyPolicyList)) {
	                                    for (int i = 0; i < notifyPolicyList.size(); i++) {
	                                        JSONObject notifyPolicyObj = notifyPolicyList.getJSONObject(i);
	                                        ProcessTaskSlaNotifyVo processTaskSlaNotifyVo = new ProcessTaskSlaNotifyVo();
	                                        processTaskSlaNotifyVo.setSlaId(slaId);
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
	                                JSONArray transferPolicyList = slaConfigObj.getJSONArray("transferPolicyList");
	                                if (CollectionUtils.isNotEmpty(transferPolicyList)) {
	                                    for (int i = 0; i < transferPolicyList.size(); i++) {
	                                        JSONObject transferPolicyObj = transferPolicyList.getJSONObject(i);
	                                        ProcessTaskSlaTransferVo processTaskSlaTransferVo = new ProcessTaskSlaTransferVo();
	                                        processTaskSlaTransferVo.setSlaId(slaId);
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
		}
	}

	protected static class TimeAuditHandler {
		@SuppressWarnings("incomplete-switch")
		protected static void audit(ProcessTaskStepVo currentProcessTaskStepVo, ProcessTaskOperationType action) {
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
			case ABORTPROCESSTASK:
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
			case RECOVERPROCESSTASK:
				if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.PENDING.getValue())) {
					newAuditVo.setActiveTime("now");
					if (processTaskStepTimeAuditVo == null || StringUtils.isNotBlank(processTaskStepTimeAuditVo.getActiveTime())) {
						processTaskStepTimeAuditMapper.insertProcessTaskStepTimeAudit(newAuditVo);
					}
				} else if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
					newAuditVo.setStartTime("now");
					if (processTaskStepTimeAuditVo == null || StringUtils.isNotBlank(processTaskStepTimeAuditVo.getStartTime())) {
					    System.out.println("recover:" + new Date());
						processTaskStepTimeAuditMapper.insertProcessTaskStepTimeAudit(newAuditVo);
					} else if (StringUtils.isBlank(processTaskStepTimeAuditVo.getStartTime())) {// 如果starttime为空，则更新starttime
						newAuditVo.setId(processTaskStepTimeAuditVo.getId());
						processTaskStepTimeAuditMapper.updateProcessTaskStepTimeAudit(newAuditVo);
					}
				}
				break;
			case PAUSE:
                /** 如果找不到审计记录并且pausetime不为空，则新建审计记录 **/
                newAuditVo.setPauseTime("now");
                if (processTaskStepTimeAuditVo == null || StringUtils.isNotBlank(processTaskStepTimeAuditVo.getPauseTime())) {
                    processTaskStepTimeAuditMapper.insertProcessTaskStepTimeAudit(newAuditVo);
                } else if (StringUtils.isBlank(processTaskStepTimeAuditVo.getPauseTime())) {// 如果pausetime为空，则更新pausetime
                    newAuditVo.setId(processTaskStepTimeAuditVo.getId());
                    Date startTime = new Date();
                    try {
                        startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(processTaskStepTimeAuditVo.getStartTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println("pause:" + new Date());                   
                    System.out.println("TimeCost:" + (System.currentTimeMillis() - startTime.getTime()));
                    processTaskStepTimeAuditMapper.updateProcessTaskStepTimeAudit(newAuditVo);
                }
                break;
			}
		}
	}

	protected static class ActionRoleChecker {

		protected static boolean verifyActionAuthoriy(Long processTaskId, ProcessTaskOperationType action) {
			return verifyActionAuthoriy(processTaskId, null, action);
		}

		protected static boolean verifyActionAuthoriy(Long processTaskId, Long processTaskStepId, ProcessTaskOperationType action) {
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
			if (CollectionUtils.isEmpty(verifyActionList) || verifyActionList.contains(ProcessTaskOperationType.POCESSTASKVIEW.getValue())) {
				if (UserContext.get().getUserUuid(true).equals(processTaskVo.getOwner())) {
					resultList.add(ProcessTaskOperationType.POCESSTASKVIEW.getValue());
				} else if (UserContext.get().getUserUuid(true).equals(processTaskVo.getReporter())) {
					resultList.add(ProcessTaskOperationType.POCESSTASKVIEW.getValue());
				} else {
					List<String> currentUserTeamList = teamMapper.getTeamUuidListByUserUuid(UserContext.get().getUserUuid(true));
					List<String> channelList = channelMapper.getAuthorizedChannelUuidList(UserContext.get().getUserUuid(true), currentUserTeamList, UserContext.get().getRoleUuidList(), processTaskVo.getChannelUuid());
					if (channelList.contains(processTaskVo.getChannelUuid())) {
						resultList.add(ProcessTaskOperationType.POCESSTASKVIEW.getValue());
					} else if (processTaskMapper.checkIsWorker(processTaskId, null, UserContext.get().getUserUuid(true), currentUserTeamList, UserContext.get().getRoleUuidList()) > 0) {
						resultList.add(ProcessTaskOperationType.POCESSTASKVIEW.getValue());
					} else if (processTaskMapper.checkIsProcessTaskStepUser(new ProcessTaskStepUserVo(processTaskId, null, UserContext.get().getUserUuid(true))) > 0) {
						resultList.add(ProcessTaskOperationType.POCESSTASKVIEW.getValue());
					}
				}
			}
			// 上报提交
			if (CollectionUtils.isEmpty(verifyActionList) || verifyActionList.contains(ProcessTaskOperationType.STARTPROCESS.getValue())) {
				if (ProcessTaskStatus.DRAFT.getValue().equals(processTaskVo.getStatus())) {
					if (UserContext.get().getUserUuid(true).equals(processTaskVo.getOwner()) || UserContext.get().getUserUuid(true).equals(processTaskVo.getReporter())) {
						resultList.add(ProcessTaskOperationType.STARTPROCESS.getValue());
					}
				}
			}
			if (processTaskStepId != null) {
				// 获取步骤信息
				ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepId);
				if (processTaskStepVo != null) {
					List<String> currentUserProcessUserTypeList = new ArrayList<>();
					List<String> actionList = new ArrayList<>();
					actionList.add(ProcessTaskOperationType.VIEW.getValue());
					actionList.add(ProcessTaskOperationType.TRANSFER.getValue());
					if (CollectionUtils.isEmpty(verifyActionList) || actionList.removeAll(verifyActionList)) {
						actionList = new ArrayList<>();
						actionList.add(ProcessTaskOperationType.VIEW.getValue());
						actionList.add(ProcessTaskOperationType.TRANSFER.getValue());
						if (CollectionUtils.isEmpty(currentUserProcessUserTypeList)) {
							currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStepId);
						}
						List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStepVo, actionList, currentUserProcessUserTypeList);
						// 根据流程设置和步骤状态判断当前用户权限
						for (String action : configActionList) {
							if (ProcessTaskOperationType.VIEW.getValue().equals(action)) {
								resultList.add(action);
							} else if (ProcessTaskOperationType.TRANSFER.getValue().equals(action)) {
								// 步骤状态为已激活的才能转交
								if (processTaskStepVo.getIsActive() == 1) {
									resultList.add(action);
								}
							}
						}
						if (UserContext.get().getUserUuid(true).equals(processTaskVo.getOwner())) {
							resultList.add(ProcessTaskOperationType.VIEW.getValue());
						} else if (UserContext.get().getUserUuid(true).equals(processTaskVo.getReporter())) {
							resultList.add(ProcessTaskOperationType.VIEW.getValue());
						} else if (processTaskMapper.checkIsProcessTaskStepUser(new ProcessTaskStepUserVo(processTaskId, processTaskStepId, UserContext.get().getUserUuid(true))) > 0) {
							resultList.add(ProcessTaskOperationType.VIEW.getValue());
						}
					}

					actionList = new ArrayList<>();
					actionList.add(ProcessTaskOperationType.COMPLETE.getValue());
					actionList.add(ProcessTaskOperationType.SAVE.getValue());
					actionList.add(ProcessTaskOperationType.COMMENT.getValue());
					actionList.add(ProcessTaskOperationType.CREATESUBTASK.getValue());
					actionList.add(ProcessTaskOperationType.ACCEPT.getValue());
					actionList.add(ProcessTaskOperationType.START.getValue());
					actionList.add(ProcessTaskOperationType.BACK.getValue());
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
													resultList.add(ProcessTaskOperationType.COMPLETE.getValue());
												} else if (ProcessFlowDirection.BACKWARD.getValue().equals(processTaskStep.getFlowDirection()) && processTaskStep.getIsActive().intValue() != 0) {
													resultList.add(ProcessTaskOperationType.BACK.getValue());
												}
											}
										}
										resultList.add(ProcessTaskOperationType.SAVE.getValue());
										resultList.add(ProcessTaskOperationType.COMMENT.getValue());
										resultList.add(ProcessTaskOperationType.CREATESUBTASK.getValue());
									}
								} else if (ProcessTaskStatus.PENDING.getValue().equals(processTaskStepVo.getStatus())) {// 已激活未处理
									if (currentUserProcessUserTypeList.contains(ProcessUserType.MAJOR.getValue())) {
										resultList.add(ProcessTaskOperationType.START.getValue());
									} else {// 没有主处理人时是accept
										resultList.add(ProcessTaskOperationType.ACCEPT.getValue());
									}
								}
							}
						}
					}
				}
			}

			List<String> actionList = new ArrayList<>();
			actionList.add(ProcessTaskOperationType.ABORTPROCESSTASK.getValue());
			actionList.add(ProcessTaskOperationType.RECOVERPROCESSTASK.getValue());
			actionList.add(ProcessTaskOperationType.UPDATE.getValue());
			actionList.add(ProcessTaskOperationType.URGE.getValue());
			if (CollectionUtils.isEmpty(verifyActionList) || actionList.removeAll(verifyActionList)) {
				// 终止/恢复流程abort、修改上报内容update取工单当前所有正在处理的节点权限配置的并集
				List<ProcessTaskStepVo> startProcessTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.START.getValue());
				List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.PROCESS.getValue());
				processTaskStepList.addAll(startProcessTaskStepList);
				for (ProcessTaskStepVo processTaskStep : processTaskStepList) {
					if (processTaskStep.getIsActive().intValue() == 1) {
						List<String> currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStep.getId());
						actionList = new ArrayList<>();
						actionList.add(ProcessTaskOperationType.ABORTPROCESSTASK.getValue());
						actionList.add(ProcessTaskOperationType.UPDATE.getValue());
						actionList.add(ProcessTaskOperationType.URGE.getValue());
						List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStep, actionList, currentUserProcessUserTypeList);
						// 根据流程设置和步骤状态判断当前用户权限
						for (String action : configActionList) {
							if (ProcessTaskOperationType.ABORTPROCESSTASK.getValue().equals(action)) {
								// 工单状态为进行中的才能终止
								if (ProcessTaskStatus.RUNNING.getValue().equals(processTaskVo.getStatus())) {
									resultList.add(action);
								}
							} else if (ProcessTaskOperationType.UPDATE.getValue().equals(action)) {
								resultList.add(action);
							} else if (ProcessTaskOperationType.URGE.getValue().equals(action)) {
								resultList.add(action);
							}
						}
					} else if (processTaskStep.getIsActive().intValue() == -1) {
						List<String> currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStep.getId());
						actionList = new ArrayList<>();
						actionList.add(ProcessTaskOperationType.ABORTPROCESSTASK.getValue());
						List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStep, actionList, currentUserProcessUserTypeList);
						if (configActionList.contains(ProcessTaskOperationType.ABORTPROCESSTASK.getValue())) {
							configActionList.add(ProcessTaskOperationType.RECOVERPROCESSTASK.getValue());
						}
						// 根据流程设置和步骤状态判断当前用户权限
						for (String action : configActionList) {
							if (ProcessTaskOperationType.RECOVERPROCESSTASK.getValue().equals(action)) {
								// 工单状态为已终止的才能恢复
								if (ProcessTaskStatus.ABORTED.getValue().equals(processTaskVo.getStatus())) {
									resultList.add(action);
								}
							}
						}
					}
				}
			}

			if (CollectionUtils.isEmpty(verifyActionList) || verifyActionList.contains(ProcessTaskOperationType.WORK.getValue())) {
				List<String> currentUserTeamList = teamMapper.getTeamUuidListByUserUuid(UserContext.get().getUserUuid(true));
				// 有可处理步骤work
				if (processTaskMapper.checkIsWorker(processTaskId, processTaskStepId, UserContext.get().getUserUuid(true), currentUserTeamList, UserContext.get().getRoleUuidList()) > 0) {
					resultList.add(ProcessTaskOperationType.WORK.getValue());
				}
			}

			if (CollectionUtils.isEmpty(verifyActionList) || verifyActionList.contains(ProcessTaskOperationType.RETREAT.getValue())) {
				// 撤销权限retreat
				Set<ProcessTaskStepVo> retractableStepSet = getRetractableStepListByProcessTaskId(processTaskId);
				if (CollectionUtils.isNotEmpty(retractableStepSet)) {
					if (processTaskStepId != null && verifyActionList.contains(ProcessTaskOperationType.RETREAT.getValue())) {
						for (ProcessTaskStepVo processTaskStepVo : retractableStepSet) {
							if (processTaskStepId.equals(processTaskStepVo.getId())) {
								resultList.add(ProcessTaskOperationType.RETREAT.getValue());
							}
						}
					} else {
						resultList.add(ProcessTaskOperationType.RETREAT.getValue());
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
			String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());
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
			List<ProcessTaskStepVo> startProcessTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.START.getValue());
			List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.PROCESS.getValue());
			processTaskStepList.addAll(startProcessTaskStepList);
			for (ProcessTaskStepVo processTaskStep : processTaskStepList) {
				if (processTaskStep.getIsActive().intValue() == 1) {
					List<String> currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStep.getId());
					List<String> actionList = new ArrayList<>();
					actionList.add(ProcessTaskOperationType.URGE.getValue());
					List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStep, actionList, currentUserProcessUserTypeList);
					if (configActionList.contains(ProcessTaskOperationType.URGE.getValue())) {
						resultList.add(processTaskStep);
					}
				}
			}
			return resultList;
		}
	}

	protected static class AuditHandler extends CodeDriverThread {
		private ProcessTaskStepVo currentProcessTaskStepVo;
		private IProcessTaskAuditType action;

		public AuditHandler(ProcessTaskStepVo _currentProcessTaskStepVo, IProcessTaskAuditType _action) {
			currentProcessTaskStepVo = _currentProcessTaskStepVo;
			action = _action;
		}

		public static synchronized void audit(ProcessTaskStepVo currentProcessTaskStepVo, IProcessTaskAuditType action) {
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
					for (IProcessTaskAuditDetailType auditDetailType : ProcessTaskAuditDetailTypeFactory.getAuditDetailTypeList()) {
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

	protected static class AutoScoreHandler extends CodeDriverThread {
		private ProcessTaskVo currentProcessTaskVo;

		public AutoScoreHandler(ProcessTaskVo _currentProcessTaskVo) {
			currentProcessTaskVo = _currentProcessTaskVo;
		}

		public static synchronized void autoScore(ProcessTaskVo currentProcessTaskVo) {
			if (!TransactionSynchronizationManager.isSynchronizationActive()) {
				AutoScoreHandler handler = new AutoScoreHandler(currentProcessTaskVo);
				CommonThreadPool.execute(handler);
			} else {
				List<AutoScoreHandler> handlerList = AUTOSCORE_HANDLERS.get();
				if (handlerList == null) {
					handlerList = new ArrayList<>();
					AUTOSCORE_HANDLERS.set(handlerList);
					TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
						@Override
						public void afterCommit() {
							List<AutoScoreHandler> handlerList = AUTOSCORE_HANDLERS.get();
							for (AutoScoreHandler handler : handlerList) {
								CommonThreadPool.execute(handler);
							}
						}

						@Override
						public void afterCompletion(int status) {
							AUTOSCORE_HANDLERS.remove();
						}
					});
				}
				handlerList.add(new AutoScoreHandler(currentProcessTaskVo));
			}
		}

		@Override
		public void execute() {
			Thread.currentThread().setName("PROCESSTASK-AUTOSCORE-" + currentProcessTaskVo.getId());

			/**
			 * 先检查是否绑定评分模版,如果绑定了，则检查是否设置自动评分
			 * 如果设置了自动评分，则启动定时器监听工单是否评分，若超时未评分，则系统自动评分
			 */
			ProcessTaskVo task = processTaskMapper.getProcessTaskById(currentProcessTaskVo.getId());
			ProcessScoreTemplateVo processScoreTemplate = null;
			if(task != null){
				processScoreTemplate = scoreTemplateMapper.getProcessScoreTemplateByProcessUuid(task.getProcessUuid());
			}
			String config = null;
			Integer isAuto = null;
			Integer autoTime = null;
			if(processScoreTemplate != null && StringUtils.isNotBlank(config = processScoreTemplate.getConfig())){
				JSONObject configObj = JSONObject.parseObject(config);
				isAuto = configObj.getInteger("isAuto");
				autoTime = configObj.getInteger("autoTime");
			}
			if(isAuto != null && Integer.parseInt(isAuto.toString()) == 1 && autoTime != null){
				IJob jobHandler = SchedulerManager.getHandler(ProcessTaskAutoScoreJob.class.getName());
				if (jobHandler != null) {
					JobObject.Builder jobObjectBuilder = new JobObject.Builder(currentProcessTaskVo.getId().toString(), jobHandler.getGroupName(), jobHandler.getClassName(), TenantContext.get().getTenantUuid()).addData("processTaskId", currentProcessTaskVo.getId());
					JobObject jobObject = jobObjectBuilder.build();
					jobHandler.reloadJob(jobObject);
				} else {
					throw new ScheduleHandlerNotFoundException(ProcessTaskAutoScoreJob.class.getName());
				}
			}
		}
	}

	protected static class DataValid {
		/**
		 * 
		* @Time:2020年7月28日
		* @Description: 获取需要验证表单数据，并校验
		* @param currentProcessTaskStepVo
		* @return boolean
		 */
		public static boolean formAttributeDataValidFromDb(ProcessTaskStepVo currentProcessTaskStepVo) {
			ProcessTaskFormVo processTaskFormVo = processTaskMapper.getProcessTaskFormByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			if (processTaskFormVo != null && StringUtils.isNotBlank(processTaskFormVo.getFormContentHash())) {
			    String formContent = selectContentByHashMapper.getProcessTaskFromContentByHash(processTaskFormVo.getFormContentHash());
			    FormVersionVo formVersionVo = new FormVersionVo();
	            formVersionVo.setFormConfig(formContent);
	            formVersionVo.setFormUuid(processTaskFormVo.getFormUuid());
	            List<FormAttributeVo> formAttributeList = formVersionVo.getFormAttributeList();
	            if (CollectionUtils.isNotEmpty(formAttributeList)) {
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
	                currentProcessTaskStepVo.setFormAttributeDataMap(formAttributeDataMap);
	                currentProcessTaskStepVo.setFormAttributeVoList(formAttributeList);
	                currentProcessTaskStepVo.setFormAttributeActionMap(formAttributeActionMap);
	                formAttributeDataValid(currentProcessTaskStepVo);
	            }
			}
			
			return true;
		}
		
		/**
         * 
        * @Time:2020年7月28日
        * @Description: 验证表单数据是否合法
        * @param currentProcessTaskStepVo
        * @return boolean
         */
		public static boolean formAttributeDataValid(ProcessTaskStepVo currentProcessTaskStepVo) {
            List<String> hidecomponentList = JSON.parseArray(JSON.toJSONString(currentProcessTaskStepVo.getParamObj().getJSONArray("hidecomponentList")), String.class);           
            for (FormAttributeVo formAttributeVo : currentProcessTaskStepVo.getFormAttributeVoList()) {
                if (!formAttributeVo.isRequired()) {
                    continue;
                }
                if ( currentProcessTaskStepVo.getFormAttributeActionMap().containsKey(formAttributeVo.getUuid())) {
                    continue;
                }
                if (CollectionUtils.isNotEmpty(hidecomponentList) && hidecomponentList.contains(formAttributeVo.getUuid())) {
                    continue;
                }
                Object data = currentProcessTaskStepVo.getFormAttributeDataMap().get(formAttributeVo.getUuid());
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
		
		/**
         * 
        * @Time:2020年7月28日
        * @Description: 获取验证基本信息数据是否合法，并验证
        * @param currentProcessTaskStepVo
        * @return boolean
         */
        public static boolean baseInfoValidFromDb(ProcessTaskStepVo currentProcessTaskStepVo) {
            ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskById(currentProcessTaskStepVo.getProcessTaskId());
            baseInfoValid(currentProcessTaskStepVo, processTaskVo);
            return true;
        }
        
		/**
		 * 
		* @Time:2020年7月28日
		* @Description: 验证基本信息数据是否合法
		* @param currentProcessTaskStepVo
		* @return boolean
		 */
		public static boolean baseInfoValid(ProcessTaskStepVo currentProcessTaskStepVo, ProcessTaskVo processTaskVo) {
			JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
			if(processTaskVo.getTitle() == null) {
				throw new ProcessTaskRuntimeException("工单标题格式不能为空");
			}
			/* 标题不限制输入
			 * Pattern titlePattern = Pattern.compile("^[A-Za-z_\\d\\u4e00-\\u9fa5]+$"); if
			 * (!titlePattern.matcher(processTaskVo.getTitle()).matches()) { throw new
			 * ProcessTaskRuntimeException("工单标题格式不对"); }
			 */
			paramObj.put(ProcessTaskAuditDetailType.TITLE.getParamName(), processTaskVo.getTitle());
			if (StringUtils.isBlank(processTaskVo.getOwner())) {
				throw new ProcessTaskRuntimeException("工单请求人不能为空");
			}
			if (userMapper.getUserBaseInfoByUuid(processTaskVo.getOwner()) == null) {
				throw new ProcessTaskRuntimeException("工单请求人账号:'" + processTaskVo.getOwner() + "'不存在");
			}
			if (StringUtils.isBlank(processTaskVo.getPriorityUuid())) {
				throw new ProcessTaskRuntimeException("工单优先级不能为空");
			}
			List<ChannelPriorityVo> channelPriorityList = channelMapper.getChannelPriorityListByChannelUuid(processTaskVo.getChannelUuid());
			List<String> priorityUuidlist = new ArrayList<>(channelPriorityList.size());
			for (ChannelPriorityVo channelPriorityVo : channelPriorityList) {
				priorityUuidlist.add(channelPriorityVo.getPriorityUuid());
			}
			if (!priorityUuidlist.contains(processTaskVo.getPriorityUuid())) {
				throw new ProcessTaskRuntimeException("工单优先级与服务优先级级不匹配");
			}
			paramObj.put(ProcessTaskAuditDetailType.PRIORITY.getParamName(), processTaskVo.getPriorityUuid());

			// 获取上报描述内容
			List<Long> fileIdList = new ArrayList<>();
			List<ProcessTaskStepContentVo> processTaskStepContentList = processTaskMapper.getProcessTaskStepContentByProcessTaskStepId(currentProcessTaskStepVo.getId());
            for(ProcessTaskStepContentVo processTaskStepContent : processTaskStepContentList) {
                if (ProcessTaskOperationType.STARTPROCESS.getValue().equals(processTaskStepContent.getType())) {
                    paramObj.put(ProcessTaskAuditDetailType.CONTENT.getParamName(), selectContentByHashMapper.getProcessTaskContentStringByHash(processTaskStepContent.getContentHash()));
                    fileIdList = processTaskMapper.getFileIdListByContentId(processTaskStepContent.getId());
                    break;
                }
            }

			if (CollectionUtils.isNotEmpty(fileIdList)) {
				for (Long fileId : fileIdList) {
					if (fileMapper.getFileById(fileId) == null) {
						throw new ProcessTaskRuntimeException("上传附件uuid:'" + fileId + "'不存在");
					}
				}
				paramObj.put(ProcessTaskAuditDetailType.FILE.getParamName(), JSON.toJSONString(fileIdList));
			}
			currentProcessTaskStepVo.setParamObj(paramObj);
			return true;
		}
		/**
		 * 
		* @Time:2020年7月28日
		* @Description: 验证前置步骤指派处理人是否合法
		* @param currentProcessTaskStepVo
		* @return boolean
		 */
		public static boolean assignWorkerValid(ProcessTaskStepVo currentProcessTaskStepVo) {
			JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
			//前置步骤指派处理人
//			"assignWorkerList": [
//			             		{
//			             			"processTaskStepId": 1,
//									"processStepUuid": "abc",
//			             			"workerList": [
//			             				"user#xxx",
//			             				"team#xxx",
//			             				"role#xxx"
//			             			]
//			             		}
//			             	]
			Map<Long, List<String>> assignWorkerMap = new HashMap<>();
			JSONArray assignWorkerList = paramObj.getJSONArray("assignWorkerList");
			if(CollectionUtils.isNotEmpty(assignWorkerList)) {
				for(int i = 0; i < assignWorkerList.size(); i++) {
					JSONObject assignWorker = assignWorkerList.getJSONObject(i);
					Long processTaskStepId = assignWorker.getLong("processTaskStepId");
					if(processTaskStepId == null) {
						String processStepUuid = assignWorker.getString("processStepUuid");
						if(processStepUuid != null) {
							ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskIdAndProcessStepUuid(currentProcessTaskStepVo.getProcessTaskId(), processStepUuid);
							if(processTaskStepVo != null) {
								processTaskStepId = processTaskStepVo.getId();
							}
						}
					}
					if(processTaskStepId != null) {
						assignWorkerMap.put(processTaskStepId, JSON.parseArray(assignWorker.getString("workerList"), String.class));					
					}
				}
			}
			
			//获取可分配处理人的步骤列表				
			ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo = new ProcessTaskStepWorkerPolicyVo();
			processTaskStepWorkerPolicyVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			List<ProcessTaskStepWorkerPolicyVo> processTaskStepWorkerPolicyList = processTaskMapper.getProcessTaskStepWorkerPolicy(processTaskStepWorkerPolicyVo);
			if(CollectionUtils.isNotEmpty(processTaskStepWorkerPolicyList)) {
				for(ProcessTaskStepWorkerPolicyVo workerPolicyVo : processTaskStepWorkerPolicyList) {
					if(WorkerPolicy.PRESTEPASSIGN.getValue().equals(workerPolicyVo.getPolicy())) {
						List<String> processStepUuidList = JSON.parseArray(workerPolicyVo.getConfigObj().getString("processStepUuidList"), String.class);
						for(String processStepUuid : processStepUuidList) {
							if(currentProcessTaskStepVo.getProcessStepUuid().equals(processStepUuid)) {
								List<ProcessTaskStepUserVo> majorList = processTaskMapper.getProcessTaskStepUserByStepId(workerPolicyVo.getProcessTaskStepId(), ProcessUserType.MAJOR.getValue());
								if(CollectionUtils.isEmpty(majorList)) {
									ProcessTaskAssignWorkerVo assignWorkerVo = new ProcessTaskAssignWorkerVo();
									assignWorkerVo.setProcessTaskId(workerPolicyVo.getProcessTaskId());
									assignWorkerVo.setProcessTaskStepId(workerPolicyVo.getProcessTaskStepId());
									assignWorkerVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
									assignWorkerVo.setFromProcessStepUuid(currentProcessTaskStepVo.getProcessStepUuid());
									processTaskMapper.deleteProcessTaskAssignWorker(assignWorkerVo);
									List<String> workerList = assignWorkerMap.get(workerPolicyVo.getProcessTaskStepId());
									if(CollectionUtils.isNotEmpty(workerList)) {
										for(String worker : workerList) {
											String[] split = worker.split("#");
											assignWorkerVo.setType(split[0]);
											assignWorkerVo.setUuid(split[1]);
											processTaskMapper.insertProcessTaskAssignWorker(assignWorkerVo);
										}
									}else {
										Integer isRequired = workerPolicyVo.getConfigObj().getInteger("isRequired");
										if(isRequired != null && isRequired.intValue() == 1) {
											ProcessTaskStepVo assignableWorkerStep = processTaskMapper.getProcessTaskStepBaseInfoById(workerPolicyVo.getProcessTaskStepId());
											throw new ProcessTaskRuntimeException("指派：" + assignableWorkerStep.getName() + "步骤处理人是必填");
										}
									}
								}
							}
						}
					}
				}
			}
			return true;
		}
	}

}
