package codedriver.framework.process.stephandler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.asynchronization.threadpool.CachedThreadPool;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.notify.dto.NotifyPolicyInvokerVo;
import codedriver.framework.notify.dto.NotifyPolicyVo;
import codedriver.framework.process.constvalue.OperationType;
import codedriver.framework.process.constvalue.ProcessStepHandler;
import codedriver.framework.process.constvalue.ProcessStepMode;
import codedriver.framework.process.constvalue.ProcessStepType;
import codedriver.framework.process.constvalue.ProcessTaskAuditDetailType;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessTaskStepAction;
import codedriver.framework.process.constvalue.ProcessTaskStepDataType;
import codedriver.framework.process.constvalue.ProcessTaskStepUserStatus;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.FormVersionVo;
import codedriver.framework.process.dto.ProcessSlaVo;
import codedriver.framework.process.dto.ProcessStepRelVo;
import codedriver.framework.process.dto.ProcessStepVo;
import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskConvergeVo;
import codedriver.framework.process.dto.ProcessTaskFormAttributeDataVo;
import codedriver.framework.process.dto.ProcessTaskFormVo;
import codedriver.framework.process.dto.ProcessTaskSlaVo;
import codedriver.framework.process.dto.ProcessTaskStepConfigVo;
import codedriver.framework.process.dto.ProcessTaskStepContentVo;
import codedriver.framework.process.dto.ProcessTaskStepDataVo;
import codedriver.framework.process.dto.ProcessTaskStepFormAttributeVo;
import codedriver.framework.process.dto.ProcessTaskStepNotifyPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepRelVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.dto.ProcessVo;
import codedriver.framework.process.exception.core.ProcessTaskException;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
import codedriver.framework.process.exception.process.ProcessStepHandlerNotFoundException;
import codedriver.framework.process.exception.processtask.ProcessTaskStepUnActivedException;
import codedriver.framework.process.exception.processtask.ProcessTaskStepUserIsExistsException;
import codedriver.framework.process.notify.core.NotifyTriggerType;

public abstract class ProcessStepHandlerBase extends ProcessStepHandlerUtilBase implements IProcessStepHandler {
	static Logger logger = LoggerFactory.getLogger(ProcessStepHandlerBase.class);
	private static final ThreadLocal<List<ProcessStepThread>> PROCESS_STEP_RUNNABLES = new ThreadLocal<>();

	private int updateProcessTaskStatus(Long processTaskId) {
		List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskId(processTaskId);

		int runningCount = 0, succeedCount = 0, failedCount = 0, abortedCount = 0, draftCount = 0;
		for (ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
			if (ProcessTaskStatus.DRAFT.getValue().equals(processTaskStepVo.getStatus()) && processTaskStepVo.getIsActive().equals(1)) {
				draftCount += 1;
			} else if (processTaskStepVo.getIsActive().equals(1)) {
				runningCount += 1;
			} else if (processTaskStepVo.getIsActive().equals(-1)) {
				abortedCount += 1;
			} else if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.SUCCEED.getValue()) && ProcessStepHandler.END.getHandler().equals(processTaskStepVo.getHandler())) {
				succeedCount += 1;
			} else if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.FAILED.getValue())) {
				failedCount += 1;
			}
		}

		ProcessTaskVo processTaskVo = new ProcessTaskVo();
		processTaskVo.setId(processTaskId);
		if (draftCount > 0) {
			processTaskVo.setStatus(ProcessTaskStatus.DRAFT.getValue());
		} else if (runningCount > 0) {
			processTaskVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
		} else if (abortedCount > 0) {
			processTaskVo.setStatus(ProcessTaskStatus.ABORTED.getValue());
		} else if (failedCount > 0) {
			processTaskVo.setStatus(ProcessTaskStatus.FAILED.getValue());
		} else if (succeedCount > 0) {
			processTaskVo.setStatus(ProcessTaskStatus.SUCCEED.getValue());
		} else {
			return 1;
		}
		processTaskMapper.updateProcessTaskStatus(processTaskVo);
		return 1;
	}

	@Override
	public final int active(ProcessTaskStepVo currentProcessTaskStepVo) {
		try {
			/** 锁定当前流程 **/
			processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
			boolean canFire = false;
			/** 获取当前步骤的所有前置步骤 **/
			/** 获取当前步骤的所有前置连线 **/
			List<ProcessTaskStepRelVo> fromProcessTaskStepRelList = processTaskMapper.getProcessTaskStepRelByToId(currentProcessTaskStepVo.getId());
			/** 场景一：当前步骤只有1个前置关系，并且该前置关系状态为已流转，则当前步骤允许激活 **/
			if (fromProcessTaskStepRelList.size() == 1) {
				ProcessTaskStepRelVo fromProcessTaskStepRelVo = fromProcessTaskStepRelList.get(0);
				if (fromProcessTaskStepRelVo.getIsHit().equals(1)) {
					canFire = true;
				}
			}
			/** 场景二：当前步骤有大于1个前置关系，则需要使用多种规则来判断当前步骤是否具备激活条件。 **/
			else if (fromProcessTaskStepRelList.size() > 1) {
				/** 获取汇聚节点是当前节点的所有前置节点 **/
				List<ProcessTaskStepVo> convergeStepList = processTaskMapper.getProcessTaskStepByConvergeId(currentProcessTaskStepVo.getId());
				boolean hasDoingStep = false;
				for (ProcessTaskStepVo convergeStepVo : convergeStepList) {
					/** 任意前置节点处于处理中状态 **/
					if (convergeStepVo.getIsActive().equals(1)) {
						hasDoingStep = true;
						break;
					}
				}
				if (!hasDoingStep) {
					canFire = true;
				}
			/** 场景三：没有前置节点，证明是开始节点 **/
			} else {
				canFire = true;
			}

			if (canFire) {
				/** 设置当前步骤状态为未开始 **/
				currentProcessTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());

				myActive(currentProcessTaskStepVo);

				/** 遍历后续节点所有步骤，写入汇聚步骤数据 **/
				resetConvergeInfo(currentProcessTaskStepVo);

				/** 如果当前步骤是二次进入(后续路径已经走过)，则需要对所有后续流转过的步骤都进行挂起操作 **/
				/** 获取当前步骤状态 **/
				List<ProcessTaskStepRelVo> nextTaskStepRelList = processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());
				for (ProcessTaskStepRelVo nextTaskStepRelVo : nextTaskStepRelList) {
					if (Objects.equals(nextTaskStepRelVo.getIsHit(), 1)) {
						ProcessTaskStepVo nextProcessTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(nextTaskStepRelVo.getToProcessTaskStepId());
						if (nextProcessTaskStepVo != null && !Objects.equals(nextProcessTaskStepVo.getIsActive(), 0)) {
							IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(nextProcessTaskStepVo.getHandler());
							// 标记挂起操作来源步骤
							nextProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
							// 标记挂起操作的发起步骤，避免出现死循环
							nextProcessTaskStepVo.setStartProcessTaskStepId(currentProcessTaskStepVo.getId());
							if (handler != null) {
								doNext(new ProcessStepThread(nextProcessTaskStepVo) {
									@Override
									public void execute() {
										handler.hang(nextProcessTaskStepVo);
									}
								});
							}
						}
					}
					// 恢复路径命中状态为0，代表路径未通过
					processTaskMapper.updateProcessTaskStepRelIsHit(currentProcessTaskStepVo.getId(), nextTaskStepRelVo.getToProcessTaskStepId(), 0);
				}

				if (this.getMode().equals(ProcessStepMode.MT)) {
					/** 分配处理人 **/
					assign(currentProcessTaskStepVo);
				} else if (this.getMode().equals(ProcessStepMode.AT)) {
					/** 自动处理 **/
					IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
					doNext(new ProcessStepThread(currentProcessTaskStepVo) {
						@Override
						public void execute() {
							handler.handle(currentProcessTaskStepVo);
						}
					});
				}
				currentProcessTaskStepVo.setIsActive(1);
				updateProcessTaskStepStatus(currentProcessTaskStepVo);

				/** 写入时间审计 **/
				TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.ACTIVE);
				if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
					TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.START);
				}

				/** 计算SLA并触发超时警告 **/
				SlaHandler.calculate(currentProcessTaskStepVo);

				/** 触发通知 **/
				NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.ACTIVE);
				
				/** 执行动作 **/
				ActionHandler.action(currentProcessTaskStepVo, NotifyTriggerType.ACTIVE);
			}
		} catch (ProcessTaskException e) {
			logger.error(e.getMessage(), e);
			currentProcessTaskStepVo.setIsActive(0);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			currentProcessTaskStepVo.setError(e.getMessage());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		} finally {
			if (ProcessTaskStatus.FAILED.getValue().equals(currentProcessTaskStepVo.getStatus())) {
				/**
				 * 发生异常不能激活当前步骤，执行当前步骤的回退操作
				 */
				IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
				doNext(new ProcessStepThread(currentProcessTaskStepVo) {
					@Override
					public void execute() {
						handler.back(currentProcessTaskStepVo);
					}
				});
			}
		}
		return 1;
	}

	protected abstract int myActive(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	@Override
	public final int assign(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException {
		/** 清空处理人 **/
		List<ProcessTaskStepWorkerVo> workerList = new ArrayList<>();
		List<ProcessTaskStepUserVo> userList = new ArrayList<>();
		myAssign(currentProcessTaskStepVo, workerList, userList);

		processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
		if (workerList.size() > 0) {
			for (ProcessTaskStepWorkerVo workerVo : workerList) {
				processTaskMapper.insertProcessTaskStepWorker(workerVo);
			}
		} else {
			throw new ProcessTaskException("没有匹配到处理人");
		}

		ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
		processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
		processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());// 只删除主处理人人
		processTaskMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
		if (userList.size() > 0) {
			for (ProcessTaskStepUserVo userVo : userList) {
				processTaskMapper.insertProcessTaskStepUser(userVo);
			}
		}
		/** 触发通知 **/
		NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.ASSIGN);
		
		/** 执行动作 **/
		ActionHandler.action(currentProcessTaskStepVo, NotifyTriggerType.ASSIGN);
		return 1;
	}

	protected abstract int myAssign(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepWorkerVo> workerList, List<ProcessTaskStepUserVo> userList) throws ProcessTaskException;

	/**
	 * hang操作原则上不允许出现任何异常，所有异常都必须解决以便流程可以顺利挂起，否则流程可能会卡死在某个节点不能前进或后退
	 */
	@Override
	public final int hang(ProcessTaskStepVo currentProcessTaskStepVo) {
		try {
			// 锁定当前流程
			processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

			myHang(currentProcessTaskStepVo);
			// 删除当前节点的汇聚记录
			processTaskMapper.deleteProcessTaskConvergeByStepId(currentProcessTaskStepVo.getId());

			// 获取流转过的路径
			List<ProcessTaskStepRelVo> toProcessTaskStepRelList = processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());

			for (ProcessTaskStepRelVo processTaskStepRelVo : toProcessTaskStepRelList) {
				// 沿着流转过的路径向后找激活过的节点并挂起
				if (Objects.equals(processTaskStepRelVo.getIsHit(), 1)) {
					ProcessTaskStepVo toProcessTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepRelVo.getToProcessTaskStepId());
					if (toProcessTaskStepVo != null && !Objects.equals(toProcessTaskStepVo.getIsActive(), 0)) {
						// 如果下一个步骤不等于发起步骤，则继续挂起
						if (!toProcessTaskStepVo.getId().equals(currentProcessTaskStepVo.getStartProcessTaskStepId())) {
							IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(toProcessTaskStepVo.getHandler());
							toProcessTaskStepVo.setStartProcessTaskStepId(currentProcessTaskStepVo.getStartProcessTaskStepId());
							toProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
							if (handler != null) {
								doNext(new ProcessStepThread(toProcessTaskStepVo) {
									@Override
									public void execute() {
										handler.hang(toProcessTaskStepVo);
									}
								});
							}
						}
					}
				}
				/** 重置路径状态 **/
				processTaskMapper.updateProcessTaskStepRelIsHit(currentProcessTaskStepVo.getId(), processTaskStepRelVo.getToProcessTaskStepId(), 0);
			}

			currentProcessTaskStepVo.setIsActive(0);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.HANG.getValue());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
			/** 触发通知 **/
			NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.HANG);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, NotifyTriggerType.HANG);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			currentProcessTaskStepVo.setIsActive(2);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			currentProcessTaskStepVo.setError(ExceptionUtils.getStackTrace(e));
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		}
		return 1;
	}

	/** 由于hang属于自动操作，不抛出任何受检异常，所有异常都需要catch并记录日志方便修正缺陷 **/
	protected abstract int myHang(ProcessTaskStepVo currentProcessTaskStepVo);

	@Override
	public final int handle(ProcessTaskStepVo currentProcessTaskStepVo) {
		// 锁定当前流程
		processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
		// 修改当前步骤状态为运行中
		currentProcessTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
		updateProcessTaskStepStatus(currentProcessTaskStepVo);
		if (!this.isAsync()) {// 同步模式
			try {
				myHandle(currentProcessTaskStepVo);
				/** 如果步骤被标记为全部完成，则触发完成 **/
				if (currentProcessTaskStepVo.getIsAllDone()) {
					/** 记录时间审计 **/
					TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.COMPLETE);
					IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
					doNext(new ProcessStepThread(currentProcessTaskStepVo) {
						@Override
						public void execute() {
							handler.complete(currentProcessTaskStepVo);
						}
					});
				}
			} catch (ProcessTaskException ex) {
				logger.error(ex.getMessage(), ex);
				currentProcessTaskStepVo.setError(ex.getMessage());
				currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
				updateProcessTaskStepStatus(currentProcessTaskStepVo);
			} finally {/** 告诉超时探测器步骤已经完成 **/
				// timeoutDetector.setIsDone(true);
				// timeoutDetector.interrupt();

			}
		} else {// 异步模式
			String handlerType = this.getHandler();
			ProcessStepThread thread = new ProcessStepThread(currentProcessTaskStepVo) {
				@Override
				public void execute() {
					IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(handlerType);
					try {
						// 这里不会有事务控制
						myHandle(currentProcessTaskStepVo);
						if (currentProcessTaskStepVo.getIsAllDone()) {
							/** 记录时间审计 **/
							TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.COMPLETE);

							doNext(new ProcessStepThread(currentProcessTaskStepVo) {
								@Override
								public void execute() {
									handler.complete(currentProcessTaskStepVo);
								}
							});
						}
					} catch (ProcessTaskException ex) {
						logger.error(ex.getMessage(), ex);
						currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
						currentProcessTaskStepVo.setError(ex.getMessage());
						updateProcessTaskStepStatus(currentProcessTaskStepVo);
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
						currentProcessTaskStepVo.setError(ExceptionUtils.getStackTrace(ex));
						updateProcessTaskStepStatus(currentProcessTaskStepVo);
					} finally {
						/** 告诉超时探测器步骤已经完成 **/
						// if (this.getTimeoutDetector() != null) {
						// this.getTimeoutDetector().setIsDone(true);
						// this.getTimeoutDetector().interrupt();
						// }
						// 是否有步骤结果
						// if (flowJobStepVo.getHasResult()) {
						// flowJobMapper.insertFlowJobStepResult(flowJobStepVo.getId(),
						// flowJobStepVo.getResultPath());
						// }
					}
				}
			};
			/** 设置超时探测器到步骤处理线程 **/
			// thread.setTimeoutDetector(timeoutDetector);
			doNext(thread);
		}

		return 1;
	}

	protected abstract int myHandle(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	@Override
	public final int start(ProcessTaskStepVo currentProcessTaskStepVo) {
		try {
			// 锁定当前流程
			processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

			/** 检查处理人是否合法 **/
			ActionRoleChecker.verifyActionAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskStepAction.START);

			ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());

			/** 检查步骤是否“已激活” **/
			if (!processTaskStepVo.getIsActive().equals(1)) {
				throw new ProcessTaskStepUnActivedException();
			}
			/** 判断工单步骤状态是否 “未开始” **/
			if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
				throw new ProcessTaskRuntimeException("当前步骤已开始");
			}

			myStart(currentProcessTaskStepVo);

			/** 更新处理人状态 **/
			ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
			processTaskStepUserVo.setUserUuid(UserContext.get().getUserUuid(true));
			processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
			processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DOING.getValue());
			processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());
			processTaskMapper.updateProcessTaskStepUserStatus(processTaskStepUserVo);

			/** 更新工单步骤状态为 “进行中” **/
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);

			/** 写入时间审计 **/
			TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.START);

			/** 计算SLA **/
			SlaHandler.calculate(currentProcessTaskStepVo);

			/** 触发通知 **/
			NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.START);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, NotifyTriggerType.START);
		} catch (ProcessTaskException ex) {
			logger.error(ex.getMessage(), ex);
			currentProcessTaskStepVo.setError(ex.getMessage());
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		} finally {
			/** 处理历史记录 **/
			AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.START);			
		}
		return 0;
	}

	private int updateProcessTaskStepStatus(ProcessTaskStepVo currentProcessTaskStepVo) {
		processTaskMapper.updateProcessTaskStepStatus(currentProcessTaskStepVo);
		updateProcessTaskStatus(currentProcessTaskStepVo.getProcessTaskId());
		return 1;
	}

	protected abstract int myStart(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	@Override
	public final int complete(ProcessTaskStepVo currentProcessTaskStepVo) {
		/** 锁定当前流程 **/
		processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
		ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
		/** 检查步骤是否 “已激活” **/
		if (!processTaskStepVo.getIsActive().equals(1)) {
			throw new ProcessTaskStepUnActivedException();
		}
		/** 状态是否为 “未开始” **/
		if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.PENDING.getValue())) {
			throw new ProcessTaskRuntimeException("请先开始步骤");
		}
		NotifyTriggerType notifyTriggerType = NotifyTriggerType.SUCCEED;
		ProcessTaskStepAction processTaskStepAction = ProcessTaskStepAction.COMPLETE;
		boolean canComplete = false;
		if (this.getMode().equals(ProcessStepMode.MT)) {
			// TODO 还需要增加代理人的逻辑

			JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
			if (MapUtils.isNotEmpty(paramObj)) {
				String action = paramObj.getString("action");
				if (ProcessTaskStepAction.BACK.getValue().equals(action)) {
					processTaskStepAction = ProcessTaskStepAction.BACK;
					notifyTriggerType = NotifyTriggerType.BACK;
				}
			}
			canComplete = ActionRoleChecker.verifyActionAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), processTaskStepAction);
		} else if (this.getMode().equals(ProcessStepMode.AT)) {
			canComplete = true;
		}

		if (canComplete) {
			try {
				JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
				ProcessTaskStepDataVo processTaskStepDataVo = new ProcessTaskStepDataVo();
				processTaskStepDataVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
				processTaskStepDataVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
				processTaskStepDataVo.setFcu(UserContext.get().getUserUuid(true));
				processTaskStepDataVo.setType(ProcessTaskStepDataType.STEPDRAFTSAVE.getValue());
				ProcessTaskStepDataVo stepDraftSaveData = processTaskStepDataMapper.getProcessTaskStepData(processTaskStepDataVo);
				if(stepDraftSaveData != null) {
					JSONObject dataObj = stepDraftSaveData.getData();
					if(MapUtils.isNotEmpty(dataObj)) {
						paramObj.putAll(dataObj);
					}
				}
				/** 保存描述内容 **/
				String content = paramObj.getString("content");
				if (StringUtils.isNotBlank(content)) {
					ProcessTaskContentVo contentVo = new ProcessTaskContentVo(content);
					processTaskMapper.replaceProcessTaskStepContent(new ProcessTaskStepContentVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), contentVo.getHash()));
				}
				myComplete(currentProcessTaskStepVo);
				if (MapUtils.isNotEmpty(paramObj)) {
					// 表单属性显示控制
					Map<String, String> formAttributeActionMap = new HashMap<>();
					List<ProcessTaskStepFormAttributeVo> processTaskStepFormAttributeList = processTaskMapper.getProcessTaskStepFormAttributeByProcessTaskStepId(currentProcessTaskStepVo.getId());
					if (processTaskStepFormAttributeList.size() > 0) {
						for (ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo : processTaskStepFormAttributeList) {
							formAttributeActionMap.put(processTaskStepFormAttributeVo.getAttributeUuid(), processTaskStepFormAttributeVo.getAction());
						}
					}
					// 组件联动导致隐藏的属性uuid列表
//					List<String> hidecomponentList = processTaskMapper.getProcessTaskStepDynamicHideFormAttributeUuidListByProcessTaskStepId(currentProcessTaskStepVo.getId());
					List<String> hidecomponentList = JSON.parseArray(JSON.toJSONString(paramObj.getJSONArray("hidecomponentList")), String.class);
					// 获取旧表单数据
					List<ProcessTaskFormAttributeDataVo> oldProcessTaskFormAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
					if (CollectionUtils.isNotEmpty(oldProcessTaskFormAttributeDataList)) {
						Iterator<ProcessTaskFormAttributeDataVo> iterator = oldProcessTaskFormAttributeDataList.iterator();
						while (iterator.hasNext()) {
							ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo = iterator.next();
							String attributeUuid = processTaskFormAttributeDataVo.getAttributeUuid();
							if (formAttributeActionMap.containsKey(attributeUuid)) {// 只读或隐藏
								iterator.remove();
							}
							if (CollectionUtils.isNotEmpty(hidecomponentList)&&hidecomponentList.contains(attributeUuid)) {
								iterator.remove();
							}
						}
						oldProcessTaskFormAttributeDataList.sort(ProcessTaskFormAttributeDataVo::compareTo);
						ProcessTaskContentVo processTaskContentVo = new ProcessTaskContentVo(JSON.toJSONString(oldProcessTaskFormAttributeDataList));
						processTaskMapper.replaceProcessTaskContent(processTaskContentVo);
						paramObj.put(ProcessTaskAuditDetailType.FORM.getOldDataParamName(), processTaskContentVo.getHash());
					}
					// 写入新表单数据
//					JSONArray formAttributeDataList = paramObj.getJSONArray(ProcessTaskAuditDetailType.FORM.getParamName());
//					if(formAttributeDataList != null) {
//						List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = JSON.parseArray(JSON.toJSONString(formAttributeDataList), ProcessTaskFormAttributeDataVo.class);
//						if(CollectionUtils.isNotEmpty(processTaskFormAttributeDataList)) {
//							for(ProcessTaskFormAttributeDataVo processTaskFromAttributeDataVo : processTaskFormAttributeDataList) {
//								processTaskMapper.replaceProcessTaskFormAttributeData(processTaskFromAttributeDataVo);
//							}
//						}
//					}
					JSONArray formAttributeDataList = paramObj.getJSONArray("formAttributeDataList");
					if (CollectionUtils.isNotEmpty(formAttributeDataList)) {
						List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = new ArrayList<>(formAttributeDataList.size());
						for (int i = 0; i < formAttributeDataList.size(); i++) {
							JSONObject formAttributeDataObj = formAttributeDataList.getJSONObject(i);
							String attributeUuid = formAttributeDataObj.getString("attributeUuid");
							if (formAttributeActionMap.containsKey(attributeUuid)) {// 对于只读或隐藏的属性，当前用户不能修改，不更新数据库中的值，不进行修改前后对比
								continue;
							}
							if (CollectionUtils.isNotEmpty(hidecomponentList)&&hidecomponentList.contains(attributeUuid)) {
								continue;
							}
							ProcessTaskFormAttributeDataVo attributeData = new ProcessTaskFormAttributeDataVo();
							String dataList = formAttributeDataObj.getString("dataList");
							attributeData.setData(dataList);
							attributeData.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
							attributeData.setAttributeUuid(attributeUuid);
							attributeData.setType(formAttributeDataObj.getString("handler"));
							attributeData.setSort(i);
							processTaskFormAttributeDataList.add(attributeData);
							processTaskMapper.replaceProcessTaskFormAttributeData(attributeData);
						}
						processTaskFormAttributeDataList.sort(ProcessTaskFormAttributeDataVo::compareTo);
						paramObj.put(ProcessTaskAuditDetailType.FORM.getParamName(), JSON.toJSONString(processTaskFormAttributeDataList));
					}
				}

				if (this.getMode().equals(ProcessStepMode.MT)) {
					if (processTaskStepAction == ProcessTaskStepAction.COMPLETE) {
						DataValid.formAttributeDataValid(currentProcessTaskStepVo);
					}
					/** 更新处理人状态 **/
					ProcessTaskStepUserVo processTaskMajorUser = new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), UserContext.get().getUserUuid());// 兼容automatic作业无用户
					processTaskMajorUser.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
					processTaskMapper.updateProcessTaskStepUserStatus(processTaskMajorUser);
					/** 清空worker表 **/
					processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
				}

				/** 更新步骤状态 **/
				currentProcessTaskStepVo.setStatus(ProcessTaskStatus.SUCCEED.getValue());
				currentProcessTaskStepVo.setIsActive(2);
				updateProcessTaskStepStatus(currentProcessTaskStepVo);

				/** 流转到下一步 **/
				List<ProcessTaskStepVo> nextStepList = getNext(currentProcessTaskStepVo);
				if (nextStepList.size() > 0) {
					for (ProcessTaskStepVo nextStep : nextStepList) {
						IProcessStepHandler nextStepHandler = ProcessStepHandlerFactory.getHandler(nextStep.getHandler());
						nextStep.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
						if (nextStepHandler != null) {
							doNext(new ProcessStepThread(nextStep) {
								@Override
								public void execute() {
									nextStepHandler.active(nextStep);
								}
							});
						}
					}
				} else if (nextStepList.size() == 0 && !processTaskStepVo.getHandler().equals(ProcessStepHandler.END.getHandler())) {
					throw new ProcessTaskException("找不到可流转路径");
				}
				/** 触发通知 **/
				NotifyHandler.notify(currentProcessTaskStepVo, notifyTriggerType);
				
				/** 执行动作 **/
				ActionHandler.action(currentProcessTaskStepVo, notifyTriggerType);
			} catch (ProcessTaskException ex) {
				logger.error(ex.getMessage(), ex);
				currentProcessTaskStepVo.setError(ex.getMessage());
				currentProcessTaskStepVo.setIsActive(0);
				currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
				updateProcessTaskStepStatus(currentProcessTaskStepVo);
				/** 触发通知 **/
				NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.FAILED);
				
				/** 执行动作 **/
				ActionHandler.action(currentProcessTaskStepVo, NotifyTriggerType.FAILED);
			} finally {
				if (ProcessTaskStatus.FAILED.getValue().equals(currentProcessTaskStepVo.getStatus())) {
					/**
					 * 发生异常不能完成当前步骤，执行当前步骤的回退操作
					 */
					IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
					doNext(new ProcessStepThread(currentProcessTaskStepVo) {
						@Override
						public void execute() {
							handler.back(currentProcessTaskStepVo);
						}
					});
				}
				if(StringUtils.isNotBlank(currentProcessTaskStepVo.getError())) {
					currentProcessTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.CAUSE.getParamName(), currentProcessTaskStepVo.getError());
				}
				/** 处理历史记录 **/
				AuditHandler.audit(currentProcessTaskStepVo, processTaskStepAction);
			}
			if (this.getMode().equals(ProcessStepMode.MT)) {
				/** 写入时间审计 **/
				TimeAuditHandler.audit(currentProcessTaskStepVo, processTaskStepAction);
				/** 计算SLA **/
				SlaHandler.calculate(currentProcessTaskStepVo);
			}
		}

		return 1;
	}

	protected abstract int myComplete(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	@Override
	public final int retreat(ProcessTaskStepVo currentProcessTaskStepVo) {
		try {
			// 锁定当前流程
			processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
			ActionRoleChecker.verifyActionAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskStepAction.RETREAT);
			/** 设置当前步骤状态为未开始 **/
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());

			myRetreat(currentProcessTaskStepVo);

			/** 遍历后续节点所有步骤，写入汇聚步骤数据 **/
			resetConvergeInfo(currentProcessTaskStepVo);

			/** 如果当前步骤是二次进入(后续路径已经走过)，则需要对所有后续流转过的步骤都进行挂起操作 **/
			/** 获取当前步骤状态 **/
			List<ProcessTaskStepRelVo> nextTaskStepRelList = processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());
			for (ProcessTaskStepRelVo nextTaskStepRelVo : nextTaskStepRelList) {
				if (nextTaskStepRelVo != null && nextTaskStepRelVo.getIsHit().equals(1)) {
					ProcessTaskStepVo nextProcessTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(nextTaskStepRelVo.getToProcessTaskStepId());
					if (nextProcessTaskStepVo != null) {
						IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(nextProcessTaskStepVo.getHandler());
						// 标记挂起操作来源步骤
						nextProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
						// 标记挂起操作的发起步骤，避免出现死循环
						nextProcessTaskStepVo.setStartProcessTaskStepId(currentProcessTaskStepVo.getId());
						if (handler != null) {
							doNext(new ProcessStepThread(nextProcessTaskStepVo) {
								@Override
								public void execute() {
									handler.hang(nextProcessTaskStepVo);
								}
							});
						}
					}
				}
				// 恢复路径命中状态为0，代表路径未通过
				processTaskMapper.updateProcessTaskStepRelIsHit(currentProcessTaskStepVo.getId(), nextTaskStepRelVo.getToProcessTaskStepId(), 0);
			}

			/** 分配处理人 **/
			assign(currentProcessTaskStepVo);
			currentProcessTaskStepVo.setIsActive(1);
			updateProcessTaskStepStatus(currentProcessTaskStepVo);

			/** 写入时间审计 **/
			TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.RETREAT);
			if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
				TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.START);
			}

			/** 计算SLA并触发超时警告 **/
			SlaHandler.calculate(currentProcessTaskStepVo);

			/** 触发通知 **/
			NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.RETREAT);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, NotifyTriggerType.RETREAT);
		} catch (ProcessTaskException ex) {
			logger.error(ex.getMessage(), ex);
			currentProcessTaskStepVo.setError(ex.getMessage());
			currentProcessTaskStepVo.setIsActive(1);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		}finally{			
			/** 处理历史记录 **/
			AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.RETREAT);		
		}
		return 1;
	}

	protected abstract int myRetreat(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	@Override
	public final int abortProcessTask(ProcessTaskVo currentProcessTaskVo) {
		// 锁定当前流程
		processTaskMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
		/** 校验权限 **/
		ActionRoleChecker.verifyActionAuthoriy(currentProcessTaskVo.getId(), ProcessTaskStepAction.ABORT);

		List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskId(currentProcessTaskVo.getId());
		for (ProcessTaskStepVo stepVo : processTaskStepList) {
			/** 找到所有已激活步骤，执行终止操作 **/
			if (stepVo.getIsActive().equals(1)) {
				IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(stepVo.getHandler());
				if (handler != null) {
					handler.abort(stepVo);
				} else {
					throw new ProcessStepHandlerNotFoundException(stepVo.getHandler());
				}
			}
		}

		/** 更新流程作业状态 **/
		updateProcessTaskStatus(currentProcessTaskVo.getId());

		/** 处理历史记录 **/
		ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
		processTaskStepVo.setProcessTaskId(currentProcessTaskVo.getId());
		AuditHandler.audit(processTaskStepVo, ProcessTaskStepAction.ABORT);

		return 1;
	}

	@Override
	public int abort(ProcessTaskStepVo currentProcessTaskStepVo) {
		/** 组件完成动作 **/
		myAbort(currentProcessTaskStepVo);

		/** 清空worker表，确保没人可以处理当前步骤 **/
		processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));

		/** 修改步骤状态 **/
		currentProcessTaskStepVo.setIsActive(-1);
		processTaskMapper.updateProcessTaskStepStatus(currentProcessTaskStepVo);

		/** 写入时间审计 **/
		TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.ABORT);

		/** 计算SLA **/
		SlaHandler.calculate(currentProcessTaskStepVo);

		/** 触发通知 **/
		NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.ABORT);
		
		/** 执行动作 **/
		ActionHandler.action(currentProcessTaskStepVo, NotifyTriggerType.ABORT);
		return 1;
	}

	/** 由于abort属于批量自动操作，不抛出任何受检异常 **/
	protected abstract int myAbort(ProcessTaskStepVo currentProcessTaskStepVo);

	@Override
	public final int recoverProcessTask(ProcessTaskVo currentProcessTaskVo) {
		// 锁定当前流程
		processTaskMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
		/** 校验权限 **/
		ActionRoleChecker.verifyActionAuthoriy(currentProcessTaskVo.getId(), ProcessTaskStepAction.RECOVER);

		List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskId(currentProcessTaskVo.getId());
		for (ProcessTaskStepVo stepVo : processTaskStepList) {
			/** 找到所有已终止步骤，执行终止操作 **/
			if (stepVo.getIsActive().equals(-1)) {
				IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(stepVo.getHandler());
				if (handler != null) {
					handler.recover(stepVo);
				}
			}
		}

		/** 更新流程作业状态 **/
		updateProcessTaskStatus(currentProcessTaskVo.getId());

		/** 处理历史记录 **/
		ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
		processTaskStepVo.setProcessTaskId(currentProcessTaskVo.getId());
		AuditHandler.audit(processTaskStepVo, ProcessTaskStepAction.RECOVER);
		return 1;
	}

	@Override
	public int recover(ProcessTaskStepVo currentProcessTaskStepVo) {
		// 锁定当前流程
		processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
		// 获取步骤基本信息
		currentProcessTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
		if (currentProcessTaskStepVo.getIsActive().equals(-1)) {
			/** 组件完成动作 **/
			myRecover(currentProcessTaskStepVo);
			currentProcessTaskStepVo.setIsActive(1);
			/** 如果已经存在过处理人，则继续使用旧处理人，否则重新分派 **/
			List<ProcessTaskStepUserVo> oldUserList = processTaskMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
			if (oldUserList.size() > 0) {
				for (ProcessTaskStepUserVo oldUserVo : oldUserList) {
					processTaskMapper.insertProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), oldUserVo.getUserUuid(), ProcessUserType.MAJOR.getValue()));
				}
			} else {
				/** 分配处理人 **/
				try {
					assign(currentProcessTaskStepVo);
				} catch (ProcessTaskException e) {
					logger.error(e.getMessage(), e);
					currentProcessTaskStepVo.setIsActive(-1);
					currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
					currentProcessTaskStepVo.setError(e.getMessage());
				}
			}

			/** 修改步骤状态 **/
			processTaskMapper.updateProcessTaskStepStatus(currentProcessTaskStepVo);

			/** 写入时间审计 **/
			TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.RECOVER);

			/** 计算SLA **/
			SlaHandler.calculate(currentProcessTaskStepVo);

			/** 触发通知 **/
			NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.RECOVER);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, NotifyTriggerType.RECOVER);
		}
		return 1;
	}

	/** 由于recover属于批量自动操作，不抛出任何受检异常 **/
	protected abstract int myRecover(ProcessTaskStepVo currentProcessTaskStepVo);

	@Override
	public final int accept(ProcessTaskStepVo currentProcessTaskStepVo) {
		try {
			// 锁定当前流程
			processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
			/** 校验权限 **/
			ActionRoleChecker.verifyActionAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskStepAction.ACCEPT);
			
			/** 清空worker表，只留下当前处理人 **/
			processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
			processTaskMapper.insertProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), UserContext.get().getUserUuid(true), ProcessUserType.MAJOR.getValue()));

			/** 删除user表主处理人，更换为当前处理人 **/
			ProcessTaskStepUserVo processTaskStepUser = new ProcessTaskStepUserVo();
			processTaskStepUser.setProcessTaskStepId(currentProcessTaskStepVo.getId());
			processTaskStepUser.setUserType(ProcessUserType.MAJOR.getValue());
			processTaskMapper.deleteProcessTaskStepUser(processTaskStepUser);
			ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
			processTaskStepUserVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
			processTaskStepUserVo.setUserUuid(UserContext.get().getUserUuid(true));
			processTaskStepUserVo.setUserName(UserContext.get().getUserName());
			processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());
			processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DOING.getValue());
			processTaskMapper.insertProcessTaskStepUser(processTaskStepUserVo);

			/** 处理历史记录 **/
			// AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.ACCEPT);

			/** 触发通知 **/
			// NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.ACCEPT);
			
		} catch (ProcessTaskRuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			currentProcessTaskStepVo.setError(ex.getMessage());
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		}
		return 0;
	}

	@Override
	public final int transfer(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepWorkerVo> workerList) {
		// 锁定当前流程
		processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

		if (workerList == null || workerList.size() <= 0) {
			throw new ProcessTaskRuntimeException("请选择需要转交的处理人、处理组或角色");
		}

		/** 校验权限 **/
		ActionRoleChecker.verifyActionAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskStepAction.TRANSFER);

		ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
		processTaskStepVo.setParamObj(currentProcessTaskStepVo.getParamObj());
		/** 检查步骤是否 “已激活” **/
		if (!processTaskStepVo.getIsActive().equals(1)) {
			throw new ProcessTaskStepUnActivedException();
		}

		/** 根据子类需要把最终处理人放进来，引擎将自动写入数据库，也可能为空，例如一些特殊的流程节点 **/
		List<ProcessTaskStepUserVo> userList = new ArrayList<>();

		try {
			if (workerList.size() == 1) {
				String workerUserUuid = workerList.get(0).getUuid();
				if (GroupSearch.USER.getValue().equals(workerList.get(0).getType()) && StringUtils.isNotBlank(workerUserUuid)) {
					List<ProcessTaskStepUserVo> oldUserList = processTaskMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
					if (oldUserList.size() > 0) {
						for (ProcessTaskStepUserVo oldUser : oldUserList) {
							if (workerUserUuid.equals(oldUser.getUserUuid())) {
								throw new ProcessTaskStepUserIsExistsException(oldUser.getUserName());
							}
						}
					}
				}
			}

			/** 默认状态设为pending，但子类可以选择置为start **/
			processTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
			myTransfer(processTaskStepVo, workerList, userList);

			/** 清空work表和user表，重新写入新数据 **/
			ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
			processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
			processTaskStepUserVo.setUserType(null);
			processTaskMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
			processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
			if (workerList != null && workerList.size() > 0) {
				for (ProcessTaskStepWorkerVo workerVo : workerList) {
					processTaskMapper.insertProcessTaskStepWorker(workerVo);
				}
			}
			if (userList != null && userList.size() > 0) {
				for (ProcessTaskStepUserVo userVo : userList) {
					processTaskMapper.insertProcessTaskStepUser(userVo);
				}
			}

			updateProcessTaskStepStatus(processTaskStepVo);

			/** 触发通知 **/
			NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.TRANSFER);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, NotifyTriggerType.TRANSFER);

			/** 处理时间审计 **/
			TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.TRANSFER);
		} catch (ProcessTaskException e) {
			logger.error(e.getMessage(), e);
			processTaskStepVo.setError(e.getMessage());
			processTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			updateProcessTaskStepStatus(processTaskStepVo);
		} finally {
			/** 处理历史记录 **/
			AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.TRANSFER);
		}

		return 0;
	}

	protected abstract int myTransfer(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepWorkerVo> workerList, List<ProcessTaskStepUserVo> userList) throws ProcessTaskException;

	/**
	 * back操作不允许出现任何异常，所有异常都必须解决以便流程可以顺利回退，否则流程可能会卡死在某个节点不能前进或后退
	 */
	@Override
	public final int back(ProcessTaskStepVo currentProcessTaskStepVo) {
		try {
			// 锁定当前流程
			processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

			myBack(currentProcessTaskStepVo);

			// 获取来源路径
			List<ProcessTaskStepRelVo> fromProcessTaskStepRelList = processTaskMapper.getProcessTaskStepRelByToId(currentProcessTaskStepVo.getId());

			for (ProcessTaskStepRelVo processTaskStepRelVo : fromProcessTaskStepRelList) {
				// 沿着流转过的路径向前找节点并重新激活
				if (!processTaskStepRelVo.getIsHit().equals(0)) {
					ProcessTaskStepVo fromProcessTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepRelVo.getFromProcessTaskStepId());
					if (fromProcessTaskStepVo != null) {
						// 如果是分流节点或条件节点，则再次调用back查找上一个处理节点
						if (fromProcessTaskStepVo.getHandler().equals(ProcessStepHandler.DISTRIBUTARY.getHandler()) || fromProcessTaskStepVo.getHandler().equals(ProcessStepHandler.CONDITION.getHandler())) {
							IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(fromProcessTaskStepVo.getHandler());
							if (handler != null) {
								fromProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
								doNext(new ProcessStepThread(fromProcessTaskStepVo) {
									@Override
									public void execute() {
										handler.back(fromProcessTaskStepVo);
									}
								});
							}
						} else {
							// 如果是处理节点，则重新激活
							IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(fromProcessTaskStepVo.getHandler());
							if (handler != null) {
								fromProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
								doNext(new ProcessStepThread(fromProcessTaskStepVo) {
									@Override
									public void execute() {
										handler.active(fromProcessTaskStepVo);
									}
								});
							}
						}
					}
				}
			}
//			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.BACK.getValue());
//			currentProcessTaskStepVo.setIsActive(0);
//			updateProcessTaskStepStatus(currentProcessTaskStepVo);
			/** 处理时间审计 **/
			TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.BACK);

			/** 触发通知 **/
			NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.BACK);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, NotifyTriggerType.BACK);
		} catch (ProcessTaskException e) {
			logger.error(e.getMessage(), e);
			currentProcessTaskStepVo.setError(e.getMessage());
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		}
		/** 处理历史记录 **/
		// AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.BACK);

		/** 计算SLA **/
		SlaHandler.calculate(currentProcessTaskStepVo);
		return 1;
	}

	protected abstract int myBack(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	@Override
	public final int saveDraft(ProcessTaskStepVo currentProcessTaskStepVo) {
		JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
		Long processTaskId = paramObj.getLong("processTaskId");
		if (processTaskId == null) {// 首次保存

			ProcessTaskVo processTaskVo = new ProcessTaskVo();
			processTaskVo.setTitle(paramObj.getString("title"));
			processTaskVo.setOwner(paramObj.getString("owner"));
			processTaskVo.setChannelUuid(paramObj.getString("channelUuid"));
			processTaskVo.setPriorityUuid(paramObj.getString("priorityUuid"));
			processTaskVo.setProcessUuid(currentProcessTaskStepVo.getProcessUuid());
			processTaskVo.setReporter(UserContext.get().getUserUuid(true));
			processTaskVo.setStatus(ProcessTaskStatus.DRAFT.getValue());

			ProcessVo processVo = processMapper.getProcessByUuid(currentProcessTaskStepVo.getProcessUuid());
			/** 对流程配置进行散列处理 **/
			if (StringUtils.isNotBlank(processVo.getConfig())) {
				String hash = DigestUtils.md5DigestAsHex(processVo.getConfig().getBytes());
				processTaskVo.setConfigHash(hash);
				processTaskMapper.replaceProcessTaskConfig(new ProcessTaskConfigVo(hash, processVo.getConfig()));
			}
			ChannelVo channelVo = channelMapper.getChannelByUuid(processTaskVo.getChannelUuid());
			processTaskVo.setWorktimeUuid(channelVo.getWorktimeUuid());
			/** 创建工单 **/
			processTaskMapper.insertProcessTask(processTaskVo);
			currentProcessTaskStepVo.setProcessTaskId(processTaskVo.getId());

			/** 写入表单信息 **/
			if (StringUtils.isNotBlank(processVo.getFormUuid())) {
				FormVersionVo formVersionVo = formMapper.getActionFormVersionByFormUuid(processVo.getFormUuid());
				if (formVersionVo != null && StringUtils.isNotBlank(formVersionVo.getFormConfig())) {
					ProcessTaskFormVo processTaskFormVo = new ProcessTaskFormVo();
					processTaskFormVo.setFormContent(formVersionVo.getFormConfig());
					processTaskFormVo.setProcessTaskId(processTaskVo.getId());
					processTaskFormVo.setFormUuid(formVersionVo.getFormUuid());
					processTaskFormVo.setFormName(formVersionVo.getFormName());
					processTaskMapper.insertProcessTaskForm(processTaskFormVo);
					processTaskMapper.replaceProcessTaskFormContent(processTaskFormVo);
				}
			}

			Map<String, Long> stepIdMap = new HashMap<>();
			/** 写入所有步骤信息 **/
			List<ProcessStepVo> processStepList = processMapper.getProcessStepDetailByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
			if (processStepList != null && processStepList.size() > 0) {
				for (ProcessStepVo stepVo : processStepList) {
					ProcessTaskStepVo ptStepVo = new ProcessTaskStepVo(stepVo);
					ptStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
					ptStepVo.setProcessTaskId(processTaskVo.getId());
					if (StringUtils.isNotBlank(ptStepVo.getConfig())) {
						/** 对步骤配置进行散列处理 **/
						String hash = DigestUtils.md5DigestAsHex(ptStepVo.getConfig().getBytes());
						ptStepVo.setConfigHash(hash);
						processTaskMapper.replaceProcessTaskStepConfig(new ProcessTaskStepConfigVo(hash, ptStepVo.getConfig()));
					}

					processTaskMapper.insertProcessTaskStep(ptStepVo);
					stepIdMap.put(ptStepVo.getProcessStepUuid(), ptStepVo.getId());

					/** 写入步骤表单属性 **/
					if (ptStepVo.getFormAttributeList() != null && ptStepVo.getFormAttributeList().size() > 0) {
						for (ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo : ptStepVo.getFormAttributeList()) {
							processTaskStepFormAttributeVo.setProcessTaskId(processTaskVo.getId());
							processTaskStepFormAttributeVo.setProcessTaskStepId(ptStepVo.getId());
							processTaskMapper.insertProcessTaskStepFormAttribute(processTaskStepFormAttributeVo);
						}
					}

					/** 写入用户分配策略信息 **/
					if (ptStepVo.getWorkerPolicyList() != null && ptStepVo.getWorkerPolicyList().size() > 0) {
						for (ProcessTaskStepWorkerPolicyVo policyVo : ptStepVo.getWorkerPolicyList()) {
							policyVo.setProcessTaskId(processTaskVo.getId());
							policyVo.setProcessTaskStepId(ptStepVo.getId());
							processTaskMapper.insertProcessTaskStepWorkerPolicy(policyVo);
						}
					}

					/** 写入超时策略信息 **/
//					if (ptStepVo.getTimeoutPolicyList() != null && ptStepVo.getTimeoutPolicyList().size() > 0) {
//						for (ProcessTaskStepTimeoutPolicyVo policyVo : ptStepVo.getTimeoutPolicyList()) {
//							policyVo.setProcessTaskId(processTaskVo.getId());
//							policyVo.setProcessTaskStepId(ptStepVo.getId());
//							processTaskMapper.insertProcessTaskStepTimeoutPolicy(policyVo);
//						}
//					}

					/** 找到开始节点 **/
					if (stepVo.getType().equals(ProcessStepType.START.getValue())) {
						currentProcessTaskStepVo.setId(ptStepVo.getId());
					}
				}
			}

			Map<Long, NotifyPolicyVo> notifyPolicyMap = new HashMap<>();
			NotifyPolicyInvokerVo notifyPolicyInvokerVo = new NotifyPolicyInvokerVo();
			notifyPolicyInvokerVo.setInvoker(currentProcessTaskStepVo.getProcessUuid());
			notifyPolicyInvokerVo.setNeedPage(false);
			List<NotifyPolicyInvokerVo> notifyPolicyInvokerList = notifyMapper.getNotifyPolicyInvokerList(notifyPolicyInvokerVo);
			for (NotifyPolicyInvokerVo notifyPolicyInvoker : notifyPolicyInvokerList) {
				JSONObject config = notifyPolicyInvoker.getConfig();
				if (MapUtils.isNotEmpty(config)) {
					String processStepUuid = config.getString("processStepUuid");
					if (StringUtils.isNotBlank(processStepUuid)) {
						Long stepId = stepIdMap.get(processStepUuid);
						if (stepId != null) {
							NotifyPolicyVo notifyPolicyVo = notifyPolicyMap.get(notifyPolicyInvoker.getPolicyId());
							if (notifyPolicyVo == null) {
								notifyPolicyVo = notifyMapper.getNotifyPolicyById(notifyPolicyInvoker.getPolicyId());
								if (notifyPolicyVo == null) {
									continue;
								} else {
									notifyPolicyMap.put(notifyPolicyVo.getId(), notifyPolicyVo);
								}
							}
							ProcessTaskStepNotifyPolicyVo processTaskStepNotifyPolicyVo = new ProcessTaskStepNotifyPolicyVo();
							processTaskStepNotifyPolicyVo.setProcessTaskStepId(stepId);
							processTaskStepNotifyPolicyVo.setPolicyId(notifyPolicyInvoker.getPolicyId());
							processTaskStepNotifyPolicyVo.setPolicyName(notifyPolicyVo.getName());
							processTaskStepNotifyPolicyVo.setPolicyConfig(notifyPolicyVo.getConfigStr());
							processTaskMapper.replaceProcessTaskStepNotifyPolicyConfig(processTaskStepNotifyPolicyVo);
							processTaskMapper.insertProcessTaskStepNotifyPolicy(processTaskStepNotifyPolicyVo);
						}
					}
				}
			}

			/** 写入关系信息 **/
			List<ProcessStepRelVo> processStepRelList = processMapper.getProcessStepRelByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
			if (processStepRelList != null && processStepRelList.size() > 0) {
				for (ProcessStepRelVo relVo : processStepRelList) {
					ProcessTaskStepRelVo processTaskStepRelVo = new ProcessTaskStepRelVo(relVo);
					processTaskStepRelVo.setProcessTaskId(processTaskVo.getId());
					processTaskStepRelVo.setFromProcessTaskStepId(stepIdMap.get(processTaskStepRelVo.getFromProcessStepUuid()));
					processTaskStepRelVo.setToProcessTaskStepId(stepIdMap.get(processTaskStepRelVo.getToProcessStepUuid()));
					/** 同时找到from step id 和to step id 时才写入，其他数据舍弃 **/
					if (processTaskStepRelVo.getFromProcessTaskStepId() != null && processTaskStepRelVo.getToProcessTaskStepId() != null) {
						processTaskMapper.insertProcessTaskStepRel(processTaskStepRelVo);
					}
				}
			}

			/** 写入sla信息 **/
			List<ProcessSlaVo> processSlaList = processMapper.getProcessSlaByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
			if (processSlaList != null && processSlaList.size() > 0) {
				for (ProcessSlaVo slaVo : processSlaList) {
					List<String> slaStepUuidList = processMapper.getProcessStepUuidBySlaUuid(slaVo.getUuid());
					if (slaStepUuidList.size() > 0) {
						ProcessTaskSlaVo processTaskSlaVo = new ProcessTaskSlaVo(slaVo);
						processTaskSlaVo.setProcessTaskId(processTaskVo.getId());
						processTaskMapper.insertProcessTaskSla(processTaskSlaVo);
						for (String suuid : slaStepUuidList) {
							processTaskMapper.insertProcessTaskStepSla(stepIdMap.get(suuid), processTaskSlaVo.getId());
						}
					}
				}
			}

			/** 加入上报人为处理人 **/
			ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), UserContext.get().getUserUuid(true));
			processTaskStepUserVo.setUserName(UserContext.get().getUserName());
			processTaskMapper.insertProcessTaskStepUser(processTaskStepUserVo);
			processTaskMapper.insertProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), UserContext.get().getUserUuid(true), ProcessUserType.MAJOR.getValue()));

		} else {
			/** 锁定当前流程 **/
			processTaskMapper.getProcessTaskLockById(processTaskId);
			// 第二次保存时的操作
			ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskById(processTaskId);
			if (!ProcessTaskStatus.DRAFT.getValue().equals(processTaskVo.getStatus())) {
				throw new ProcessTaskRuntimeException("工单非草稿状态，不能进行上报暂存操作");
			}
		}
		try {

			// 组件联动导致隐藏的属性uuid列表
			ProcessTaskStepDataVo processTaskStepDataVo = new ProcessTaskStepDataVo();
			processTaskStepDataVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			processTaskStepDataVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
			processTaskStepDataVo.setType(ProcessTaskStepDataType.STEPDRAFTSAVE.getValue());
			processTaskStepDataVo.setFcu(UserContext.get().getUserUuid(true));
			processTaskStepDataMapper.deleteProcessTaskStepData(processTaskStepDataVo);
			processTaskStepDataVo.setData(paramObj.toJSONString());
			processTaskStepDataVo.setIsAutoGenerateId(true);
			processTaskStepDataMapper.replaceProcessTaskStepData(processTaskStepDataVo);

			List<String> hidecomponentList = JSON.parseArray(paramObj.getString("hidecomponentList"), String.class);

			/** 写入当前步骤的表单属性值 **/
			JSONArray formAttributeDataList = paramObj.getJSONArray("formAttributeDataList");
			if (CollectionUtils.isNotEmpty(formAttributeDataList)) {
				// 表单属性显示控制
				Map<String, String> formAttributeActionMap = new HashMap<>();
				List<ProcessTaskStepFormAttributeVo> processTaskStepFormAttributeList = processTaskMapper.getProcessTaskStepFormAttributeByProcessTaskStepId(currentProcessTaskStepVo.getId());
				if (processTaskStepFormAttributeList.size() > 0) {
					for (ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo : processTaskStepFormAttributeList) {
						formAttributeActionMap.put(processTaskStepFormAttributeVo.getAttributeUuid(), processTaskStepFormAttributeVo.getAction());
					}
				}
				for (int i = 0; i < formAttributeDataList.size(); i++) {
					JSONObject formAttributeDataObj = formAttributeDataList.getJSONObject(i);
					String attributeUuid = formAttributeDataObj.getString("attributeUuid");
					if (formAttributeActionMap.get(attributeUuid) != null) {// 对于只读或隐藏的属性，当前用户不能修改，不更新数据库中的值，不进行修改前后对比
						continue;
					}
					if (hidecomponentList.contains(attributeUuid)) {
						continue;
					}
					ProcessTaskFormAttributeDataVo attributeData = new ProcessTaskFormAttributeDataVo();
					attributeData.setData(formAttributeDataObj.getString("dataList"));
					attributeData.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
					attributeData.setAttributeUuid(formAttributeDataObj.getString("attributeUuid"));
					attributeData.setType(formAttributeDataObj.getString("handler"));
					attributeData.setSort(i);
					processTaskMapper.replaceProcessTaskFormAttributeData(attributeData);
				}
			}
			mySaveDraft(currentProcessTaskStepVo);
			currentProcessTaskStepVo.setIsActive(1);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.DRAFT.getValue());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		} catch (ProcessTaskException ex) {
			logger.error(ex.getMessage(), ex);
			currentProcessTaskStepVo.setIsActive(1);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			currentProcessTaskStepVo.setError(ex.getMessage());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		}

		return 1;
	}

	protected abstract int mySaveDraft(ProcessTaskStepVo processTaskStepVo) throws ProcessTaskException;

	@Override
	public final int startProcess(ProcessTaskStepVo currentProcessTaskStepVo) {
		try {
			// 锁定当前流程
			processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
			ActionRoleChecker.verifyActionAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskStepAction.STARTPROCESS);
			DataValid.formAttributeDataValid(currentProcessTaskStepVo);
			myStartProcess(currentProcessTaskStepVo);
			// 获取表单数据
			List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			if (CollectionUtils.isNotEmpty(processTaskFormAttributeDataList)) {
				processTaskFormAttributeDataList.sort(ProcessTaskFormAttributeDataVo::compareTo);
				JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
				paramObj.put(ProcessTaskAuditDetailType.FORM.getParamName(), JSON.toJSONString(processTaskFormAttributeDataList));
			}
			/** 更新处理人状态 **/
			ProcessTaskStepUserVo processTaskMajorUser = new ProcessTaskStepUserVo(currentProcessTaskStepVo.getId(), UserContext.get().getUserUuid(true));
			processTaskMajorUser.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
			processTaskMajorUser.setUserType(ProcessUserType.MAJOR.getValue());
			processTaskMapper.updateProcessTaskStepUserStatus(processTaskMajorUser);
			/** 清空worker表 **/
			processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
			currentProcessTaskStepVo.setIsActive(2);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.SUCCEED.getValue());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
			
			/** 流转到下一步 **/
			List<ProcessTaskStepVo> nextStepList = getNext(currentProcessTaskStepVo);
			for (ProcessTaskStepVo nextStep : nextStepList) {
				IProcessStepHandler nextStepHandler = ProcessStepHandlerFactory.getHandler(nextStep.getHandler());
				nextStep.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
				if (nextStepHandler != null) {
					doNext(new ProcessStepThread(nextStep) {
						@Override
						public void execute() {
							nextStepHandler.active(nextStep);
						}

					});
				}
			}
		} catch (ProcessTaskException ex) {
			logger.error(ex.getMessage(), ex);
			currentProcessTaskStepVo.setIsActive(1);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			currentProcessTaskStepVo.setError(ex.getMessage());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		} finally {
			/** 处理历史记录 **/
			AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.STARTPROCESS);
			
		}
		return 0;
	}

	protected abstract int myStartProcess(ProcessTaskStepVo processTaskStepVo) throws ProcessTaskException;

	@Override
	public final List<ProcessTaskStepVo> getNext(ProcessTaskStepVo currentProcessTaskStepVo) {
		List<ProcessTaskStepRelVo> relList = processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());
		// 重置所有关系状态为-1
		for (ProcessTaskStepRelVo rel : relList) {
			processTaskMapper.updateProcessTaskStepRelIsHit(rel.getFromProcessTaskStepId(), rel.getToProcessTaskStepId(), -1);
		}
		currentProcessTaskStepVo.setRelList(relList);

		List<ProcessTaskStepVo> nextStepList = null;
		try {
			nextStepList = myGetNext(currentProcessTaskStepVo);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex.getMessage() != null && !ex.getMessage().equals("")) {
				currentProcessTaskStepVo.appendError(ex.getMessage());
			} else {
				currentProcessTaskStepVo.appendError(ExceptionUtils.getStackTrace(ex));
			}
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
		}
		if (nextStepList != null && nextStepList.size() > 0) {
			Iterator<ProcessTaskStepVo> stepIter = nextStepList.iterator();
			Set<Long> checkSet = new HashSet<Long>();
			while (stepIter.hasNext()) {// 去掉重复的路径
				ProcessTaskStepVo step = stepIter.next();
				if (!checkSet.contains(step.getId())) {
					checkSet.add(step.getId());
				} else {
					stepIter.remove();
				}
			}
		}
		/** 更新路径isHit=1，在active方法里需要根据isHit状态判断路径是否经通过 **/
		if (nextStepList == null) {
			nextStepList = new ArrayList<>();
		}
		for (ProcessTaskStepVo stepVo : nextStepList) {
			processTaskMapper.updateProcessTaskStepRelIsHit(currentProcessTaskStepVo.getId(), stepVo.getId(), 1);
		}
		return nextStepList;
	}

	private void resetConvergeInfo(ProcessTaskStepVo nextStepVo) {

		List<ProcessTaskStepVo> stepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(nextStepVo.getProcessTaskId(), ProcessStepType.END.getValue());
		ProcessTaskStepVo endStepVo = null;
		if (stepList.size() == 1) {
			endStepVo = stepList.get(0);
		}
		// 重新插入汇聚数据
		List<List<Long>> routeList = new ArrayList<>();
		List<Long> routeStepList = new ArrayList<>();
		routeList.add(routeStepList);

		getAllRouteList(nextStepVo.getId(), routeList, routeStepList, endStepVo);
		// 如果最后一个步骤不是结束节点的路由全部删掉，因为这是回环路由
		Iterator<List<Long>> routeStepIt = routeList.iterator();
		List<Long> convergeIdList = new ArrayList<>();
		while (routeStepIt.hasNext()) {
			List<Long> rsList = routeStepIt.next();
			if (!rsList.get(rsList.size() - 1).equals(endStepVo.getId())) {
				routeStepIt.remove();
			} else {
				for (Long cid : rsList) {
					if (!convergeIdList.contains(cid) && !cid.equals(nextStepVo.getId())) {
						convergeIdList.add(cid);
					}
				}
			}
		}
		if (convergeIdList.size() > 0) {
			for (Long convergeId : convergeIdList) {
				ProcessTaskConvergeVo processTaskStepConvergeVo = new ProcessTaskConvergeVo(nextStepVo.getProcessTaskId(), nextStepVo.getId(), convergeId);
				if (processTaskMapper.checkProcessTaskConvergeIsExists(processTaskStepConvergeVo) == 0) {
					processTaskMapper.insertProcessTaskConverge(processTaskStepConvergeVo);
				}
			}
		}
	}

	private void getAllRouteList(Long processTaskStepId, List<List<Long>> routeList, List<Long> routeStepList, ProcessTaskStepVo endStepVo) {
		if (!routeStepList.contains(processTaskStepId)) {
			routeStepList.add(processTaskStepId);
			List<Long> tmpRouteStepList = new ArrayList<>(routeStepList);
			if (!processTaskStepId.equals(endStepVo.getId())) {
				List<ProcessTaskStepVo> convergeStepList = processTaskMapper.getProcessTaskStepByConvergeId(processTaskStepId);
				List<ProcessTaskStepVo> toProcessTaskStepList = processTaskMapper.getToProcessTaskStepByFromIdAndType(processTaskStepId,null);
				for (int i = 0; i < toProcessTaskStepList.size(); i++) {
					ProcessTaskStepVo toProcessTaskStepVo = toProcessTaskStepList.get(i);
					/** 当前节点不是别人的汇聚节点时，才记录进路由，这是为了避免因为出现打回路径而产生错误的汇聚数据 **/
					if (!convergeStepList.contains(toProcessTaskStepVo)) {
						if (i > 0) {
							List<Long> newRouteStepList = new ArrayList<>(tmpRouteStepList);
							routeList.add(newRouteStepList);
							getAllRouteList(toProcessTaskStepVo.getId(), routeList, newRouteStepList, endStepVo);
						} else {
							getAllRouteList(toProcessTaskStepVo.getId(), routeList, routeStepList, endStepVo);
						}
					}
				}
			}
		}
	}

	protected abstract List<ProcessTaskStepVo> myGetNext(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	protected synchronized static void doNext(ProcessStepThread thread) {
		if (!TransactionSynchronizationManager.isSynchronizationActive()) {
			CachedThreadPool.execute(thread);
		} else {
			List<ProcessStepThread> runableActionList = PROCESS_STEP_RUNNABLES.get();
			if (runableActionList == null) {
				runableActionList = new ArrayList<>();
				PROCESS_STEP_RUNNABLES.set(runableActionList);
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						List<ProcessStepThread> runableActionList = PROCESS_STEP_RUNNABLES.get();
						for (int i = 0; i < runableActionList.size(); i++) {
							ProcessStepThread runnable = runableActionList.get(i);
							CachedThreadPool.execute(runnable);
						}
					}

					@Override
					public void afterCompletion(int status) {
						PROCESS_STEP_RUNNABLES.remove();
					}
				});
			}
			runableActionList.add(thread);
		}
	}

	@Override
	public void activityAudit(ProcessTaskStepVo currentProcessTaskStepVo, ProcessTaskStepAction action) {
		AuditHandler.audit(currentProcessTaskStepVo, action);
	}

	@Override
	public List<String> getProcessTaskStepActionList(Long processTaskId, Long processTaskStepId) {
		return ActionRoleChecker.getProcessTaskStepActionList(processTaskId, processTaskStepId);
	}

	@Override
	public List<String> getProcessTaskStepActionList(Long processTaskId, Long processTaskStepId, List<String> verifyActionList) {
		return ActionRoleChecker.getProcessTaskStepActionList(processTaskId, processTaskStepId, verifyActionList);
	}

	@Override
	public boolean verifyActionAuthoriy(Long processTaskId, Long processTaskStepId, ProcessTaskStepAction action) {
		return ActionRoleChecker.verifyActionAuthoriy(processTaskId, processTaskStepId, action);
	}

	@Override
	public List<ProcessTaskStepVo> getProcessableStepList(Long processTaskId) {
		return ActionRoleChecker.getProcessableStepList(processTaskId);
	}

	@Override
	public Set<ProcessTaskStepVo> getRetractableStepList(Long processTaskId) {
		return ActionRoleChecker.getRetractableStepListByProcessTaskId(processTaskId);
	}

	@Override
	public List<ProcessTaskStepVo> getUrgeableStepList(Long processTaskId) {
		return ActionRoleChecker.getUrgeableStepList(processTaskId);
	}

	@Override
	public void notify(ProcessTaskStepVo currentProcessTaskStepVo, NotifyTriggerType trigger) {
		NotifyHandler.notify(currentProcessTaskStepVo, trigger);
	}

	public boolean verifyOperationAuthoriy(Long processTaskId, Long processTaskStepId, OperationType operation) {
		return true;
	}

}
