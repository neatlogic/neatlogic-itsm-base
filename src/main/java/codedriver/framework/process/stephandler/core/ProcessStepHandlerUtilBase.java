package codedriver.framework.process.stephandler.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
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
import codedriver.framework.notify.core.INotifyTriggerType;
import codedriver.framework.notify.dao.mapper.NotifyMapper;
import codedriver.framework.notify.dto.NotifyPolicyVo;
import codedriver.framework.notify.dto.NotifyReceiverVo;
import codedriver.framework.notify.dto.ParamMappingVo;
import codedriver.framework.process.audithandler.core.IProcessTaskAuditDetailType;
import codedriver.framework.process.audithandler.core.IProcessTaskAuditType;
import codedriver.framework.process.audithandler.core.ProcessTaskAuditDetailTypeFactory;
import codedriver.framework.process.column.core.ProcessTaskUtil;
import codedriver.framework.process.constvalue.ProcessFieldType;
import codedriver.framework.process.constvalue.ProcessTaskAuditDetailType;
import codedriver.framework.process.constvalue.ProcessTaskAuditType;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.constvalue.WorkerPolicy;
import codedriver.framework.process.dao.mapper.CatalogMapper;
import codedriver.framework.process.dao.mapper.ChannelMapper;
import codedriver.framework.process.dao.mapper.FormMapper;
import codedriver.framework.process.dao.mapper.PriorityMapper;
import codedriver.framework.process.dao.mapper.ProcessMapper;
import codedriver.framework.process.dao.mapper.ProcessStepHandlerMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskStepTimeAuditMapper;
import codedriver.framework.process.dao.mapper.SelectContentByHashMapper;
import codedriver.framework.process.dao.mapper.WorktimeMapper;
import codedriver.framework.process.dao.mapper.score.ProcessTaskScoreMapper;
import codedriver.framework.process.dao.mapper.score.ScoreTemplateMapper;
import codedriver.framework.process.dto.ActionVo;
import codedriver.framework.process.dto.ChannelPriorityVo;
import codedriver.framework.process.dto.FormAttributeVo;
import codedriver.framework.process.dto.FormVersionVo;
import codedriver.framework.process.dto.ProcessStepHandlerVo;
import codedriver.framework.process.dto.ProcessTaskAssignWorkerVo;
import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskFormAttributeDataVo;
import codedriver.framework.process.dto.ProcessTaskFormVo;
import codedriver.framework.process.dto.ProcessTaskSlaNotifyVo;
import codedriver.framework.process.dto.ProcessTaskSlaTimeVo;
import codedriver.framework.process.dto.ProcessTaskSlaTransferVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditDetailVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepContentVo;
import codedriver.framework.process.dto.ProcessTaskStepNotifyPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepTimeAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerPolicyVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
import codedriver.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import codedriver.framework.process.integration.handler.ProcessRequestFrom;
import codedriver.framework.process.notify.core.TaskStepNotifyTriggerType;
import codedriver.framework.process.notify.schedule.plugin.ProcessTaskSlaNotifyJob;
import codedriver.framework.process.notify.schedule.plugin.ProcessTaskSlaTransferJob;
import codedriver.framework.process.score.schedule.plugin.ProcessTaskAutoScoreJob;
import codedriver.framework.process.util.WorkTimeUtil;
import codedriver.framework.scheduler.core.IJob;
import codedriver.framework.scheduler.core.SchedulerManager;
import codedriver.framework.scheduler.dto.JobObject;
import codedriver.framework.scheduler.exception.ScheduleHandlerNotFoundException;
import codedriver.framework.transaction.util.TransactionUtil;
import codedriver.framework.util.ConditionUtil;
import codedriver.framework.util.JSONUtil;
import codedriver.framework.util.NotifyPolicyUtil;
import codedriver.framework.util.RunScriptUtil;

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
	protected static ProcessTaskScoreMapper processTaskScoreMapper;
	private static TransactionUtil transactionUtil;
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
	public void setProcessTaskScoreMapper(ProcessTaskScoreMapper _processTaskScoreMapper) {
		processTaskScoreMapper = _processTaskScoreMapper;
	}
	
	@Autowired
	public void settransactionUtil(TransactionUtil _transactionUtil) {
	    transactionUtil = _transactionUtil;
	}

	protected static class ActionHandler extends CodeDriverThread {

		private ProcessTaskStepVo currentProcessTaskStepVo;
		private INotifyTriggerType triggerType;

		public ActionHandler(ProcessTaskStepVo _currentProcessTaskStepVo, INotifyTriggerType _trigger) {
			currentProcessTaskStepVo = _currentProcessTaskStepVo;
			triggerType = _trigger;
			if (_currentProcessTaskStepVo != null) {
				this.setThreadName("PROCESSTASK-ACTION-" + _currentProcessTaskStepVo.getId());
			}
		}

		protected static void action(ProcessTaskStepVo currentProcessTaskStepVo, INotifyTriggerType trigger) {
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
				JSONArray actionList = JSONUtil.getJSONArray(stepConfig, "actionConfig.actionList");
				if(CollectionUtils.isEmpty(actionList)) {
				    IProcessStepUtilHandler processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(stepVo.getHandler());
	                if(processStepUtilHandler == null) {
	                    throw new ProcessStepUtilHandlerNotFoundException(stepVo.getHandler());
	                }
	                ProcessStepHandlerVo processStepHandlerVo = processStepHandlerMapper.getProcessStepHandlerByHandler(stepVo.getHandler());
	                JSONObject globalConfig = processStepUtilHandler.makeupConfig(processStepHandlerVo != null ? processStepHandlerVo.getConfig() : null);
	                actionList = JSONUtil.getJSONArray(globalConfig, "actionConfig.actionList");
				}
                
				/** 从步骤配置信息中获取动作列表 **/
//				JSONArray actionList = stepVo.getActionList();
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
		private INotifyTriggerType notifyTriggerType;

		public NotifyHandler(ProcessTaskStepVo _currentProcessTaskStepVo, INotifyTriggerType _trigger) {
			currentProcessTaskStepVo = _currentProcessTaskStepVo;
			notifyTriggerType = _trigger;
			if (_currentProcessTaskStepVo != null) {
				this.setThreadName("PROCESSTASK-NOTIFY-" + _currentProcessTaskStepVo.getId());
			}
		}

		protected static void notify(ProcessTaskStepVo currentProcessTaskStepVo, INotifyTriggerType trigger) {
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
			    IProcessStepUtilHandler processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler();
			    JSONObject notifyPolicyConfig = null;
			    if(notifyTriggerType instanceof TaskStepNotifyTriggerType) {
			        /** 获取工单配置信息 **/
			        ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(currentProcessTaskStepVo.getProcessTaskId());
			        String config = selectContentByHashMapper.getProcessTaskConfigStringByHash(processTaskVo.getConfigHash());
			        notifyPolicyConfig = JSONUtil.getJSONObject(config, "process.processConfig.notifyPolicyConfig");
			    }else {
			        /** 获取步骤配置信息 **/
	                ProcessTaskStepVo stepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
	                processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(stepVo.getHandler());
	                if(processStepUtilHandler == null) {
	                    throw new ProcessStepUtilHandlerNotFoundException(stepVo.getHandler());
	                }
	                String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(stepVo.getConfigHash());
	                notifyPolicyConfig = JSONUtil.getJSONObject(stepConfig, "notifyPolicyConfig");
	                if(MapUtils.isEmpty(notifyPolicyConfig)) {
	                    ProcessStepHandlerVo processStepHandlerVo = processStepHandlerMapper.getProcessStepHandlerByHandler(stepVo.getHandler());
	                    JSONObject globalConfig = processStepUtilHandler.makeupConfig(processStepHandlerVo != null ? processStepHandlerVo.getConfig() : null);
	                    notifyPolicyConfig = JSONUtil.getJSONObject(globalConfig, "notifyPolicyConfig");
	                }
			    }				

				/** 从步骤配置信息中获取通知策略信息 **/
				if (MapUtils.isNotEmpty(notifyPolicyConfig)) {
					Long policyId = notifyPolicyConfig.getLong("policyId");
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
							JSONObject templateParamData = ProcessTaskUtil.getProcessTaskParamData(processTaskVo);
							Map<String, List<NotifyReceiverVo>> receiverMap = new HashMap<>();
							processStepUtilHandler.getReceiverMap(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), receiverMap);
		                    /** 参数映射列表**/
		                    List<ParamMappingVo> paramMappingList = JSON.parseArray(JSON.toJSONString(notifyPolicyConfig.getJSONArray("paramMappingList")), ParamMappingVo.class);
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
		private ProcessTaskVo currentProcessTaskVo;
		public SlaHandler(ProcessTaskVo _currentProcessTaskVo, ProcessTaskStepVo _currentProcessTaskStepVo) {
			currentProcessTaskVo = _currentProcessTaskVo;
			currentProcessTaskStepVo = _currentProcessTaskStepVo;
			if (_currentProcessTaskStepVo != null) {
				this.setThreadName("PROCESSTASK-SLA-" + _currentProcessTaskStepVo.getId());
			}else if(_currentProcessTaskVo != null) {
			    this.setThreadName("PROCESSTASK-SLA-" + _currentProcessTaskVo.getId());
			}
		}

		private static long getTimeCost(List<ProcessTaskStepTimeAuditVo> processTaskStepTimeAuditList, long currentTimeMillis, String worktimeUuid) {
			List<Map<String, Long>> timeList = new ArrayList<>();
			for (ProcessTaskStepTimeAuditVo auditVo : processTaskStepTimeAuditList) {
			    //System.out.println(auditVo);
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
				}else if(startTime != null) {
				    Map<String, Long> stimeMap = new HashMap<>();
                    stimeMap.put("s", startTime);
                    timeList.add(stimeMap);
                    Map<String, Long> etimeMap = new HashMap<>();
                    etimeMap.put("e", currentTimeMillis);
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
			    //System.out.println(timeMap);
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

		private static long getRealTimeCost(List<ProcessTaskStepTimeAuditVo> processTaskStepTimeAuditList, long currentTimeMillis) {
			int timeCost = 0;
			if (CollectionUtils.isNotEmpty(processTaskStepTimeAuditList)) {
				List<Map<String, Long>> timeZoneList = new ArrayList<>();
				for (ProcessTaskStepTimeAuditVo auditVo : processTaskStepTimeAuditList) {
				    //System.out.println(auditVo);
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
					}else if(startTime != null) {
					    Map<String, Long> smap = new HashMap<>();
                        smap.put("s", startTime);
                        timeZoneList.add(smap);
                        Map<String, Long> emap = new HashMap<>();
                        emap.put("e", currentTimeMillis);
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
				    //System.out.println(timeMap);
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
			//System.out.println("timeCost:" + timeCost);
			return timeCost;
		}
		protected static void calculate(ProcessTaskVo currentProcessTaskVo, boolean isAsync) {
            calculate(currentProcessTaskVo, null, isAsync);
        }
		protected static void calculate(ProcessTaskVo currentProcessTaskVo) {
		    calculate(currentProcessTaskVo, null, true);
		}
		protected static void calculate(ProcessTaskStepVo currentProcessTaskStepVo) {
		    calculate(null, currentProcessTaskStepVo, true);
		}
		protected static void calculate(ProcessTaskVo currentProcessTaskVo, ProcessTaskStepVo currentProcessTaskStepVo, boolean isAsync) {
		    if(!isAsync) {
		        new SlaHandler(currentProcessTaskVo, currentProcessTaskStepVo).execute();
		    }
			if (!TransactionSynchronizationManager.isSynchronizationActive()) {
				CachedThreadPool.execute(new SlaHandler(currentProcessTaskVo, currentProcessTaskStepVo));
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
				handlerList.add(new SlaHandler(currentProcessTaskVo, currentProcessTaskStepVo));
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

		private Long getSlaTimeSumBySlaConfig(JSONObject slaConfigObj, ProcessTaskVo processTaskVo) {
		    JSONArray policyList = slaConfigObj.getJSONArray("calculatePolicyList");
            if (CollectionUtils.isNotEmpty(policyList)) {
                for (int i = 0; i < policyList.size(); i++) {
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
                        if (enablePriority == 0) {
                            return getRealtime(time, unit);
                        } else {//关联优先级
                            if (CollectionUtils.isNotEmpty(priorityList)) {
                                for (int p = 0; p < priorityList.size(); p++) {
                                    JSONObject priorityObj = priorityList.getJSONObject(p);
                                    if (priorityObj.getString("priorityUuid").equals(processTaskVo.getPriorityUuid())) {
                                        return getRealtime(priorityObj.getIntValue("time"), priorityObj.getString("unit"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
		}
		
		private void calculateExpireTime(ProcessTaskSlaTimeVo slaTimeVo, String worktimeUuid) {
		    long realTimeCost = 0;
            long timeCost = 0;

            long currentTimeMillis = System.currentTimeMillis();
            List<ProcessTaskStepTimeAuditVo> processTaskStepTimeAuditList = processTaskStepTimeAuditMapper.getProcessTaskStepTimeAuditBySlaId(slaTimeVo.getSlaId());
            if(CollectionUtils.isNotEmpty(processTaskStepTimeAuditList)) {
                // 非第一次进入，进行时间扣减
                realTimeCost = getRealTimeCost(processTaskStepTimeAuditList, currentTimeMillis);
                timeCost = realTimeCost;
                if (StringUtils.isNotBlank(worktimeUuid)) {// 如果有工作时间，则计算实际消耗的工作时间
                    timeCost = getTimeCost(processTaskStepTimeAuditList, currentTimeMillis, worktimeUuid);
                }
            }
            
            slaTimeVo.setRealTimeLeft(slaTimeVo.getTimeSum() - realTimeCost);
            slaTimeVo.setTimeLeft(slaTimeVo.getTimeSum() - timeCost);
            
            slaTimeVo.setRealExpireTime(new Date(currentTimeMillis + slaTimeVo.getRealTimeLeft()));
            if (StringUtils.isNotBlank(worktimeUuid)) {
                if (slaTimeVo.getTimeLeft() != null) {
                    if(slaTimeVo.getTimeLeft() > 0) {
                        long expireTime = WorkTimeUtil.calculateExpireTime(currentTimeMillis, slaTimeVo.getTimeLeft(), worktimeUuid);
                        slaTimeVo.setExpireTime(new Date(expireTime));
                    }else {
                        long expireTime = WorkTimeUtil.calculateExpireTimeForTimedOut(currentTimeMillis, -slaTimeVo.getTimeLeft(), worktimeUuid);
                        slaTimeVo.setExpireTime(new Date(expireTime));
                    }
                    
                } else {
                    throw new RuntimeException("计算剩余时间失败");
                }
            } else {
                if (slaTimeVo.getTimeLeft() != null) {
                    slaTimeVo.setExpireTime(new Date(currentTimeMillis + slaTimeVo.getTimeLeft()));
                } else {
                    throw new RuntimeException("计算剩余时间失败");
                }
            }
            //System.out.println(slaTimeVo);
		}
		
		private void adjustJob(ProcessTaskSlaTimeVo slaTimeVo, JSONObject slaConfigObj, Date oldExpireTime) {
		    /** 有超时时间点 **/
            if (slaTimeVo.getExpireTime() != null) {
                /** 是否需要启动作业 **/
                boolean isStartJob = false;
                List<Long> processTaskStepIdList = processTaskMapper.getProcessTaskStepIdListBySlaId(slaTimeVo.getSlaId());
                if(CollectionUtils.isNotEmpty(processTaskStepIdList)) {
                    List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepListByIdList(processTaskStepIdList);
                    for(ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
                        if(Objects.equals(processTaskStepVo.getIsActive(), 1)) {
                            if(ProcessTaskStatus.PENDING.getValue().equals(processTaskStepVo.getStatus()) || ProcessTaskStatus.RUNNING.getValue().equals(processTaskStepVo.getStatus())) {
                                isStartJob = true;
                                break;
                            }
                        }
                    }
                }
                /** 作业是否已启动 **/
                boolean jobStarted = false;
                List<ProcessTaskSlaNotifyVo> processTaskSlaNotifyList = processTaskMapper.getProcessTaskSlaNotifyBySlaId(slaTimeVo.getSlaId());
                List<ProcessTaskSlaTransferVo> processTaskSlaTransferList = processTaskMapper.getProcessTaskSlaTransferBySlaId(slaTimeVo.getSlaId());
                if(CollectionUtils.isNotEmpty(processTaskSlaNotifyList) || CollectionUtils.isNotEmpty(processTaskSlaTransferList)) {
                    jobStarted = true;
                }
                if(jobStarted) {
                    if(!isStartJob || !slaTimeVo.getExpireTime().equals(oldExpireTime)) {
                        processTaskMapper.deleteProcessTaskSlaTransferBySlaId(slaTimeVo.getSlaId());
                        processTaskMapper.deleteProcessTaskSlaNotifyBySlaId(slaTimeVo.getSlaId());
                        jobStarted = false;
//                        for(ProcessTaskSlaNotifyVo processTaskSlaNotifyVo : processTaskSlaNotifyList) {
//                            processTaskMapper.deleteProcessTaskSlaNotifyById(processTaskSlaNotifyVo.getId());
//                        }
//                        for(ProcessTaskSlaTransferVo processTaskSlaTransferVo : processTaskSlaTransferList) {
//                            processTaskMapper.deleteProcessTaskSlaTransferById(processTaskSlaTransferVo.getId());
//                        }
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
                            processTaskSlaNotifyVo.setSlaId(slaTimeVo.getSlaId());
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
                            processTaskSlaTransferVo.setSlaId(slaTimeVo.getSlaId());
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

		@Override
		protected void execute() {
		    TransactionStatus transactionStatus = transactionUtil.openTx();
		    try {
		        Long processTaskId = null;
	            List<Long> slaIdList = null;
	            if(currentProcessTaskStepVo != null) {
	                slaIdList = processTaskMapper.getSlaIdListByProcessTaskStepId(currentProcessTaskStepVo.getId());
	                processTaskId = currentProcessTaskStepVo.getProcessTaskId();
	            }else if(currentProcessTaskVo != null){
	                slaIdList = processTaskMapper.getSlaIdListByProcessTaskId(currentProcessTaskVo.getId());
	                Iterator<Long> iterator = slaIdList.iterator();
	                while(iterator.hasNext()) {
	                    Long slaId = iterator.next();
	                    if(processTaskMapper.getProcessTaskSlaTimeBySlaId(slaId) == null) {
	                        List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepBaseInfoBySlaId(slaId);
	                        Iterator<ProcessTaskStepVo> it = processTaskStepList.iterator();
	                        while (it.hasNext()) {
	                            ProcessTaskStepVo processTaskStepVo = it.next();
	                            // 未处理、处理中和挂起的步骤才需要计算SLA
	                            if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.PENDING.getValue())) {
	                                continue;
	                            }else if(processTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
	                                continue;
	                            }else if(processTaskStepVo.getStatus().equals(ProcessTaskStatus.HANG.getValue())){
	                                continue;
	                            }	                            
	                            it.remove();
	                        }
	                        if(CollectionUtils.isEmpty(processTaskStepList)) {
	                            iterator.remove();
	                        }
	                    }
	                }
	                processTaskId = currentProcessTaskVo.getId();
	            }
	                
	            if (CollectionUtils.isNotEmpty(slaIdList)) {
	                ProcessTaskVo processTaskVo = ProcessStepUtilHandlerFactory.getHandler().getProcessTaskDetailById(processTaskId);
	                processTaskVo.setCurrentProcessTaskStep(currentProcessTaskStepVo);
	                String worktimeUuid = processTaskVo.getWorktimeUuid();
	                for (Long slaId : slaIdList) {
	                    processTaskMapper.getProcessTaskSlaLockById(slaId);
	                    String config = processTaskMapper.getProcessTaskSlaConfigById(slaId);
	                    JSONObject slaConfigObj = JSON.parseObject(config);
	                    if(MapUtils.isNotEmpty(slaConfigObj)) {
	                        /** 旧的超时时间点 **/
	                        Date oldExpireTime = null;
	                        Long oldTimeSum = null;
	                        boolean isSlaTimeExists = false;
	                        /** 如果没有超时时间，证明第一次进入SLA标签范围，开始计算超时时间 **/
	                        ProcessTaskSlaTimeVo oldSlaTimeVo = processTaskMapper.getProcessTaskSlaTimeBySlaId(slaId);
	                        if(oldSlaTimeVo != null) {
	                            /** 记录旧的超时时间点 **/
	                            oldExpireTime = oldSlaTimeVo.getExpireTime();
	                            oldTimeSum = oldSlaTimeVo.getTimeSum();
	                            isSlaTimeExists = true;
	                        }
	                        Long timeSum = getSlaTimeSumBySlaConfig(slaConfigObj, processTaskVo);

	                        // 修正最终超时日期
	                        if (timeSum != null) {
	                            ProcessTaskSlaTimeVo slaTimeVo = new ProcessTaskSlaTimeVo();
	                            slaTimeVo.setTimeSum(timeSum);
	                            slaTimeVo.setSlaId(slaId);
	                            calculateExpireTime(slaTimeVo, worktimeUuid);
	                            if(Objects.equals(timeSum, oldTimeSum) && Objects.equals(slaTimeVo.getExpireTime(), oldExpireTime)) {
	                                return;
	                            }
	                            slaTimeVo.setProcessTaskId(processTaskId);
	                            if (isSlaTimeExists) {
	                                processTaskMapper.updateProcessTaskSlaTime(slaTimeVo);
	                            } else {
	                                processTaskMapper.insertProcessTaskSlaTime(slaTimeVo);
	                            }
	                            adjustJob(slaTimeVo, slaConfigObj, oldExpireTime);
	                        }else if(isSlaTimeExists){
	                            processTaskMapper.deleteProcessTaskSlaTimeBySlaId(slaId);
	                            processTaskMapper.deleteProcessTaskSlaTransferBySlaId(slaId);
	                            processTaskMapper.deleteProcessTaskSlaNotifyBySlaId(slaId);
	                        }
	                    }
	                }
	            }
		        transactionUtil.commitTx(transactionStatus);
		    }catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            transactionUtil.rollbackTx(transactionStatus);
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
                    processTaskStepTimeAuditMapper.updateProcessTaskStepTimeAudit(newAuditVo);
                }
                break;
			}
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
				processTaskStepAuditVo.setOriginalUser(currentProcessTaskStepVo.getOriginalUser());
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
			 * 先检查是否设置自动评分
			 * 如果设置了自动评分，则启动定时器监听工单是否评分，若超时未评分，则系统自动评分
			 */
			ProcessTaskVo task = processTaskMapper.getProcessTaskById(currentProcessTaskVo.getId());
			Integer isAuto = null;
			Integer autoTime = null;
			if(task != null){
				String configHash = task.getConfigHash();
				ProcessTaskConfigVo taskConfigVo = null;
				JSONObject scoreConfig = null;
				if(StringUtils.isNotBlank(configHash) && (taskConfigVo = selectContentByHashMapper.getProcessTaskConfigByHash(configHash)) != null){
					JSONObject taskConfigObj = JSONObject.parseObject(taskConfigVo.getConfig());
					JSONObject processConfig = taskConfigObj.getJSONObject("process");
					if(MapUtils.isNotEmpty(processConfig) && MapUtils.isNotEmpty(scoreConfig = processConfig.getJSONObject("scoreConfig")) && MapUtils.isNotEmpty(scoreConfig.getJSONObject("config"))){
						isAuto = scoreConfig.getInteger("isAuto");
						autoTime = scoreConfig.getJSONObject("config").getInteger("autoTime");
					}
				}
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
//	                Map<String, String> formAttributeActionMap = new HashMap<>();
//	                List<ProcessTaskStepFormAttributeVo> processTaskStepFormAttributeList = processTaskMapper.getProcessTaskStepFormAttributeByProcessTaskStepId(currentProcessTaskStepVo.getId());
//	                for (ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo : processTaskStepFormAttributeList) {
//	                    formAttributeActionMap.put(processTaskStepFormAttributeVo.getAttributeUuid(), processTaskStepFormAttributeVo.getAction());
//	                }
	                currentProcessTaskStepVo.setFormAttributeDataMap(formAttributeDataMap);
	                currentProcessTaskStepVo.setFormAttributeVoList(formAttributeList);
//	                currentProcessTaskStepVo.setFormAttributeActionMap(formAttributeActionMap);
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
//		    Map<String, String> formAttributeActionMap = currentProcessTaskStepVo.getFormAttributeActionMap();
            List<String> hidecomponentList = JSON.parseArray(JSON.toJSONString(currentProcessTaskStepVo.getParamObj().getJSONArray("hidecomponentList")), String.class);           
            List<String> readcomponentList = JSON.parseArray(JSON.toJSONString(currentProcessTaskStepVo.getParamObj().getJSONArray("readcomponentList")), String.class);           
            for (FormAttributeVo formAttributeVo : currentProcessTaskStepVo.getFormAttributeVoList()) {
                if (!formAttributeVo.isRequired()) {
                    continue;
                }
//                if(formAttributeActionMap.containsKey(formAttributeVo.getUuid()) || formAttributeActionMap.containsKey("all")) {
//                    continue;
//                }
                if (CollectionUtils.isNotEmpty(hidecomponentList) && hidecomponentList.contains(formAttributeVo.getUuid())) {
                    continue;
                }
                if (CollectionUtils.isNotEmpty(readcomponentList) && readcomponentList.contains(formAttributeVo.getUuid())) {
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
