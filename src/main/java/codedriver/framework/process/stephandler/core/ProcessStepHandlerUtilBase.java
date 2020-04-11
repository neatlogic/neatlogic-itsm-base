package codedriver.framework.process.stephandler.core;

import java.text.SimpleDateFormat;
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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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
import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.asynchronization.threadpool.CachedThreadPool;
import codedriver.framework.asynchronization.threadpool.CommonThreadPool;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.constvalue.UserType;
import codedriver.framework.dao.mapper.TeamMapper;
import codedriver.framework.dao.mapper.UserMapper;
import codedriver.framework.file.dao.mapper.FileMapper;
import codedriver.framework.process.constvalue.ProcessStepMode;
import codedriver.framework.process.constvalue.ProcessStepType;
import codedriver.framework.process.constvalue.ProcessTaskAuditDetailType;
import codedriver.framework.process.constvalue.ProcessTaskGroupSearch;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessTaskStepAction;
import codedriver.framework.process.constvalue.ProcessTaskStepUserStatus;
import codedriver.framework.process.constvalue.ProcessTaskStepWorkerAction;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dao.mapper.ChannelMapper;
import codedriver.framework.process.dao.mapper.FormMapper;
import codedriver.framework.process.dao.mapper.ProcessMapper;
import codedriver.framework.process.dao.mapper.ProcessStepHandlerMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskAuditMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskStepTimeAuditMapper;
import codedriver.framework.process.dao.mapper.WorktimeMapper;
import codedriver.framework.process.dao.mapper.notify.NotifyMapper;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.FormAttributeVo;
import codedriver.framework.process.dto.FormVersionVo;
import codedriver.framework.process.dto.ProcessStepHandlerVo;
import codedriver.framework.process.dto.ProcessTaskFormAttributeDataVo;
import codedriver.framework.process.dto.ProcessTaskFormVo;
import codedriver.framework.process.dto.ProcessTaskSlaNotifyVo;
import codedriver.framework.process.dto.ProcessTaskSlaTimeVo;
import codedriver.framework.process.dto.ProcessTaskSlaTransferVo;
import codedriver.framework.process.dto.ProcessTaskSlaVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditDetailVo;
import codedriver.framework.process.dto.ProcessTaskStepAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepFormAttributeVo;
import codedriver.framework.process.dto.ProcessTaskStepTimeAuditVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.dto.WorktimeRangeVo;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
import codedriver.framework.process.exception.notify.NotifyHandlerNotFoundException;
import codedriver.framework.process.exception.process.ProcessStepHandlerNotFoundException;
import codedriver.framework.process.exception.processtask.ProcessTaskNoPermissionException;
import codedriver.framework.process.exception.worktime.WorktimeNotFoundException;
import codedriver.framework.process.formattribute.core.FormAttributeHandlerFactory;
import codedriver.framework.process.formattribute.core.IFormAttributeHandler;
import codedriver.framework.process.notify.core.INotifyHandler;
import codedriver.framework.process.notify.core.NotifyHandlerFactory;
import codedriver.framework.process.notify.core.NotifyTriggerType;
import codedriver.framework.process.notify.dto.NotifyTemplateVo;
import codedriver.framework.process.notify.dto.NotifyVo;
import codedriver.framework.process.notify.schedule.plugin.ProcessTaskSlaNotifyJob;
import codedriver.framework.process.notify.schedule.plugin.ProcessTaskSlaTransferJob;
import codedriver.framework.scheduler.core.IJob;
import codedriver.framework.scheduler.core.SchedulerManager;
import codedriver.framework.scheduler.dto.JobObject;
import codedriver.framework.scheduler.exception.ScheduleHandlerNotFoundException;

public abstract class ProcessStepHandlerUtilBase {
	static Logger logger = LoggerFactory.getLogger(ProcessStepHandlerUtilBase.class);

	private static final ThreadLocal<List<AuditHandler>> AUDIT_HANDLERS = new ThreadLocal<>();
	private static final ThreadLocal<List<SlaHandler>> SLA_HANDLERS = new ThreadLocal<>();
	private static final ThreadLocal<List<NotifyHandler>> NOTIFY_HANDLERS = new ThreadLocal<>();
	protected static ProcessMapper processMapper;
	protected static ProcessTaskMapper processTaskMapper;
	protected static ProcessTaskAuditMapper processTaskAuditMapper;
	protected static FormMapper formMapper;
	protected static UserMapper userMapper;
	protected static ProcessTaskStepTimeAuditMapper processTaskStepTimeAuditMapper;
	private static WorktimeMapper worktimeMapper;
	protected static ChannelMapper channelMapper;
	private static NotifyMapper notifyMapper;
	protected static FileMapper fileMapper;
	protected static TeamMapper teamMapper;
	protected static ProcessStepHandlerMapper processStepHandlerMapper;
	// private static SchedulerManager schedulerManager;

	// @Autowired
	// public void setSchedulerManager(SchedulerManager _schedulerManager) {
	// schedulerManager = _schedulerManager;
	// }

	@Autowired
	public void setProcessMapper(ProcessMapper _processMapper) {
		processMapper = _processMapper;
	}

	@Autowired
	public void setProcessTaskMapper(ProcessTaskMapper _processTaskMapper) {
		processTaskMapper = _processTaskMapper;
	}

	@Autowired
	public void setProcessTaskAuditMapper(ProcessTaskAuditMapper _processTaskAuditMapper) {
		processTaskAuditMapper = _processTaskAuditMapper;
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
				ProcessTaskStepVo stepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
				ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(currentProcessTaskStepVo.getProcessTaskId());
				List<ProcessTaskStepWorkerVo> workerList = null;
				if (StringUtils.isNotBlank(stepVo.getConfigHash())) {
					String stepConfig = processTaskMapper.getProcessTaskStepConfigByHash(stepVo.getConfigHash());
					JSONObject stepConfigObj = null;
					stepConfigObj = JSONObject.parseObject(stepConfig);

					if (stepConfigObj != null && stepConfigObj.containsKey("notifyList")) {
						JSONArray notifyList = stepConfigObj.getJSONArray("notifyList");
						for (int i = 0; i < notifyList.size(); i++) {
							JSONObject notifyObj = notifyList.getJSONObject(i);
							String trigger = notifyObj.getString("trigger");
							String type = notifyObj.getString("type");
							String templateUuid = notifyObj.getString("template");
							JSONArray receiverList = notifyObj.getJSONArray("receiverList");
							if (receiverList != null && receiverList.size() > 0 && notifyTriggerType.getTrigger().equalsIgnoreCase(trigger)) {
								INotifyHandler handler = NotifyHandlerFactory.getHandler(type);
								if (handler != null) {
									NotifyVo.Builder notifyBuilder = new NotifyVo.Builder(notifyTriggerType);
									if (StringUtils.isNotBlank(templateUuid)) {
										NotifyTemplateVo notifyTemplateVo = notifyMapper.getNotifyTemplateByUuid(templateUuid);
										if (notifyTemplateVo != null) {
											notifyBuilder.withContentTemplate(notifyTemplateVo.getContent());
											notifyBuilder.withTitleTemplate(notifyTemplateVo.getTitle());
										}
									}

									/** 注入流程作业信息 不够将来再补充 **/
									notifyBuilder.addData("task", processTaskVo).addData("step", stepVo);
									/** 注入结束 **/

									for (int u = 0; u < receiverList.size(); u++) {
										String worker = receiverList.getString(u);
										if (worker.startsWith("common.")) {
											worker = worker.substring(7);
											if (workerList == null) {
												workerList = processTaskMapper.getProcessTaskStepWorkerByProcessTaskStepId(currentProcessTaskStepVo.getId());
											}
											if (worker.equalsIgnoreCase("reporter")) {
												notifyBuilder.addUserId(processTaskVo.getReporter());
											} else if (worker.equalsIgnoreCase("owner")) {
												notifyBuilder.addUserId(processTaskVo.getOwner());
											} else if (worker.equalsIgnoreCase("worker")) {
												for (ProcessTaskStepWorkerVo workerVo : workerList) {
													if(GroupSearch.USER.getValue().equals(workerVo.getType())) {
														notifyBuilder.addUserId(workerVo.getUuid());
													}
												}
											}
										} else if (worker.startsWith("user.")) {
											worker = worker.substring(5);
											notifyBuilder.addUserId(worker);
										} else if (worker.startsWith("team.")) {
											worker = worker.substring(5);
											notifyBuilder.addTeamId(worker);
										}
									}
									NotifyVo notifyVo = notifyBuilder.build();
									handler.execute(notifyVo);
								} else {
									throw new NotifyHandlerNotFoundException(type);
								}
							}
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

		private static final ScriptEngineManager sem = new ScriptEngineManager();

		private boolean validateRule(JSONArray ruleList, String connectionType) {
			if (ruleList != null && ruleList.size() > 0) {
				ScriptEngine se = sem.getEngineByName("nashorn");

				JSONObject paramObj = new JSONObject();
				List<ProcessTaskFormAttributeDataVo> formAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
				String script = "";
				for (int i = 0; i < ruleList.size(); i++) {
					JSONObject ruleObj = ruleList.getJSONObject(i);
					String key = ruleObj.getString("key");
					String value = null;
					String compareValue = ruleObj.getString("value");
					String expression = ruleObj.getString("expression");
					if (key.startsWith("form.")) {
						for (ProcessTaskFormAttributeDataVo attributeData : formAttributeDataList) {
							if (attributeData.getAttributeUuid().equals(key.substring(5))) {
								IFormAttributeHandler handler = FormAttributeHandlerFactory.getHandler(attributeData.getType());
								if (handler != null) {
									//configObj是表单属性配置，暂时用空JSONObject对象代替
									JSONObject configObj = new JSONObject();
									value = handler.getValue(attributeData, configObj);
								}
								break;
							}
						}
					}
					if (StringUtils.isNotBlank(value)) {
						paramObj.put(key, value);
					} else {
						paramObj.put(key, "");
					}
					if (StringUtils.isNotBlank(script)) {
						if (connectionType.equalsIgnoreCase("and")) {
							script += " && ";
						} else {
							script += " || ";
						}
					}
					script += "json['" + key + "'] " + expression + " '" + compareValue + "'";
				}
				se.put("json", paramObj);
				try {
					return Boolean.parseBoolean(se.eval(script).toString());
				} catch (ScriptException e) {
					logger.error(e.getMessage(), e);
					return false;
				}
			}
			return false;
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long now = System.currentTimeMillis();
				String worktimeUuid = null;
				ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(currentProcessTaskStepVo.getProcessTaskId());
				if (processTaskVo != null) {
					if (StringUtils.isNotBlank(processTaskVo.getChannelUuid())) {
						ChannelVo channelVo = channelMapper.getChannelByUuid(processTaskVo.getChannelUuid());
						if (channelVo != null && StringUtils.isNotBlank(channelVo.getWorktimeUuid())) {
							worktimeUuid = channelVo.getWorktimeUuid();
						}
					}
				}
				for (ProcessTaskSlaVo slaVo : slaList) {
					/** 如果没有超时时间，证明第一次进入SLA标签范围，开始计算超时时间 **/
					ProcessTaskSlaTimeVo slaTimeVo = slaVo.getSlaTimeVo();
					boolean isSlaTimeExists = false;
					if (slaTimeVo == null) {
						if (slaVo.getConfigObj() != null) {
							JSONArray policyList = slaVo.getConfigObj().getJSONArray("calculatePolicyList");
							if (policyList != null && policyList.size() > 0) {
								POLICY: for (int i = 0; i < policyList.size(); i++) {
									JSONObject policyObj = policyList.getJSONObject(i);
									String connectionType = policyObj.getString("connectType");
									int enablePriority = policyObj.getIntValue("enablePriority");
									int time = policyObj.getIntValue("time");
									String unit = policyObj.getString("unit");
									JSONArray priorityList = policyObj.getJSONArray("priorityList");
									JSONArray ruleList = policyObj.getJSONArray("ruleList");
									boolean isHit = false;
									if (ruleList != null && ruleList.size() > 0) {
										isHit = validateRule(ruleList, connectionType);
									} else {// 如果没有规则，则无需判断
										isHit = true;
									}
									if (isHit) {
										slaTimeVo = new ProcessTaskSlaTimeVo();
										if (enablePriority == 0) {
											long timecost = getRealtime(time, unit);
											slaTimeVo.setTimeSum(timecost);
											slaTimeVo.setRealTimeLeft(timecost);
											slaTimeVo.setTimeLeft(timecost);
										} else {
											if (priorityList != null && priorityList.size() > 0) {
												for (int p = 0; p < priorityList.size(); p++) {
													JSONObject priorityObj = priorityList.getJSONObject(p);
													if (priorityObj.getString("priority").equals(processTaskVo.getPriorityUuid())) {
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
						slaTimeVo.setRealExpireTime(sdf.format(new Date(now + slaTimeVo.getRealTimeLeft())));
						if (StringUtils.isNotBlank(worktimeUuid)) {
							if (slaTimeVo.getTimeLeft() != null) {
								long expireTime = calculateExpireTime(now, slaTimeVo.getTimeLeft(), worktimeUuid);
								slaTimeVo.setExpireTime(sdf.format(new Date(expireTime)));
							} else {
								throw new RuntimeException("计算剩余时间失败");
							}
						} else {
							if (slaTimeVo.getTimeLeft() != null) {
								slaTimeVo.setExpireTime(sdf.format(new Date(now + slaTimeVo.getTimeLeft())));
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

						if (StringUtils.isNotBlank(slaTimeVo.getExpireTime()) && slaVo.getConfigObj() != null) {
							// 加载定时作业，执行超时通知操作
							JSONArray notifyPolicyList = slaVo.getConfigObj().getJSONArray("notifyPolicyList");
							if (notifyPolicyList != null && notifyPolicyList.size() > 0) {
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
							if (transferPolicyList != null && transferPolicyList.size() > 0) {
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
		protected static void audit(ProcessTaskStepVo currentProcessTaskStepVo, ProcessTaskStepAction action) {
			ProcessTaskStepTimeAuditVo processTaskStepTimeAuditVo = processTaskStepTimeAuditMapper.getLastProcessTaskStepTimeAuditByStepId(currentProcessTaskStepVo.getId());
			boolean needNewAuduit = false;
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
			List<String> actionList = getProcessTaskStepActionList(processTaskId, processTaskStepId, verifyActionList);
			if(!actionList.contains(action.getValue())) {
				throw new ProcessTaskNoPermissionException(action.getText());
			}
			return true;
		}
		protected static List<String> getProcessTaskStepActionList(Long processTaskId, Long processTaskStepId) {
			return getProcessTaskStepActionList(processTaskId, processTaskStepId, null);
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
			Set<String> resultList = new HashSet<>();
			ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskById(processTaskId);
			
			//上报提交
			if(CollectionUtils.isEmpty(verifyActionList) || verifyActionList.contains(ProcessTaskStepAction.STARTPROCESS.getValue())) {	
				if(ProcessTaskStatus.DRAFT.getValue().equals(processTaskVo.getStatus())) {
					if(UserContext.get().getUserId(true).equals(processTaskVo.getOwner()) || UserContext.get().getUserId(true).equals(processTaskVo.getReporter())) {
						resultList.add(ProcessTaskStepAction.STARTPROCESS.getValue());
					}
				}
			}
			if(processTaskStepId != null) {
				//获取步骤信息
				ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepId);
				List<String> currentUserProcessUserTypeList = new ArrayList<>();
				List<String> actionList = new ArrayList<>();
				actionList.add(ProcessTaskStepAction.VIEW.getValue());
				actionList.add(ProcessTaskStepAction.TRANSFER.getValue());
				actionList.add(ProcessTaskStepAction.URGE.getValue());
				if(CollectionUtils.isEmpty(verifyActionList) || actionList.removeAll(verifyActionList)) {
					actionList = new ArrayList<>();
					actionList.add(ProcessTaskStepAction.VIEW.getValue());
					actionList.add(ProcessTaskStepAction.TRANSFER.getValue());
					actionList.add(ProcessTaskStepAction.URGE.getValue());
					if(CollectionUtils.isEmpty(currentUserProcessUserTypeList)) {
						currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStepId);
					}
					List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStepVo, actionList, currentUserProcessUserTypeList);
					//根据流程设置和步骤状态判断当前用户权限
					for(String action : configActionList) {
						if(ProcessTaskStepAction.VIEW.getValue().equals(action)) {
							resultList.add(action);
						}else if(ProcessTaskStepAction.TRANSFER.getValue().equals(action)) {
							//步骤状态为已激活的才能转交
							if(processTaskStepVo.getIsActive() == 1) {
								resultList.add(action);
							}
						} else if(ProcessTaskStepAction.URGE.getValue().equals(action)) {
							//步骤状态为已激活的才能催办
							if(processTaskStepVo.getIsActive() == 1) {
								resultList.add(action);
							}
						}
					}
				}
				
				actionList = new ArrayList<>();
				actionList.add(ProcessTaskStepAction.COMPLETE.getValue());
				actionList.add(ProcessTaskStepAction.SAVE.getValue());
				actionList.add(ProcessTaskStepAction.COMMENT.getValue());
				actionList.add(ProcessTaskStepAction.CREATESUBTASK.getValue());
				if(CollectionUtils.isEmpty(verifyActionList) || actionList.removeAll(verifyActionList)) {
					if(processTaskStepVo.getIsActive() == 1 && ProcessTaskStatus.RUNNING.getValue().equals(processTaskStepVo.getStatus())) {
						if(CollectionUtils.isEmpty(currentUserProcessUserTypeList)) {
							currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStepId);
						}
						//完成complete 暂存save 评论comment 创建子任务createsubtask
						if(currentUserProcessUserTypeList.contains(ProcessUserType.MAJOR.getValue()) || currentUserProcessUserTypeList.contains(ProcessUserType.AGENT.getValue())) {
							resultList.add(ProcessTaskStepAction.COMPLETE.getValue());
							resultList.add(ProcessTaskStepAction.SAVE.getValue());
							resultList.add(ProcessTaskStepAction.COMMENT.getValue());
							resultList.add(ProcessTaskStepAction.CREATESUBTASK.getValue());
						}
					}
				}
			}

			List<String> actionList = new ArrayList<>();
			actionList.add(ProcessTaskStepAction.ABORT.getValue());
			actionList.add(ProcessTaskStepAction.RECOVER.getValue());
			actionList.add(ProcessTaskStepAction.UPDATE.getValue());
			if(CollectionUtils.isEmpty(verifyActionList) || actionList.removeAll(verifyActionList)) {
				//终止/恢复流程abort、修改上报内容update取工单当前所有正在处理的节点权限配置的并集
				List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.PROCESS.getValue());
				for(ProcessTaskStepVo processTaskStep : processTaskStepList) {
					if(processTaskStep.getIsActive().intValue() == 1) {
						List<String> currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStep.getId());
						actionList = new ArrayList<>();
						actionList.add(ProcessTaskStepAction.ABORT.getValue());
						actionList.add(ProcessTaskStepAction.UPDATE.getValue());
						List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStep, actionList, currentUserProcessUserTypeList);
						//根据流程设置和步骤状态判断当前用户权限
						for(String action : configActionList) {
							if(ProcessTaskStepAction.ABORT.getValue().equals(action)) {
								//工单状态为进行中的才能终止
								if(ProcessTaskStatus.RUNNING.getValue().equals(processTaskVo.getStatus())) {
									resultList.add(action);
								}
							}else if(ProcessTaskStepAction.UPDATE.getValue().equals(action)) {
								resultList.add(action);
							}
						}
					}else if(processTaskStep.getIsActive().intValue() == -1) {
						List<String> currentUserProcessUserTypeList = getCurrentUserProcessUserTypeList(processTaskVo, processTaskStep.getId());
						actionList = new ArrayList<>();
						actionList.add(ProcessTaskStepAction.ABORT.getValue());
						List<String> configActionList = getProcessTaskStepConfigActionList(processTaskVo, processTaskStep, actionList, currentUserProcessUserTypeList);
						if(configActionList.contains(ProcessTaskStepAction.ABORT.getValue())) {
							configActionList.add(ProcessTaskStepAction.RECOVER.getValue());
						}
						//根据流程设置和步骤状态判断当前用户权限
						for(String action : configActionList) {
							if(ProcessTaskStepAction.RECOVER.getValue().equals(action)) {
								//工单状态为已终止的才能恢复
								if(ProcessTaskStatus.ABORTED.getValue().equals(processTaskVo.getStatus())) {
									resultList.add(action);
								}
							}
						}
					}
				}
			}
			
			actionList = new ArrayList<>();
			actionList.add(ProcessTaskStepAction.ACCEPT.getValue());
			actionList.add(ProcessTaskStepAction.START.getValue());
			if(CollectionUtils.isEmpty(verifyActionList) || actionList.removeAll(verifyActionList)) {
				//获取当前用户可处理步骤列表
				List<ProcessTaskStepVo> processableStepList = getProcessableStepList(processTaskId);
				if(CollectionUtils.isNotEmpty(processableStepList)) {
					for(ProcessTaskStepVo processTaskStepVo : processableStepList) {
						if(ProcessTaskStatus.PENDING.getValue().equals(processTaskStepVo.getStatus())) {//已激活未处理
							List<ProcessTaskStepUserVo> majorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
							String action = "";
							if(majorUserList.isEmpty()) {//没有主处理人时是accept
								action = ProcessTaskStepAction.ACCEPT.getValue();
							}else {
								action = ProcessTaskStepAction.START.getValue();
							}
							if(processTaskStepId != null) {
								if(processTaskStepId.equals(processTaskStepVo.getId())) {
									resultList.add(action);
									break;
								}
							}else {
								resultList.add(action);
							}
						}
					}
				}
			}

			if(CollectionUtils.isEmpty(verifyActionList) || verifyActionList.contains(ProcessTaskStepAction.RETREAT.getValue())) {
				//撤销权限retreat
				Set<ProcessTaskStepVo> retractableStepSet = getRetractableStepListByProcessTaskId(processTaskId);
				if(CollectionUtils.isNotEmpty(retractableStepSet)) {
					resultList.add(ProcessTaskStepAction.RETREAT.getValue());
				}
			}

			return new ArrayList<String>(resultList);
		}
		/**
		 * 
		* @Time:2020年4月3日
		* @Description: 获取当前用户在当前步骤中工单干系人列表
		* @param processTaskVo 工单信息
		* @param processTaskStepId 步骤id 
		* @return List<String>
		 */
		private static List<String> getCurrentUserProcessUserTypeList(ProcessTaskVo processTaskVo, Long processTaskStepId){
			List<String> currentUserProcessUserTypeList = new ArrayList<>();
			currentUserProcessUserTypeList.add(UserType.ALL.getValue());
			if(UserContext.get().getUserId(true).equals(processTaskVo.getOwner())) {
				currentUserProcessUserTypeList.add(ProcessUserType.OWNER.getValue());
			}
			if(UserContext.get().getUserId(true).equals(processTaskVo.getReporter())) {
				currentUserProcessUserTypeList.add(ProcessUserType.REPORTER.getValue());
			}
			List<ProcessTaskStepUserVo> majorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.MAJOR.getValue());
			List<String> majorUserIdList = majorUserList.stream().map(ProcessTaskStepUserVo::getUserId).collect(Collectors.toList());
			if(majorUserIdList.contains(UserContext.get().getUserId(true))) {
				currentUserProcessUserTypeList.add(ProcessUserType.MAJOR.getValue());
			}
			List<ProcessTaskStepUserVo> minorUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.MINOR.getValue());
			List<String> minorUserIdList = minorUserList.stream().map(ProcessTaskStepUserVo::getUserId).collect(Collectors.toList());
			if(minorUserIdList.contains(UserContext.get().getUserId(true))) {
				currentUserProcessUserTypeList.add(ProcessUserType.MINOR.getValue());
			}
			List<ProcessTaskStepUserVo> agentUserList = processTaskMapper.getProcessTaskStepUserByStepId(processTaskStepId, ProcessUserType.AGENT.getValue());
			List<String> agentUserIdList = agentUserList.stream().map(ProcessTaskStepUserVo::getUserId).collect(Collectors.toList());
			if(agentUserIdList.contains(UserContext.get().getUserId(true))) {
				currentUserProcessUserTypeList.add(ProcessUserType.AGENT.getValue());
			}
			return currentUserProcessUserTypeList;
		}
		/**
		 * 
		* @Time:2020年4月2日
		* @Description: 获取流程节点配置中的当前用户的拥有的权限 
		* @param processTaskVo
		* @param processTaskStepVo
		* @param actionList 要获取的权限集合
		* @param currentUserProcessUserTypeList 当前用户工单干系人列表
		* @return List<String>
		 */
		private static List<String> getProcessTaskStepConfigActionList(ProcessTaskVo processTaskVo, ProcessTaskStepVo processTaskStepVo, List<String> actionList, List<String> currentUserProcessUserTypeList){
			
			String stepConfig = processTaskMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());
			JSONObject stepConfigObj = JSON.parseObject(stepConfig);
			JSONArray authorityList = stepConfigObj.getJSONArray("authorityList");
			//如果步骤自定义权限设置为空，则用组件的全局权限设置
			if(CollectionUtils.isEmpty(authorityList)) {
				ProcessStepHandlerVo processStepHandlerVo = processStepHandlerMapper.getProcessStepHandlerByHandler(processTaskStepVo.getHandler());
				if(processStepHandlerVo != null) {
					JSONObject handlerConfigObj = JSON.parseObject(processStepHandlerVo.getConfig());
					authorityList = handlerConfigObj.getJSONArray("authorityList");
				}
			}
			List<String> configActionList = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(authorityList)) {
				List<String> currentUserTeamList = teamMapper.getTeamUuidListByUserId(UserContext.get().getUserId(true));
				for(int i = 0; i < authorityList.size(); i++) {
					JSONObject authorityObj = authorityList.getJSONObject(i);
					JSONArray acceptList = authorityObj.getJSONArray("acceptList");
					if(CollectionUtils.isNotEmpty(acceptList)) {
						for(int j = 0; j < acceptList.size(); j++) {
							String accept = acceptList.getString(j);
							String[] split = accept.split("#");
							if(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue().equals(split[0])) {
								if(currentUserProcessUserTypeList.contains(split[1])) {
									configActionList.add(authorityObj.getString("action"));
									break;
								}
							}else if(GroupSearch.USER.getValue().equals(split[0])) {
								if(UserContext.get().getUserId(true).equals(split[1])) {
									configActionList.add(authorityObj.getString("action"));
									break;
								}
							}else if(GroupSearch.TEAM.getValue().equals(split[0])) {
								if(currentUserTeamList.contains(split[1])) {
									configActionList.add(authorityObj.getString("action"));
									break;
								}
							}else if(GroupSearch.ROLE.getValue().equals(split[0])) {
								if(UserContext.get().getRoleNameList().contains(split[1])) {
									configActionList.add(authorityObj.getString("action"));
									break;
								}
							}
						}

					}else {
						configActionList.add(authorityObj.getString("action"));
					}
				}
				configActionList.retainAll(actionList);
			}else {//不设置，默认都有
				configActionList.addAll(actionList);
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
			/**所有前置步骤**/
			List<ProcessTaskStepVo> fromStepList = processTaskMapper.getFromProcessTaskStepByToId(processTaskStepId);
			/** 找到所有已完成步骤 **/
			for (ProcessTaskStepVo fromStep : fromStepList) {
				IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(fromStep.getHandler());
				if(handler != null) {
					if (ProcessStepMode.MT == handler.getMode()) {//手动处理节点
						//获取步骤处理人
						List<ProcessTaskStepUserVo> majorUserList = processTaskMapper.getProcessTaskStepUserByStepId(fromStep.getId(), ProcessUserType.MAJOR.getValue());
						List<String> majorUserIdList = majorUserList.stream().map(ProcessTaskStepUserVo::getUserId).collect(Collectors.toList());
						if(majorUserIdList.contains(UserContext.get().getUserId())) {
							resultList.add(fromStep);
						}
					}else {//自动处理节点，继续找前置节点
						resultList.addAll(getRetractableStepListByProcessTaskStepId(fromStep.getId()));
					}
				}else {
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
			List<String> currentUserTeamList = teamMapper.getTeamUuidListByUserId(UserContext.get().getUserId(true));
			List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskId(processTaskId);
			for (ProcessTaskStepVo stepVo : processTaskStepList) {
				/** 找到所有已激活未处理的步骤 **/
				if (stepVo.getIsActive().equals(1)) {
					List<ProcessTaskStepWorkerVo> processTaskStepWorkerList = processTaskMapper.getProcessTaskStepWorkerByProcessTaskStepId(stepVo.getId());
					for(ProcessTaskStepWorkerVo processTaskStepWorkerVo : processTaskStepWorkerList) {
						if(GroupSearch.USER.getValue().equals(processTaskStepWorkerVo.getType()) && UserContext.get().getUserId(true).equals(processTaskStepWorkerVo.getUuid())) {
							resultList.add(stepVo);
							break;
						}else if(GroupSearch.TEAM.getValue().equals(processTaskStepWorkerVo.getType()) && currentUserTeamList.contains(processTaskStepWorkerVo.getUuid())){
							resultList.add(stepVo);
							break;
						}else if(GroupSearch.ROLE.getValue().equals(processTaskStepWorkerVo.getType()) && UserContext.get().getRoleNameList().contains(processTaskStepWorkerVo.getUuid())){
							resultList.add(stepVo);
							break;
						}
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
				processTaskStepAuditVo.setUserId(UserContext.get().getUserId(true));
				processTaskMapper.insertProcessTaskStepAudit(processTaskStepAuditVo);
				JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
				if(MapUtils.isNotEmpty(paramObj)) {
					for(ProcessTaskAuditDetailType auditDetailType : ProcessTaskAuditDetailType.values()) {
						String newData = paramObj.getString(auditDetailType.getParamName());
						String oldData = paramObj.getString(auditDetailType.getOldDataParamName());
						if(!Objects.equals(oldData, newData)) {
							processTaskMapper.insertProcessTaskStepAuditDetail(new ProcessTaskStepAuditDetailVo(processTaskStepAuditVo.getId(), auditDetailType.getValue(), oldData, newData));
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
			if(processTaskFormVo == null) {
				return true;
			}
			if(StringUtils.isBlank(processTaskFormVo.getFormContent())) {
				return true;
			}
			FormVersionVo formVersionVo = new FormVersionVo();
			formVersionVo.setFormConfig(processTaskFormVo.getFormContent());
			formVersionVo.setFormUuid(processTaskFormVo.getFormUuid());
			List<FormAttributeVo> formAttributeList =formVersionVo.getFormAttributeList();
			if(formAttributeList == null || formAttributeList.isEmpty()) {
				return true;
			}
			Map<String, String> formAttributeDataMap = new HashMap<>();
			List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			for(ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo : processTaskFormAttributeDataList) {
				formAttributeDataMap.put(processTaskFormAttributeDataVo.getAttributeUuid(), processTaskFormAttributeDataVo.getData());
			}
			Map<String, String> formAttributeActionMap = new HashMap<>();
			List<ProcessTaskStepFormAttributeVo> processTaskStepFormAttributeList = processTaskMapper.getProcessTaskStepFormAttributeByProcessTaskStepId(currentProcessTaskStepVo.getId());
			for(ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo : processTaskStepFormAttributeList) {
				formAttributeActionMap.put(processTaskStepFormAttributeVo.getAttributeUuid(), processTaskStepFormAttributeVo.getAction());
			}
			
			for(FormAttributeVo formAttributeVo : formAttributeList) {
				if(!formAttributeVo.isRequired()) {
					continue;
				}
				if(formAttributeActionMap.containsKey(formAttributeVo.getUuid())) {
					continue;
				}
				String data = formAttributeDataMap.get(formAttributeVo.getUuid());
				if(StringUtils.isBlank(data)) {
					throw new ProcessTaskRuntimeException("表单属性：'" + formAttributeVo.getLabel()+ "'不能为空");
				}
				if(data.startsWith("[") && data.endsWith("]")) {
					JSONArray jsonArray = JSON.parseArray(data);
					if(jsonArray == null || jsonArray.isEmpty()) {
						throw new ProcessTaskRuntimeException("表单属性：'" + formAttributeVo.getLabel()+ "'不能为空");
					}
				}
			}
			return true;
		}
	}
}
