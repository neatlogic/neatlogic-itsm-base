package codedriver.framework.process.stephandler.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
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
import com.alibaba.fastjson.JSONPath;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.asynchronization.threadpool.CachedThreadPool;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.notify.dto.NotifyPolicyInvokerVo;
import codedriver.framework.notify.dto.NotifyPolicyVo;
import codedriver.framework.process.constvalue.ProcessStepHandlerType;
import codedriver.framework.process.constvalue.ProcessStepMode;
import codedriver.framework.process.constvalue.ProcessStepType;
import codedriver.framework.process.constvalue.ProcessTaskAuditDetailType;
import codedriver.framework.process.constvalue.ProcessTaskAuditType;
import codedriver.framework.process.constvalue.ProcessTaskOperationType;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessTaskStepRemindType;
import codedriver.framework.process.constvalue.ProcessTaskStepUserStatus;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.FormAttributeVo;
import codedriver.framework.process.dto.FormVersionVo;
import codedriver.framework.process.dto.ProcessSlaVo;
import codedriver.framework.process.dto.ProcessStepRelVo;
import codedriver.framework.process.dto.ProcessStepVo;
import codedriver.framework.process.dto.ProcessTagVo;
import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskContentVo;
import codedriver.framework.process.dto.ProcessTaskConvergeVo;
import codedriver.framework.process.dto.ProcessTaskFormAttributeDataVo;
import codedriver.framework.process.dto.ProcessTaskFormVo;
import codedriver.framework.process.dto.ProcessTaskScoreTemplateConfigVo;
import codedriver.framework.process.dto.ProcessTaskScoreTemplateVo;
import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;
import codedriver.framework.process.dto.ProcessTaskSlaVo;
import codedriver.framework.process.dto.ProcessTaskStepAgentVo;
import codedriver.framework.process.dto.ProcessTaskStepConfigVo;
import codedriver.framework.process.dto.ProcessTaskStepContentVo;
import codedriver.framework.process.dto.ProcessTaskStepFileVo;
import codedriver.framework.process.dto.ProcessTaskStepFormAttributeVo;
import codedriver.framework.process.dto.ProcessTaskStepInOperationVo;
import codedriver.framework.process.dto.ProcessTaskStepNotifyPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepRelVo;
import codedriver.framework.process.dto.ProcessTaskStepRemindVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerPolicyVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskTagVo;
import codedriver.framework.process.dto.ProcessTaskTranferReportVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.dto.ProcessVo;
import codedriver.framework.process.dto.score.ProcessScoreTemplateVo;
import codedriver.framework.process.dto.score.ProcessTaskScoreVo;
import codedriver.framework.process.dto.score.ScoreTemplateDimensionVo;
import codedriver.framework.process.exception.core.ProcessTaskException;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
import codedriver.framework.process.exception.process.ProcessStepHandlerNotFoundException;
import codedriver.framework.process.exception.process.ProcessStepUtilHandlerNotFoundException;
import codedriver.framework.process.exception.processtask.ProcessTaskStepUnActivedException;
import codedriver.framework.process.exception.processtask.ProcessTaskStepUserIsExistsException;
import codedriver.framework.process.exception.processtaskserialnumberpolicy.ProcessTaskSerialNumberPolicyNotFoundException;
import codedriver.framework.process.notify.core.TaskStepNotifyTriggerType;
import codedriver.framework.process.processtaskserialnumberpolicy.core.IProcessTaskSerialNumberPolicyHandler;
import codedriver.framework.process.processtaskserialnumberpolicy.core.ProcessTaskSerialNumberPolicyHandlerFactory;
import codedriver.framework.process.workerpolicy.core.IWorkerPolicyHandler;
import codedriver.framework.process.workerpolicy.core.WorkerPolicyHandlerFactory;
import codedriver.framework.process.notify.core.TaskNotifyTriggerType;

public abstract class ProcessStepHandlerBase extends ProcessStepHandlerUtilBase implements IProcessStepHandler {
	static Logger logger = LoggerFactory.getLogger(ProcessStepHandlerBase.class);
	private static final ThreadLocal<List<ProcessStepThread>> PROCESS_STEP_RUNNABLES = new ThreadLocal<>();

	private int updateProcessTaskStatus(Long processTaskId) {
		List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepBaseInfoByProcessTaskId(processTaskId);

		int runningCount = 0, succeedCount = 0, failedCount = 0, abortedCount = 0, draftCount = 0, hangCount = 0;
		for (ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
			if (ProcessTaskStatus.DRAFT.getValue().equals(processTaskStepVo.getStatus()) && processTaskStepVo.getIsActive().equals(1)) {
				draftCount += 1;
			} else if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.HANG.getValue())) {
			    hangCount += 1;
            } else if (processTaskStepVo.getIsActive().equals(1)) {
				runningCount += 1;
			} else if (processTaskStepVo.getIsActive().equals(-1)) {
				abortedCount += 1;
			} else if (processTaskStepVo.getStatus().equals(ProcessTaskStatus.SUCCEED.getValue()) && ProcessStepHandlerType.END.getHandler().equals(processTaskStepVo.getHandler())) {
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
		} else if(hangCount > 0) {
		    processTaskVo.setStatus(ProcessTaskStatus.HANG.getValue());
		} else {
			return 1;
		}
		processTaskMapper.updateProcessTaskStatus(processTaskVo);
		return 1;
	}
	/**
	 * 
	* @Time:2020年7月28日
	* @Description: 保存描述内容和附件
	* @param currentProcessTaskStepVo 
	* @return void
	 */
	private void saveContentAndFile(ProcessTaskStepVo currentProcessTaskStepVo, ProcessTaskOperationType action) {
	    JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
		String content = paramObj.getString("content");        
        List<Long> fileIdList = JSON.parseArray(JSON.toJSONString(paramObj.getJSONArray("fileIdList")), Long.class);
        if(StringUtils.isBlank(content) && CollectionUtils.isEmpty(fileIdList)) {
            return;
        }
        ProcessTaskStepContentVo processTaskStepContentVo = new ProcessTaskStepContentVo();
        processTaskStepContentVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
        processTaskStepContentVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
        processTaskStepContentVo.setType(action.getValue());
		if (StringUtils.isNotBlank(content)) {
			ProcessTaskContentVo contentVo = new ProcessTaskContentVo(content);
			processTaskMapper.replaceProcessTaskContent(contentVo);
			processTaskStepContentVo.setContentHash(contentVo.getHash());
		}else {
		    paramObj.remove("content");
		}
        processTaskMapper.insertProcessTaskStepContent(processTaskStepContentVo);

        /** 保存附件uuid **/
        if(CollectionUtils.isNotEmpty(fileIdList)) {
            ProcessTaskStepFileVo processTaskStepFileVo = new ProcessTaskStepFileVo();
            processTaskStepFileVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            processTaskStepFileVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
            processTaskStepFileVo.setContentId(processTaskStepContentVo.getId());
            for (Long fileId : fileIdList) {
                processTaskStepFileVo.setFileId(fileId);
                processTaskMapper.insertProcessTaskStepFile(processTaskStepFileVo);
            }
        }else {
            paramObj.remove("fileIdList");
        }
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
				currentProcessTaskStepVo.setUpdateActiveTime(1);
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
								doNext(ProcessTaskOperationType.HANG, new ProcessStepThread(nextProcessTaskStepVo) {
									@Override
									public void myExecute() {
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

	                myActive(currentProcessTaskStepVo);
	                currentProcessTaskStepVo.setIsActive(1);
	                updateProcessTaskStepStatus(currentProcessTaskStepVo);

	                /** 写入时间审计 **/
	                TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.ACTIVE);
	                if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
	                    TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.START);
	                    NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.START);
	                }

	                /** 计算SLA并触发超时警告 **/
	                SlaHandler.calculate(currentProcessTaskStepVo);

	                /** 触发通知 **/
	                NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ACTIVE);
	                
	                /** 执行动作 **/
	                ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ACTIVE);
				} else if (this.getMode().equals(ProcessStepMode.AT)) {
				    myActive(currentProcessTaskStepVo);
                    currentProcessTaskStepVo.setIsActive(1);
                    updateProcessTaskStepStatus(currentProcessTaskStepVo);
					/** 自动处理 **/
					IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
					doNext(ProcessTaskOperationType.HANDLE, new ProcessStepThread(currentProcessTaskStepVo) {
						@Override
						public void myExecute() {
						    handler.handle(currentProcessTaskStepVo);
						}
					});
				}
			}
		} catch (ProcessTaskException e) {
			logger.error(e.getMessage(), e);
			currentProcessTaskStepVo.setIsActive(0);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			currentProcessTaskStepVo.setError(e.getMessage());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
			/** 异常提醒 **/
			ProcessStepUtilHandlerFactory.getHandler().saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getStartProcessTaskStepId(), e.getMessage(), ProcessTaskStepRemindType.ERROR);
		} finally {
			if (ProcessTaskStatus.FAILED.getValue().equals(currentProcessTaskStepVo.getStatus())) {
				/**
				 * 发生异常不能激活当前步骤，执行当前步骤的回退操作
				 */
				IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
				doNext(ProcessTaskOperationType.BACK, new ProcessStepThread(currentProcessTaskStepVo) {
					@Override
					public void myExecute() {
					    handler.back(currentProcessTaskStepVo);
					}
				});
			}
//			deleteProcessTaskStepInOperationByProcessTaskStepId(currentProcessTaskStepVo.getId(), ProcessTaskOperationType.ACTIVE);
		}
		return 1;
	}

	protected abstract int myActive(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	@Override
	public final int assign(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException {
		/** 清空处理人 **/
		List<ProcessTaskStepWorkerVo> workerList = new ArrayList<>();		
		/** 如果已经存在过处理人，则继续使用旧处理人，否则启用分派 **/
		List<ProcessTaskStepUserVo> oldUserList = processTaskMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
		if (oldUserList.size() > 0) {
			ProcessTaskStepUserVo oldUserVo = oldUserList.get(0);
			workerList.add(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), oldUserVo.getUserUuid(), ProcessUserType.MAJOR.getValue()));
			currentProcessTaskStepVo.setUpdateActiveTime(0);
		}
		currentProcessTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
		int autoStart = myAssign(currentProcessTaskStepVo, workerList);
		boolean isAssignException = false;
        if(CollectionUtils.isEmpty(workerList)) {
            /** 获取步骤配置信息 **/
            ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
            String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());
            String defaultWorker = (String)JSONPath.read(stepConfig, "workerPolicyConfig.defaultWorker");
            String[] split = defaultWorker.split("#");
            workerList.add(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), split[0], split[1], ProcessUserType.MAJOR.getValue()));
            isAssignException = true;
        }
        
		processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
		if (CollectionUtils.isNotEmpty(workerList)) {
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
		
		/** 当只分配到一个用户时，自动设置为处理人，不需要抢单 **/
		if (workerList.size() == 1) {
			if (StringUtils.isNotBlank(workerList.get(0).getUuid()) && GroupSearch.USER.getValue().equals(workerList.get(0).getType())) {
				processTaskMapper.insertProcessTaskStepUser(new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), workerList.get(0).getUuid(), ProcessUserType.MAJOR.getValue()));
				/** 当步骤设置了自动开始时，设置当前步骤状态为处理中 **/
				if (autoStart == 1) {
					currentProcessTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
					currentProcessTaskStepVo.setUpdateStartTime(1);
				}			
			}
		}
		/** 触发通知 **/
		NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ASSIGN);
		if(isAssignException) {
		    NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ASSIGNEXCEPTION);
		}
		/** 执行动作 **/
		ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ASSIGN);
		return 1;
	}
	/**
	 * 
	* @Time:2020年7月29日
	* @Description: 子类分配处理人
	* @param currentProcessTaskStepVo
	* @param workerList
	* @throws ProcessTaskException 
	* @return int 返回值为1时代表配置了自动开始处理，0时代表配置了不自动开始处理
	 */
	protected abstract int myAssign(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepWorkerVo> workerList) throws ProcessTaskException;

	protected int defaultAssign(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepWorkerVo> workerList) throws ProcessTaskException {
        /** 获取步骤配置信息 **/
        ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
        String stepConfig = selectContentByHashMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());

        String executeMode = (String)JSONPath.read(stepConfig, "workerPolicyConfig.executeMode");
        int autoStart = (int)JSONPath.read(stepConfig, "workerPolicyConfig.autoStart");
        
        /** 如果workerList.size()>0，说明已经存在过处理人，则继续使用旧处理人，否则启用分派 **/
        if (CollectionUtils.isEmpty(workerList))  {
            /** 分配处理人 **/
            ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo = new ProcessTaskStepWorkerPolicyVo();
            processTaskStepWorkerPolicyVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
            List<ProcessTaskStepWorkerPolicyVo> workerPolicyList = processTaskMapper.getProcessTaskStepWorkerPolicy(processTaskStepWorkerPolicyVo);
            if (CollectionUtils.isNotEmpty(workerPolicyList)) {
                for (ProcessTaskStepWorkerPolicyVo workerPolicyVo : workerPolicyList) {
                    IWorkerPolicyHandler workerPolicyHandler = WorkerPolicyHandlerFactory.getHandler(workerPolicyVo.getPolicy());
                    if (workerPolicyHandler != null) {
                        List<ProcessTaskStepWorkerVo> tmpWorkerList = workerPolicyHandler.execute(workerPolicyVo, currentProcessTaskStepVo);
                        /** 顺序分配处理人 **/
                        if ("sort".equals(executeMode) && CollectionUtils.isNotEmpty(tmpWorkerList)) {
                            // 找到处理人，则退出
                            workerList.addAll(tmpWorkerList);
                            break;
                        } else if ("batch".equals(executeMode)) {
                            // 去重取并集
                            tmpWorkerList.removeAll(workerList);
                            workerList.addAll(tmpWorkerList);
                        }
                    }
                }
            }
        }
        return autoStart;
    }
	
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
								doNext(ProcessTaskOperationType.HANG, new ProcessStepThread(toProcessTaskStepVo) {
									@Override
									public void myExecute() {
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
			NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.HANG);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.HANG);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			currentProcessTaskStepVo.setIsActive(2);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			currentProcessTaskStepVo.setError(ExceptionUtils.getStackTrace(e));
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		}
//		finally {
//		    deleteProcessTaskStepInOperationByProcessTaskStepId(currentProcessTaskStepVo.getId(), ProcessTaskOperationType.HANG);
//		}
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
		currentProcessTaskStepVo.setUpdateStartTime(1);
		currentProcessTaskStepVo.setUpdateEndTime(-1);
		updateProcessTaskStepStatus(currentProcessTaskStepVo);
		if (!this.isAsync()) {// 同步模式
			try {
				myHandle(currentProcessTaskStepVo);
				/** 如果步骤被标记为全部完成，则触发完成 **/
				if (currentProcessTaskStepVo.getIsAllDone()) {
					/** 记录时间审计 **/
					TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.COMPLETE);
					IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
					doNext(ProcessTaskOperationType.COMPLETE, new ProcessStepThread(currentProcessTaskStepVo) {
						@Override
						public void myExecute() {
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
				public void myExecute() {
					IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(handlerType);
					try {
						// 这里不会有事务控制
						myHandle(currentProcessTaskStepVo);
						if (currentProcessTaskStepVo.getIsAllDone()) {
							/** 记录时间审计 **/
							TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.COMPLETE);

							doNext(ProcessTaskOperationType.COMPLETE, new ProcessStepThread(currentProcessTaskStepVo) {
								@Override
								public void myExecute() {
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
//	    finally {
//	        deleteProcessTaskStepInOperationByProcessTaskStepId(currentProcessTaskStepVo.getId(), ProcessTaskOperationType.HANDLE);
//	    }
		return 1;
	}

	protected abstract int myHandle(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	@Override
	public final int start(ProcessTaskStepVo currentProcessTaskStepVo) {
		try {
			// 锁定当前流程
			processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

            IProcessStepUtilHandler  processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(this.getHandler());
            if(processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
			/** 检查处理人是否合法 **/
            processStepUtilHandler.verifyOperationAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskOperationType.START, true);
            stepMajorUserRegulate(currentProcessTaskStepVo);
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

			/** 更新工单步骤状态为 “进行中” **/
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
			currentProcessTaskStepVo.setUpdateStartTime(1);
			currentProcessTaskStepVo.setUpdateEndTime(-1);
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
			processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
			
			/** 写入时间审计 **/
			TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.START);

			/** 计算SLA **/
			SlaHandler.calculate(currentProcessTaskStepVo);

			/** 触发通知 **/
			NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.START);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.START);

            processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId(), ProcessTaskStepRemindType.TRANSFER.getValue()));
		} catch (ProcessTaskException ex) {
			logger.error(ex.getMessage(), ex);
			currentProcessTaskStepVo.setError(ex.getMessage());
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		} finally {
			/** 处理历史记录 **/
			AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskAuditType.START);			
		}
		return 0;
	}

	private int updateProcessTaskStepStatus(ProcessTaskStepVo currentProcessTaskStepVo) {
	    if(currentProcessTaskStepVo.getActiveTime() != null) {
	        currentProcessTaskStepVo.setUpdateActiveTime(0);
	    }
	    if(currentProcessTaskStepVo.getStartTime() != null) {
            currentProcessTaskStepVo.setUpdateStartTime(0);
        }
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
		TaskStepNotifyTriggerType notifyTriggerType = TaskStepNotifyTriggerType.SUCCEED;
		ProcessTaskOperationType operationType = ProcessTaskOperationType.COMPLETE;
		boolean canComplete = false;
		if (this.getMode().equals(ProcessStepMode.MT)) {
			JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
			if (MapUtils.isNotEmpty(paramObj)) {
				String action = paramObj.getString("action");
				if (ProcessTaskOperationType.BACK.getValue().equals(action)) {
				    operationType = ProcessTaskOperationType.BACK;
					notifyTriggerType = TaskStepNotifyTriggerType.BACK;
				}
			}
			IProcessStepUtilHandler  processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(this.getHandler());
            if(processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
			canComplete = processStepUtilHandler.verifyOperationAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), operationType, true);
			stepMajorUserRegulate(currentProcessTaskStepVo);
		} else if (this.getMode().equals(ProcessStepMode.AT)) {
			canComplete = true;
		}

		if (canComplete) {
			try {
				/** 保存描述内容 **/
				saveContentAndFile(currentProcessTaskStepVo, operationType);
				myComplete(currentProcessTaskStepVo);			

				if (this.getMode().equals(ProcessStepMode.MT)) {
	                JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
					if (MapUtils.isNotEmpty(paramObj)) {
					    String priorityUuid = paramObj.getString("priorityUuid");
					    if(StringUtils.isNotBlank(priorityUuid)) {
					        processTaskMapper.updateProcessTaskPriorityUuidById(currentProcessTaskStepVo.getProcessTaskId(), priorityUuid);
					    }
					    JSONArray formAttributeDataList = paramObj.getJSONArray("formAttributeDataList");
                        if (CollectionUtils.isNotEmpty(formAttributeDataList)) {
    						// 表单属性显示控制
//    						Map<String, String> formAttributeActionMap = new HashMap<>();
//                            List<String> editableFormAttributeList = new ArrayList<>();
//    						List<ProcessTaskStepFormAttributeVo> processTaskStepFormAttributeList = processTaskMapper.getProcessTaskStepFormAttributeByProcessTaskStepId(currentProcessTaskStepVo.getId());
//    						if (processTaskStepFormAttributeList.size() > 0) {
//    							for (ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo : processTaskStepFormAttributeList) {
//    								//formAttributeActionMap.put(processTaskStepFormAttributeVo.getAttributeUuid(), processTaskStepFormAttributeVo.getAction());
//    							    if(processTaskStepFormAttributeVo.getAction().equals(FormAttributeAction.EDIT.getValue())) {
//    							        editableFormAttributeList.add(processTaskStepFormAttributeVo.getAttributeUuid());
//    							    }
//    							}
//    						}
    						// 组件联动导致隐藏的属性uuid列表
    						List<String> hidecomponentList = JSON.parseArray(JSON.toJSONString(paramObj.getJSONArray("hidecomponentList")), String.class);
    						// 获取旧表单数据
    						List<ProcessTaskFormAttributeDataVo> oldProcessTaskFormAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
    						if (CollectionUtils.isNotEmpty(oldProcessTaskFormAttributeDataList)) {
    							Iterator<ProcessTaskFormAttributeDataVo> iterator = oldProcessTaskFormAttributeDataList.iterator();
    							while (iterator.hasNext()) {
    								ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo = iterator.next();
    								String attributeUuid = processTaskFormAttributeDataVo.getAttributeUuid();
//    								if(!editableFormAttributeList.contains(attributeUuid) && !editableFormAttributeList.contains("all")) {// 只读或隐藏
//    								    iterator.remove();
//    								}
    								if (CollectionUtils.isNotEmpty(hidecomponentList) && hidecomponentList.contains(attributeUuid)) {
    									iterator.remove();
    								}
    							}
    							oldProcessTaskFormAttributeDataList.sort(ProcessTaskFormAttributeDataVo::compareTo);
    							ProcessTaskContentVo processTaskContentVo = new ProcessTaskContentVo(JSON.toJSONString(oldProcessTaskFormAttributeDataList));
    							processTaskMapper.replaceProcessTaskContent(processTaskContentVo);
    							paramObj.put(ProcessTaskAuditDetailType.FORM.getOldDataParamName(), processTaskContentVo.getHash());
    						}

							List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = new ArrayList<>(formAttributeDataList.size());
							for (int i = 0; i < formAttributeDataList.size(); i++) {
								JSONObject formAttributeDataObj = formAttributeDataList.getJSONObject(i);
								String attributeUuid = formAttributeDataObj.getString("attributeUuid");
//								if(!editableFormAttributeList.contains(attributeUuid) && !editableFormAttributeList.contains("all")) {// 对于只读或隐藏的属性，当前用户不能修改，不更新数据库中的值，不进行修改前后对比
//								    continue;
//								}
								if (CollectionUtils.isNotEmpty(hidecomponentList) && hidecomponentList.contains(attributeUuid)) {
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
					if (operationType == ProcessTaskOperationType.COMPLETE) {
						DataValid.formAttributeDataValidFromDb(currentProcessTaskStepVo);
						DataValid.assignWorkerValid(currentProcessTaskStepVo);
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
				currentProcessTaskStepVo.setUpdateEndTime(1);
				updateProcessTaskStepStatus(currentProcessTaskStepVo);

				/** 流转到下一步 **/
				Set<ProcessTaskStepVo> nextStepList = getNext(currentProcessTaskStepVo);
				if (nextStepList.size() > 0) {
				    Long startProcessTaskStepId = currentProcessTaskStepVo.getStartProcessTaskStepId();
				    if(startProcessTaskStepId == null) {
				        startProcessTaskStepId = currentProcessTaskStepVo.getId();
				    }
					for (ProcessTaskStepVo nextStep : nextStepList) {
						IProcessStepHandler nextStepHandler = ProcessStepHandlerFactory.getHandler(nextStep.getHandler());
						nextStep.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
						nextStep.setStartProcessTaskStepId(startProcessTaskStepId);
						
						if (nextStepHandler != null) {
							doNext(ProcessTaskOperationType.ACTIVE, new ProcessStepThread(nextStep) {
								@Override
								public void myExecute() {
								    nextStepHandler.active(nextStep);
								}
							});
						}
					}
				} else if (nextStepList.size() == 0 && !this.getHandler().equals(ProcessStepHandlerType.END.getHandler())) {
					throw new ProcessTaskException("找不到可流转路径");
				}
				
				if(this.getHandler().equals(ProcessStepHandlerType.END.getHandler())) {
		            NotifyHandler.notify(currentProcessTaskStepVo, TaskNotifyTriggerType.COMPLETEPROCESSTASK);
                    ActionHandler.action(currentProcessTaskStepVo, TaskNotifyTriggerType.SCOREPROCESSTASK);
				}else {
				    /** 触发通知 **/
	                NotifyHandler.notify(currentProcessTaskStepVo, notifyTriggerType);
	                /** 执行动作 **/
	                ActionHandler.action(currentProcessTaskStepVo, notifyTriggerType);
				}				
				
				/** 回退提醒 **/
				if (this.getMode().equals(ProcessStepMode.MT)) {
				    JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
				    if (ProcessTaskOperationType.BACK.getValue().equals(paramObj.getString("action"))) {
				        processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId()));
	                    ProcessStepUtilHandlerFactory.getHandler().saveStepRemind(currentProcessTaskStepVo, paramObj.getLong("nextStepId"), paramObj.getString("content"), ProcessTaskStepRemindType.BACK);
				    }else {
				        processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId()));
				    }
				}
			} catch (ProcessTaskException ex) {
				logger.error(ex.getMessage(), ex);
				currentProcessTaskStepVo.setError(ex.getMessage());
				currentProcessTaskStepVo.setIsActive(0);
				currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
				updateProcessTaskStepStatus(currentProcessTaskStepVo);
				/** 异常提醒 **/
	            ProcessStepUtilHandlerFactory.getHandler().saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getStartProcessTaskStepId(), ex.getMessage(), ProcessTaskStepRemindType.ERROR);
				/** 触发通知 **/
				NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.FAILED);
				
				/** 执行动作 **/
				ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.FAILED);
			} finally {
				if (ProcessTaskStatus.FAILED.getValue().equals(currentProcessTaskStepVo.getStatus())) {
					/**
					 * 发生异常不能完成当前步骤，执行当前步骤的回退操作
					 */
					IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(this.getHandler());
					doNext(ProcessTaskOperationType.BACK, new ProcessStepThread(currentProcessTaskStepVo) {
						@Override
						public void myExecute() {
						    handler.back(currentProcessTaskStepVo);
						}
					});
				}
				
				myCompleteAudit(currentProcessTaskStepVo);
			}
			if (this.getMode().equals(ProcessStepMode.MT)) {
				/** 写入时间审计 **/
				TimeAuditHandler.audit(currentProcessTaskStepVo, operationType);
				/** 计算SLA **/
				SlaHandler.calculate(new ProcessTaskVo(currentProcessTaskStepVo.getProcessTaskId()));
			}
		}

		return 1;
	}

	protected abstract int myComplete(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	protected abstract int myCompleteAudit(ProcessTaskStepVo currentProcessTaskStepVo);
	
	@Override
	public final int retreat(ProcessTaskStepVo currentProcessTaskStepVo) {
		try {
			// 锁定当前流程
			processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
			IProcessStepUtilHandler  processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(this.getHandler());
            if(processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
            processStepUtilHandler.verifyOperationAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskOperationType.RETREATCURRENTSTEP, true);
            stepMajorUserRegulate(currentProcessTaskStepVo);
            /** 设置当前步骤状态为未开始 **/
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
			/** 保存撤回原因 **/
			saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.RETREAT);
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
							doNext(ProcessTaskOperationType.HANG, new ProcessStepThread(nextProcessTaskStepVo) {
								@Override
								public void myExecute() {
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
			TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.RETREAT);
			if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
				TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.START);
			}

			/** 计算SLA并触发超时警告 **/
			SlaHandler.calculate(currentProcessTaskStepVo);

			/** 触发通知 **/
			NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.RETREAT);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.RETREAT);
		} catch (ProcessTaskException ex) {
			logger.error(ex.getMessage(), ex);
			currentProcessTaskStepVo.setError(ex.getMessage());
			currentProcessTaskStepVo.setIsActive(1);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
		}finally{			
			/** 处理历史记录 **/
			AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskAuditType.RETREAT);		
		}
		return 1;
	}

	protected abstract int myRetreat(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

	@Override
	public final int abortProcessTask(ProcessTaskVo currentProcessTaskVo) {
		// 锁定当前流程
		processTaskMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
		/** 校验权限 **/
        ProcessStepUtilHandlerFactory.getHandler().verifyOperationAuthoriy(currentProcessTaskVo.getId(), ProcessTaskOperationType.ABORTPROCESSTASK, true);

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
		AuditHandler.audit(processTaskStepVo, ProcessTaskAuditType.ABORTPROCESSTASK);
		/** 触发通知 **/
		NotifyHandler.notify(processTaskStepVo, TaskNotifyTriggerType.ABORTPROCESSTASK);
		/** 执行动作 **/
        ActionHandler.action(processTaskStepVo, TaskNotifyTriggerType.ABORTPROCESSTASK);
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
		TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.ABORTPROCESSTASK);

		/** 计算SLA **/
		SlaHandler.calculate(currentProcessTaskStepVo);

		/** 触发通知 **/
		NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ABORT);
		
		/** 执行动作 **/
		ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.ABORT);
		return 1;
	}

	/** 由于abort属于批量自动操作，不抛出任何受检异常 **/
	protected abstract int myAbort(ProcessTaskStepVo currentProcessTaskStepVo);

	@Override
	public final int recoverProcessTask(ProcessTaskVo currentProcessTaskVo) {
		// 锁定当前流程
		processTaskMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
		/** 校验权限 **/
        ProcessStepUtilHandlerFactory.getHandler().verifyOperationAuthoriy(currentProcessTaskVo, ProcessTaskOperationType.RECOVERPROCESSTASK, true);

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
		AuditHandler.audit(processTaskStepVo, ProcessTaskAuditType.RECOVERPROCESSTASK);
        /** 触发通知 **/
        NotifyHandler.notify(processTaskStepVo, TaskNotifyTriggerType.RECOVERPROCESSTASK);
        /** 执行动作 **/
        ActionHandler.action(processTaskStepVo, TaskNotifyTriggerType.RECOVERPROCESSTASK);
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
			List<ProcessTaskStepWorkerVo> workerList = new ArrayList<>();
			List<ProcessTaskStepUserVo> oldUserList = processTaskMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
			if (oldUserList.size() > 0) {
				ProcessTaskStepUserVo oldUserVo = oldUserList.get(0);
				workerList.add(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), oldUserVo.getUserUuid(), ProcessUserType.MAJOR.getValue()));
			}else {
				try {
					myAssign(currentProcessTaskStepVo, workerList);
				} catch (ProcessTaskException e) {
					logger.error(e.getMessage(), e);
					currentProcessTaskStepVo.setIsActive(-1);
					currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
					currentProcessTaskStepVo.setError(e.getMessage());
				}
			}
			for(ProcessTaskStepWorkerVo worker : workerList) {
				processTaskMapper.insertProcessTaskStepWorker(worker);
			}
			IProcessStepUtilHandler  processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(this.getHandler());
			if(processStepUtilHandler != null) {			
				processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
			}

			/** 修改步骤状态 **/
			processTaskMapper.updateProcessTaskStepStatus(currentProcessTaskStepVo);
		}else if(currentProcessTaskStepVo.getIsActive().equals(1) && ProcessTaskStatus.HANG.getValue().equals(currentProcessTaskStepVo.getStatus())) {
		    IProcessStepUtilHandler  processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(this.getHandler());
            if(processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
            /** 检查处理人是否合法 **/
            processStepUtilHandler.verifyOperationAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskOperationType.RECOVER, true);
            stepMajorUserRegulate(currentProcessTaskStepVo);
            myRecover(currentProcessTaskStepVo);

            /** 更新工单步骤状态为 “进行中” **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
		}

        /** 写入时间审计 **/
        TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.RECOVERPROCESSTASK);

        /** 计算SLA **/
        SlaHandler.calculate(currentProcessTaskStepVo);

        /** 触发通知 **/
        NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.RECOVER);
        
        /** 执行动作 **/
        ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.RECOVER);
		return 1;
	}

	/** 由于recover属于批量自动操作，不抛出任何受检异常 **/
	protected abstract int myRecover(ProcessTaskStepVo currentProcessTaskStepVo);
	
	@Override
	public int pause(ProcessTaskStepVo currentProcessTaskStepVo) {
        try {
            // 锁定当前流程
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

            IProcessStepUtilHandler  processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(this.getHandler());
            if(processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
            /** 检查处理人是否合法 **/
            processStepUtilHandler.verifyOperationAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskOperationType.PAUSE, true);
            stepMajorUserRegulate(currentProcessTaskStepVo);
            myPause(currentProcessTaskStepVo);

            /** 更新工单步骤状态为 “进行中” **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.HANG.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
//            processStepUtilHandler.updateProcessTaskStepUserAndWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId());
            
            /** 写入时间审计 **/
            TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.PAUSE);

            /** 计算SLA **/
            SlaHandler.calculate(currentProcessTaskStepVo);

            /** 触发通知 **/
            NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.PAUSE);
            
            /** 执行动作 **/
            ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.PAUSE);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        } finally {
            /** 处理历史记录 **/
            AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskAuditType.PAUSE);           
        }
        return 1;
	}
	
	protected abstract int myPause(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;
	
	@Override
	public final int accept(ProcessTaskStepVo currentProcessTaskStepVo) {
		try {
			// 锁定当前流程
			processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
			/** 校验权限 **/
			IProcessStepUtilHandler  processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(this.getHandler());
	        if(processStepUtilHandler == null) {
	            throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
	        }
	        processStepUtilHandler.verifyOperationAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskOperationType.ACCEPT, true);
	        stepMajorUserRegulate(currentProcessTaskStepVo);
			/** 清空worker表，只留下当前处理人 **/
			processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
			processTaskMapper.insertProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), UserContext.get().getUserUuid(true), ProcessUserType.MAJOR.getValue()));

			/** 删除user表主处理人，更换为当前处理人 **/
			ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
			processTaskStepUserVo.setProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
			processTaskStepUserVo.setUserUuid(UserContext.get().getUserUuid(true));
			processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());
			processTaskStepUserVo.setStatus(ProcessTaskStepUserStatus.DOING.getValue());
			processTaskMapper.insertProcessTaskStepUser(processTaskStepUserVo);

			/** 处理历史记录 **/
			// AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskStepAction.ACCEPT);

			/** 触发通知 **/
			// NotifyHandler.notify(currentProcessTaskStepVo, NotifyTriggerType.ACCEPT);
			processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId(), ProcessTaskStepRemindType.TRANSFER.getValue()));
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

		if (CollectionUtils.isEmpty(workerList)) {
			throw new ProcessTaskRuntimeException("请选择需要转交的处理人、处理组或角色");
		}
		// 锁定当前流程
		processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());

		/** 校验权限 **/
		IProcessStepUtilHandler  processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(this.getHandler());
        if(processStepUtilHandler == null) {
            throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
        }
        processStepUtilHandler.verifyOperationAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskOperationType.TRANSFERCURRENTSTEP, true);
		ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(currentProcessTaskStepVo.getId());
		processTaskStepVo.setParamObj(currentProcessTaskStepVo.getParamObj());
		/** 检查步骤是否 “已激活” **/
		if (!processTaskStepVo.getIsActive().equals(1)) {
			throw new ProcessTaskStepUnActivedException();
		}

		try {		
			List<ProcessTaskStepUserVo> oldUserList = processTaskMapper.getProcessTaskStepUserByStepId(currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue());
			if (CollectionUtils.isNotEmpty(oldUserList)) {
				List<String> workerUserUuidList = new ArrayList<>();
				for (ProcessTaskStepWorkerVo workerVo : workerList) {
					if (GroupSearch.USER.getValue().equals(workerVo.getType()) && StringUtils.isNotBlank(workerVo.getUuid())) {
						workerUserUuidList.add(workerVo.getUuid());
					}
				}
				for (ProcessTaskStepUserVo oldUser : oldUserList) {
					if (workerUserUuidList.contains(oldUser.getUserUuid())) {
						throw new ProcessTaskStepUserIsExistsException(oldUser.getUserName());
					}
				}
				/** 清空user表 **/
				ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo();
				processTaskStepUserVo.setProcessTaskStepId(currentProcessTaskStepVo.getId());
				processTaskStepUserVo.setUserType(ProcessUserType.MAJOR.getValue());
				processTaskMapper.deleteProcessTaskStepUser(processTaskStepUserVo);
			}

			/** 保存描述内容 **/
			saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.TRANSFER);
			
			/** 根据子类需要把最终处理人放进来，引擎将自动写入数据库，也可能为空，例如一些特殊的流程节点 **/
            processTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
			myTransfer(processTaskStepVo, workerList);
			
			ProcessTaskStepWorkerVo processTaskStepWorkerVo = new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId());
			/** 当只分配到一个用户时，自动设置为处理人，不需要抢单 **/
			if (workerList.size() == 1) {
				if (StringUtils.isNotBlank(workerList.get(0).getUuid()) && GroupSearch.USER.getValue().equals(workerList.get(0).getType())) {
					processTaskMapper.insertProcessTaskStepUser(new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), workerList.get(0).getUuid(), ProcessUserType.MAJOR.getValue()));
					/** 当步骤设置了自动开始时，设置当前步骤状态为处理中 **/
					int autoStart = myAssign(processTaskStepVo, workerList);
					if (autoStart == 1) {
						processTaskStepWorkerVo.setUserType(ProcessUserType.MAJOR.getValue());
						processTaskStepVo.setStatus(ProcessTaskStatus.RUNNING.getValue());
					}
				}
			}
			/** 清空work表，重新写入新数据 **/
			processTaskMapper.deleteProcessTaskStepWorker(processTaskStepWorkerVo);
			for (ProcessTaskStepWorkerVo workerVo : workerList) {
				processTaskMapper.insertProcessTaskStepWorker(workerVo);
			}

			updateProcessTaskStepStatus(processTaskStepVo);

			/** 触发通知 **/
			NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.TRANSFER);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.TRANSFER);

			/** 处理时间审计 **/
			TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.TRANSFER);
			/** 转交提醒 **/

            processTaskMapper.deleteProcessTaskStepRemind(new ProcessTaskStepRemindVo(currentProcessTaskStepVo.getId(), ProcessTaskStepRemindType.TRANSFER.getValue()));
	        ProcessStepUtilHandlerFactory.getHandler().saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getId(), currentProcessTaskStepVo.getParamObj().getString("content"), ProcessTaskStepRemindType.TRANSFER);
		} catch (ProcessTaskException e) {
			logger.error(e.getMessage(), e);
			processTaskStepVo.setError(e.getMessage());
			processTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			updateProcessTaskStepStatus(processTaskStepVo);
		} finally {
			/** 处理历史记录 **/
			AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskAuditType.TRANSFER);
		}

		return 0;
	}

	protected abstract int myTransfer(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepWorkerVo> workerList) throws ProcessTaskException;

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
						if (fromProcessTaskStepVo.getHandler().equals(ProcessStepHandlerType.DISTRIBUTARY.getHandler()) || fromProcessTaskStepVo.getHandler().equals(ProcessStepHandlerType.CONDITION.getHandler())) {
							IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(fromProcessTaskStepVo.getHandler());
							if (handler != null) {
								fromProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
								doNext(ProcessTaskOperationType.BACK, new ProcessStepThread(fromProcessTaskStepVo) {
									@Override
									public void myExecute() {
									    handler.back(fromProcessTaskStepVo);
									}
								});
							}
						} else {
							// 如果是处理节点，则重新激活
							IProcessStepHandler handler = ProcessStepHandlerFactory.getHandler(fromProcessTaskStepVo.getHandler());
							if (handler != null) {
								fromProcessTaskStepVo.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
								doNext(ProcessTaskOperationType.ACTIVE, new ProcessStepThread(fromProcessTaskStepVo) {
									@Override
									public void myExecute() {
									    handler.active(fromProcessTaskStepVo);
									}
								});
							}
						}
					}
				}
			}
			/** 处理时间审计 **/
			TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.BACK);

			/** 触发通知 **/
			NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.BACK);
			
			/** 执行动作 **/
			ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.BACK);
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
		 Map<String, Object> formAttributeDataMap = new HashMap<String, Object>();
		 List<FormAttributeVo> formAttributeList = new ArrayList<FormAttributeVo>();
//		 Map<String, String> formAttributeActionMap = new HashMap<String,String>();
		 ProcessTaskVo processTaskVo = new ProcessTaskVo();
		if (processTaskId == null) {// 首次保存
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
			/** 生成工单号 **/
			ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo = channelMapper.getProcessTaskSerialNumberPolicyLockByChannelTypeUuid(channelVo.getChannelTypeUuid());
			if(processTaskSerialNumberPolicyVo == null) {
			    // throw new 
			}
			IProcessTaskSerialNumberPolicyHandler policyHandler = ProcessTaskSerialNumberPolicyHandlerFactory.getHandler(processTaskSerialNumberPolicyVo.getHandler());
			if(policyHandler == null) {
			    throw new ProcessTaskSerialNumberPolicyNotFoundException(processTaskSerialNumberPolicyVo.getHandler());
			}
			processTaskVo.setSerialNumber(policyHandler.genarate(processTaskSerialNumberPolicyVo));
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
					formAttributeList = formVersionVo.getFormAttributeList();
				}
			}

			ProcessScoreTemplateVo processScoreTemplateVo = processMapper.getProcessScoreTemplateByProcessUuid(currentProcessTaskStepVo.getProcessUuid());
			if(processScoreTemplateVo != null) {
			    ProcessTaskScoreTemplateVo processTaskScoreTemplateVo = new ProcessTaskScoreTemplateVo(processScoreTemplateVo);
			    if(processTaskScoreTemplateVo.getConfig() != null) {
			        ProcessTaskScoreTemplateConfigVo processTaskScoreTemplateConfigVo = new ProcessTaskScoreTemplateConfigVo(processTaskScoreTemplateVo.getConfigStr());
			        if(StringUtils.isNotBlank(processTaskScoreTemplateConfigVo.getHash()) && selectContentByHashMapper.checkProcessTaskScoreTempleteConfigIsExists(processTaskScoreTemplateConfigVo.getHash()) == 0) {
			            processTaskMapper.insertProcessTaskScoreTempleteConfig(processTaskScoreTemplateConfigVo);
			        }
			        processTaskScoreTemplateVo.setConfigHash(processTaskScoreTemplateConfigVo.getHash());
			    }
			    processTaskScoreTemplateVo.setProcessTaskId(processTaskVo.getId());
			    processTaskMapper.insertProcessTaskScoreTemplate(processTaskScoreTemplateVo);
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
						    Long stepId = stepIdMap.get(suuid);
						    if(stepId != null) {
	                            processTaskMapper.insertProcessTaskStepSla(stepId, processTaskSlaVo.getId());
						    }
						}
					}
				}
			}

			/** 加入上报人为处理人 **/
			ProcessTaskStepUserVo processTaskStepUserVo = new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), UserContext.get().getUserUuid(true), ProcessUserType.MAJOR.getValue());
			processTaskMapper.insertProcessTaskStepUser(processTaskStepUserVo);
			processTaskMapper.insertProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), UserContext.get().getUserUuid(true), ProcessUserType.MAJOR.getValue()));

			/** 保存转报数据 **/
			Long fromProcessTaskId = paramObj.getLong("fromProcessTaskId");
			Long channelTypeRelationId = paramObj.getLong("channelTypeRelationId");
			if(fromProcessTaskId != null && channelTypeRelationId != null) {
			    processTaskMapper.insertProcessTaskTranferReport(new ProcessTaskTranferReportVo(channelTypeRelationId, fromProcessTaskId, processTaskVo.getId()));
//			    processTaskMapper.replaceProcessTaskRelation(new ProcessTaskRelationVo(channelTypeRelationId, fromProcessTaskId, processTaskVo.getId()));
			}
		} else {
			/** 锁定当前流程 **/
			processTaskMapper.getProcessTaskLockById(processTaskId);
			// 第二次保存时的操作
			processTaskVo = processTaskMapper.getProcessTaskById(processTaskId);
			if (!ProcessTaskStatus.DRAFT.getValue().equals(processTaskVo.getStatus())) {
				throw new ProcessTaskRuntimeException("工单非草稿状态，不能进行上报暂存操作");
			}
			processTaskMapper.deleteProcessTaskStepContentByProcessTaskStepId(currentProcessTaskStepVo.getId());
	        processTaskMapper.deleteProcessTaskStepFileByProcessTaskStepId(currentProcessTaskStepVo.getId());
		}
		try {

			

			if(processTaskMapper.checkProcessTaskhasForm(currentProcessTaskStepVo.getProcessTaskId()) > 0) {
			    processTaskMapper.deleteProcessTaskFormAttributeDataByProcessTaskId(processTaskId);
	            // 组件联动导致隐藏的属性uuid列表
	            List<String> hidecomponentList = JSON.parseArray(paramObj.getString("hidecomponentList"), String.class);

	            /** 写入当前步骤的表单属性值 **/
	            JSONArray formAttributeDataList = paramObj.getJSONArray("formAttributeDataList");
	            if (CollectionUtils.isNotEmpty(formAttributeDataList)) {
	                // 表单属性显示控制
//	                formAttributeActionMap = new HashMap<>();
//	                List<String> editableFormAttributeList = new ArrayList<>();
//	                List<ProcessTaskStepFormAttributeVo> processTaskStepFormAttributeList = processTaskMapper.getProcessTaskStepFormAttributeByProcessTaskStepId(currentProcessTaskStepVo.getId());
//	                if (processTaskStepFormAttributeList.size() > 0) {
//	                    for (ProcessTaskStepFormAttributeVo processTaskStepFormAttributeVo : processTaskStepFormAttributeList) {
//	                        formAttributeActionMap.put(processTaskStepFormAttributeVo.getAttributeUuid(), processTaskStepFormAttributeVo.getAction());
//	                        if(processTaskStepFormAttributeVo.getAction().equals(FormAttributeAction.EDIT.getValue())) {
//                                editableFormAttributeList.add(processTaskStepFormAttributeVo.getAttributeUuid());
//                            }
//	                    }
//	                }
	                for (int i = 0; i < formAttributeDataList.size(); i++) {
	                    JSONObject formAttributeDataObj = formAttributeDataList.getJSONObject(i);
	                    String attributeUuid = formAttributeDataObj.getString("attributeUuid");
//	                    if(!editableFormAttributeList.contains(attributeUuid) && !editableFormAttributeList.contains("all")) {// 对于只读或隐藏的属性，当前用户不能修改，不更新数据库中的值，不进行修改前后对比
//	                        continue;
//	                    }
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
	                    formAttributeDataMap.put(formAttributeDataObj.getString("attributeUuid"), formAttributeDataObj);
	                }
	            }
			}
			//是否需要校验，兼容提供给第三方的上报接口，表单等不合法，则不生成工单
            Integer isNeedValid = paramObj.getInteger("isNeedValid");
			if(isNeedValid != null &&isNeedValid == 1) {
                currentProcessTaskStepVo.setFormAttributeDataMap(formAttributeDataMap);
                currentProcessTaskStepVo.setFormAttributeVoList(formAttributeList);
//                currentProcessTaskStepVo.setFormAttributeActionMap(formAttributeActionMap);
                DataValid.formAttributeDataValid(currentProcessTaskStepVo);
                DataValid.baseInfoValid(currentProcessTaskStepVo,processTaskVo);
			}
            
			/** 更新工单信息 **/
            processTaskVo = new ProcessTaskVo();
            processTaskVo.setId(currentProcessTaskStepVo.getProcessTaskId());
            processTaskVo.setTitle(paramObj.getString("title"));
            processTaskVo.setOwner(paramObj.getString("owner"));
            processTaskVo.setPriorityUuid(paramObj.getString("priorityUuid"));
            processTaskMapper.updateProcessTaskTitleOwnerPriorityUuid(processTaskVo);

            /** 保存描述内容和附件 **/
            saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.STARTPROCESS);
            
            /** 写入“标签”信息**/
            processTaskMapper.deleteProcessTaskTagByProcessTaskId(processTaskVo.getId());
            JSONArray tagArray = paramObj.getJSONArray("tagList");
            if(CollectionUtils.isNotEmpty(tagArray)) {
                List<String> tagNameList = JSONObject.parseArray(tagArray.toJSONString(), String.class);
                List<ProcessTagVo> existTagList = processMapper.getProcessTagByNameList(tagNameList);
                List<String> existTagNameList = existTagList.stream().map(ProcessTagVo::getName).collect(Collectors.toList());
                List<String> notExistTagList = ListUtils.removeAll(tagNameList, existTagNameList);
                for(String tagName : notExistTagList) {
                    ProcessTagVo tagVo = new ProcessTagVo(tagName);
                    processMapper.insertProcessTag(tagVo);
                    existTagList.add(tagVo);
                }
                List<ProcessTaskTagVo> processTaskTagVoList = new ArrayList<ProcessTaskTagVo>();
                for(ProcessTagVo processTagVo : existTagList) {
                    processTaskTagVoList.add(new ProcessTaskTagVo(processTaskVo.getId(),processTagVo.getId()));
                }
                processTaskMapper.insertProcessTaskTag(processTaskTagVoList);
            }   
            
			mySaveDraft(currentProcessTaskStepVo);
			currentProcessTaskStepVo.setIsActive(1);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.DRAFT.getValue());
			currentProcessTaskStepVo.setUpdateActiveTime(1);
			currentProcessTaskStepVo.setUpdateStartTime(1);
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
			ProcessStepUtilHandlerFactory.getHandler().verifyOperationAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), ProcessTaskOperationType.STARTPROCESS, true);
			DataValid.formAttributeDataValidFromDb(currentProcessTaskStepVo);
			DataValid.baseInfoValidFromDb(currentProcessTaskStepVo);
			DataValid.assignWorkerValid(currentProcessTaskStepVo);
			myStartProcess(currentProcessTaskStepVo);
			// 获取表单数据
			List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			if (CollectionUtils.isNotEmpty(processTaskFormAttributeDataList)) {
				processTaskFormAttributeDataList.sort(ProcessTaskFormAttributeDataVo::compareTo);
				JSONObject paramObj = currentProcessTaskStepVo.getParamObj();
				paramObj.put(ProcessTaskAuditDetailType.FORM.getParamName(), JSON.toJSONString(processTaskFormAttributeDataList));
			}
			/** 更新处理人状态 **/
			ProcessTaskStepUserVo processTaskMajorUser = new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(),currentProcessTaskStepVo.getId(), UserContext.get().getUserUuid(true));
			processTaskMajorUser.setStatus(ProcessTaskStepUserStatus.DONE.getValue());
			processTaskMajorUser.setUserType(ProcessUserType.MAJOR.getValue());
			processTaskMapper.updateProcessTaskStepUserStatus(processTaskMajorUser);
			/** 清空worker表 **/
			processTaskMapper.deleteProcessTaskStepWorker(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getId()));
			currentProcessTaskStepVo.setIsActive(2);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.SUCCEED.getValue());
			currentProcessTaskStepVo.setUpdateEndTime(1);
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
			
			/** 流转到下一步 **/
			Set<ProcessTaskStepVo> nextStepList = getNext(currentProcessTaskStepVo);
			for (ProcessTaskStepVo nextStep : nextStepList) {
				IProcessStepHandler nextStepHandler = ProcessStepHandlerFactory.getHandler(nextStep.getHandler());
				nextStep.setFromProcessTaskStepId(currentProcessTaskStepVo.getId());
				nextStep.setStartProcessTaskStepId(currentProcessTaskStepVo.getId());
				if (nextStepHandler != null) {
					doNext(ProcessTaskOperationType.ACTIVE, new ProcessStepThread(nextStep) {
						@Override
						public void myExecute() {
						    nextStepHandler.active(nextStep);
						}

					});
				}
			}
            /** 写入时间审计 **/
            TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.ACTIVE);
            TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.START);
            TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.COMPLETE);

            /** 计算SLA并触发超时警告 **/
            SlaHandler.calculate(currentProcessTaskStepVo);

            /** 触发通知 **/
            NotifyHandler.notify(currentProcessTaskStepVo, TaskNotifyTriggerType.STARTPROCESS);
            
            /** 执行动作 **/
            ActionHandler.action(currentProcessTaskStepVo, TaskNotifyTriggerType.STARTPROCESS);
		} catch (ProcessTaskException ex) {
			logger.error(ex.getMessage(), ex);
			currentProcessTaskStepVo.setIsActive(1);
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
			currentProcessTaskStepVo.setError(ex.getMessage());
			updateProcessTaskStepStatus(currentProcessTaskStepVo);
            /** 触发通知 **/
            NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.FAILED);
            
            /** 执行动作 **/
            ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.FAILED);
		} finally {
			/** 处理历史记录 **/
		    ProcessTaskTranferReportVo processTaskTranferReportVo =  processTaskMapper.getProcessTaskTranferReportByToProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
			if(processTaskTranferReportVo != null) {
			    currentProcessTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.CHANNELTYPERELATION.getParamName(), processTaskTranferReportVo.getChannelTypeRelationId());
			    currentProcessTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.PROCESSTASK.getParamName(), processTaskTranferReportVo.getFromProcessTaskId());
			    AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskAuditType.REPORTRELATION);
			    
			    ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
                processTaskStepVo.setProcessTaskId(processTaskTranferReportVo.getFromProcessTaskId());
                processTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.CHANNELTYPERELATION.getParamName(), processTaskTranferReportVo.getChannelTypeRelationId());
                processTaskStepVo.getParamObj().put(ProcessTaskAuditDetailType.PROCESSTASKLIST.getParamName(), JSON.toJSONString(Arrays.asList(currentProcessTaskStepVo.getProcessTaskId())));
                AuditHandler.audit(processTaskStepVo, ProcessTaskAuditType.TRANFERREPORT);
			}else {
	            AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskAuditType.STARTPROCESS);			    
			}
			
		}
		return 0;
	}

	protected abstract int myStartProcess(ProcessTaskStepVo processTaskStepVo) throws ProcessTaskException;

	@Override
	public final Set<ProcessTaskStepVo> getNext(ProcessTaskStepVo currentProcessTaskStepVo) {
		List<ProcessTaskStepRelVo> relList = processTaskMapper.getProcessTaskStepRelByFromId(currentProcessTaskStepVo.getId());
		// 重置所有关系状态为-1
		for (ProcessTaskStepRelVo rel : relList) {
			processTaskMapper.updateProcessTaskStepRelIsHit(rel.getFromProcessTaskStepId(), rel.getToProcessTaskStepId(), -1);
		}
		currentProcessTaskStepVo.setRelList(relList);
		Long nextStepId = currentProcessTaskStepVo.getParamObj().getLong("nextStepId");
		List<ProcessTaskStepVo> nextStepList = processTaskMapper.getToProcessTaskStepByFromIdAndType(currentProcessTaskStepVo.getId(),null);
		Set<ProcessTaskStepVo> nextStepSet = null;
		try {
			nextStepSet = myGetNext(currentProcessTaskStepVo, nextStepList, nextStepId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (ex.getMessage() != null && !ex.getMessage().equals("")) {
				currentProcessTaskStepVo.appendError(ex.getMessage());
			} else {
				currentProcessTaskStepVo.appendError(ExceptionUtils.getStackTrace(ex));
			}
			currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            /** 异常提醒 **/
            ProcessStepUtilHandlerFactory.getHandler().saveStepRemind(currentProcessTaskStepVo, currentProcessTaskStepVo.getId(), ex.getMessage(), ProcessTaskStepRemindType.ERROR);
		}

		/** 更新路径isHit=1，在active方法里需要根据isHit状态判断路径是否经通过 **/
		if (nextStepSet == null) {
			nextStepSet = new HashSet<>();
		}
		for (ProcessTaskStepVo stepVo : nextStepSet) {
			processTaskMapper.updateProcessTaskStepRelIsHit(currentProcessTaskStepVo.getId(), stepVo.getId(), 1);
		}
		return nextStepSet;
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

	protected abstract Set<ProcessTaskStepVo> myGetNext(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepVo> nextStepList, Long nextStepId) throws ProcessTaskException;

	protected synchronized static void doNext(ProcessTaskOperationType operationType, ProcessStepThread thread) {
	    if(operationType != null) {
	        ProcessTaskStepInOperationVo processTaskStepInOperationVo = new ProcessTaskStepInOperationVo(thread.getProcessTaskStepVo().getProcessTaskId(), thread.getProcessTaskStepVo().getId(), operationType.getValue());
	        processTaskMapper.insertProcessTaskStepInOperation(processTaskStepInOperationVo);
	        thread.setSupplier(() -> processTaskMapper.deleteProcessTaskStepInOperationByProcessTaskStepIdAndOperationType(processTaskStepInOperationVo));
	    }
	        
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
	/** handle方法异步模式会调用这个方法**/
	protected synchronized static void doNext(ProcessStepThread thread) {
        doNext(null, thread);
    }
	public int redo(ProcessTaskStepVo currentProcessTaskStepVo) {
	    try {
            // 锁定当前流程
            processTaskMapper.getProcessTaskLockById(currentProcessTaskStepVo.getProcessTaskId());
            IProcessStepUtilHandler  processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(this.getHandler());
            if(processStepUtilHandler == null) {
                throw new ProcessStepUtilHandlerNotFoundException(this.getHandler());
            }
            processStepUtilHandler.verifyOperationAuthoriy(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessTaskOperationType.REDO, true);
            stepMajorUserRegulate(currentProcessTaskStepVo);
            /** 设置当前步骤状态为未开始 **/
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.PENDING.getValue());
            /** 保存打回原因 **/
            saveContentAndFile(currentProcessTaskStepVo, ProcessTaskOperationType.REDO);
//            myRedo(currentProcessTaskStepVo);

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
                            doNext(ProcessTaskOperationType.HANG, new ProcessStepThread(nextProcessTaskStepVo) {
                                @Override
                                public void myExecute() {
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
            processTaskScoreMapper.deleteProcessTaskAutoScoreByProcessTaskId(currentProcessTaskStepVo.getProcessTaskId());
            /** 写入时间审计 **/
            TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.REDO);
            if (currentProcessTaskStepVo.getStatus().equals(ProcessTaskStatus.RUNNING.getValue())) {
                TimeAuditHandler.audit(currentProcessTaskStepVo, ProcessTaskOperationType.START);
            }

            /** 计算SLA并触发超时警告 **/
            SlaHandler.calculate(currentProcessTaskStepVo);

            /** 触发通知 **/
            NotifyHandler.notify(currentProcessTaskStepVo, TaskStepNotifyTriggerType.REDO);
            
            /** 执行动作 **/
            ActionHandler.action(currentProcessTaskStepVo, TaskStepNotifyTriggerType.REDO);
        } catch (ProcessTaskException ex) {
            logger.error(ex.getMessage(), ex);
            currentProcessTaskStepVo.setError(ex.getMessage());
            currentProcessTaskStepVo.setIsActive(1);
            currentProcessTaskStepVo.setStatus(ProcessTaskStatus.FAILED.getValue());
            updateProcessTaskStepStatus(currentProcessTaskStepVo);
        }finally{           
            /** 处理历史记录 **/
            AuditHandler.audit(currentProcessTaskStepVo, ProcessTaskAuditType.REDO);     
        }
        return 1;
	}
	
//	protected abstract int myRedo(ProcessTaskStepVo currentProcessTaskStepVo);
	
	public int scoreProcessTask(ProcessTaskVo currentProcessTaskVo) {
	    // 锁定当前流程
        processTaskMapper.getProcessTaskLockById(currentProcessTaskVo.getId());
        IProcessStepUtilHandler  processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler();
        //只有上报人才可评分
        processStepUtilHandler.verifyOperationAuthoriy(currentProcessTaskVo, ProcessTaskOperationType.SCORE, true);
        JSONObject paramObj = currentProcessTaskVo.getParamObj();
        Long scoreTemplateId = paramObj.getLong("scoreTemplateId");
        String content = paramObj.getString("content");
        List<ScoreTemplateDimensionVo> scoreDimensionList = JSON.parseArray(paramObj.getJSONArray("scoreDimensionList").toJSONString(), ScoreTemplateDimensionVo.class);

        ProcessTaskScoreVo processtaskScoreVo = new ProcessTaskScoreVo();
        processtaskScoreVo.setProcessTaskId(currentProcessTaskVo.getId());
        processtaskScoreVo.setScoreTemplateId(scoreTemplateId);
        processtaskScoreVo.setFcu(UserContext.get().getUserUuid());
        processtaskScoreVo.setIsAuto(0);
        for(ScoreTemplateDimensionVo scoreTemplateDimensionVo : scoreDimensionList){
            processtaskScoreVo.setScoreDimensionId(scoreTemplateDimensionVo.getId());
            processtaskScoreVo.setScore(scoreTemplateDimensionVo.getScore());
            processTaskScoreMapper.insertProcessTaskScore(processtaskScoreVo);
        }

        JSONObject contentObj = new JSONObject();
        contentObj.put("scoreTemplateId", scoreTemplateId);
        contentObj.put("content", content);
        contentObj.put("dimensionList", paramObj.getJSONArray("scoreDimensionList"));
        JSONObject scoreObj = new JSONObject();
        scoreObj.put(ProcessTaskAuditDetailType.SCORE.getParamName(),contentObj);
        /**processtask_content表存储了两份数据：
         * 1、评价内容content本身
         * 2、由评分模版ID、评价内容、评分维度与分数组装而成的JSON
         */
        if (StringUtils.isNotBlank(content)) {
            ProcessTaskContentVo contentVo = new ProcessTaskContentVo(content);
            processTaskMapper.replaceProcessTaskContent(contentVo);
            processtaskScoreVo.setContentHash(contentVo.getHash());
            processTaskScoreMapper.insertProcessTaskScoreContent(processtaskScoreVo);
        }
        currentProcessTaskVo.setStatus(ProcessTaskStatus.SCORED.getValue());
        processTaskMapper.updateProcessTaskStatus(currentProcessTaskVo);
        processTaskScoreMapper.deleteProcessTaskAutoScoreByProcessTaskId(currentProcessTaskVo.getId());        
        ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
        processTaskStepVo.setProcessTaskId(currentProcessTaskVo.getId());
        processTaskStepVo.setParamObj(scoreObj);
        /** 生成活动 */
        AuditHandler.audit(processTaskStepVo, ProcessTaskAuditType.SCORE);
        /** 触发通知 **/
        NotifyHandler.notify(processTaskStepVo, TaskNotifyTriggerType.SCOREPROCESSTASK);
	    return 1;
	}
	
	/**
	 * 
	* @Time:2020年9月30日
	* @Description: 步骤主处理人校正操作
	*  判断当前用户是否是代办人，如果不是就什么都不做，如果是，进行下面3个操作
	*  1.往processtask_step_agent表中插入一条数据，记录该步骤的原主处理人和代办人
	*  2.将processtask_step_worker表中该步骤的主处理人uuid改为代办人(当前用户)
	*  3.将processtask_step_user表中该步骤的主处理人user_uuid改为代办人(当前用户)
	* @param currentProcessTaskStepVo 
	* @return void
	 */
	private void stepMajorUserRegulate(ProcessTaskStepVo currentProcessTaskStepVo) {
	    /** 能进入这个方法，说明当前用户有权限处理当前步骤，可能是三类处理人：第一处理人(A)、代办人(B)、代办人的代办人(C) 。其中A授权给B，B授权给C **/
	    ProcessTaskStepAgentVo processTaskStepAgentVo = processTaskMapper.getProcessTaskStepAgentByProcessTaskStepId(currentProcessTaskStepVo.getId());
	    if(processTaskStepAgentVo == null) {
	        //代办人还没接管，当前用户可能是A和B
	        List<String> teamUuidList = teamMapper.getTeamUuidListByUserUuid(UserContext.get().getUserUuid(true));
	        if(processTaskMapper.checkIsWorker(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true), teamUuidList, UserContext.get().getRoleUuidList()) == 0) {
	            //当用户是B
	            String userUuid = userMapper.getUserUuidByAgentUuidAndFunc(UserContext.get().getUserUuid(), "processTask");
	            if(StringUtils.isNotBlank(userUuid)) {
	                processTaskMapper.replaceProcesssTaskStepAgent(new ProcessTaskStepAgentVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), userUuid, UserContext.get().getUserUuid(true)));
	                processTaskMapper.updateProcessTaskStepWorkerUuid(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), userUuid, ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
	                processTaskMapper.updateProcessTaskStepUserUserUuid(new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), userUuid, ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
                    currentProcessTaskStepVo.setOriginalUser(userUuid);
	            }
	        }
	    }else {
	        //代办人接管过了，当前用户可能是A、B、C
	        if(UserContext.get().getUserUuid(true).equals(processTaskStepAgentVo.getUserUuid())) {
	            //当前用户是A
	            processTaskMapper.deleteProcessTaskStepAgentByProcessTaskStepId(currentProcessTaskStepVo.getId());
                processTaskMapper.updateProcessTaskStepWorkerUuid(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), processTaskStepAgentVo.getAgentUuid(), ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
                processTaskMapper.updateProcessTaskStepUserUserUuid(new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), processTaskStepAgentVo.getAgentUuid(), ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
	        }else if(UserContext.get().getUserUuid(true).equals(processTaskStepAgentVo.getAgentUuid())) {
	            //当前用户是B
	            currentProcessTaskStepVo.setOriginalUser(processTaskStepAgentVo.getUserUuid());
	        }else {
	            //当前用户是C
	            processTaskMapper.replaceProcesssTaskStepAgent(new ProcessTaskStepAgentVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), processTaskStepAgentVo.getAgentUuid(), UserContext.get().getUserUuid(true)));
                processTaskMapper.updateProcessTaskStepWorkerUuid(new ProcessTaskStepWorkerVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), GroupSearch.USER.getValue(), processTaskStepAgentVo.getAgentUuid(), ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
                processTaskMapper.updateProcessTaskStepUserUserUuid(new ProcessTaskStepUserVo(currentProcessTaskStepVo.getProcessTaskId(), currentProcessTaskStepVo.getId(), processTaskStepAgentVo.getAgentUuid(), ProcessUserType.MAJOR.getValue(), UserContext.get().getUserUuid(true)));
                currentProcessTaskStepVo.setOriginalUser(processTaskStepAgentVo.getAgentUuid());
	        }
	    }
	    
	}
}
