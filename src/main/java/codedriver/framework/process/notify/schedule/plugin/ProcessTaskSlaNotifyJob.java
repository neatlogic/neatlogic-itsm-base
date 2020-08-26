package codedriver.framework.process.notify.schedule.plugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import codedriver.framework.common.constvalue.TeamLevel;
import codedriver.framework.dao.mapper.TeamMapper;
import codedriver.framework.dto.TeamVo;
import codedriver.framework.file.dao.mapper.FileMapper;
import codedriver.framework.notify.dao.mapper.NotifyMapper;
import codedriver.framework.notify.dto.NotifyPolicyVo;
import codedriver.framework.notify.dto.NotifyReceiverVo;
import codedriver.framework.notify.dto.ParamMappingVo;
import codedriver.framework.process.column.core.ProcessTaskUtil;
import codedriver.framework.process.constvalue.ProcessStepType;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessTaskStepAction;
import codedriver.framework.process.constvalue.ProcessUserType;
import codedriver.framework.process.dao.mapper.CatalogMapper;
import codedriver.framework.process.dao.mapper.ChannelMapper;
import codedriver.framework.process.dao.mapper.PriorityMapper;
import codedriver.framework.process.dao.mapper.ProcessStepHandlerMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.WorktimeMapper;
import codedriver.framework.process.dto.CatalogVo;
import codedriver.framework.process.dto.ChannelTypeVo;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.PriorityVo;
import codedriver.framework.process.dto.ProcessStepHandlerVo;
import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskFormAttributeDataVo;
import codedriver.framework.process.dto.ProcessTaskFormVo;
import codedriver.framework.process.dto.ProcessTaskSlaNotifyVo;
import codedriver.framework.process.dto.ProcessTaskSlaTimeVo;
import codedriver.framework.process.dto.ProcessTaskSlaVo;
import codedriver.framework.process.dto.ProcessTaskStepContentVo;
import codedriver.framework.process.dto.ProcessTaskStepReplyVo;
import codedriver.framework.process.dto.ProcessTaskStepUserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
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
	private NotifyMapper notifyMapper;
	
	@Autowired
	private PriorityMapper priorityMapper;
	@Autowired
	private ChannelMapper channelMapper;
	@Autowired
	private CatalogMapper catalogMapper;
	@Autowired
	private WorktimeMapper worktimeMapper;
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private FileMapper fileMapper;
	@Autowired
	private ProcessStepHandlerMapper processStepHandlerMapper;
	
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
		String tenantUuid = jobObject.getTenantUuid();
		TenantContext.get().switchTenant(tenantUuid);
		Long slaNotifyId = (Long) jobObject.getData("slaNotifyId");
		ProcessTaskSlaNotifyVo processTaskSlaNotifyVo = processTaskMapper.getProcessTaskNotifyById(slaNotifyId);
		boolean isJobLoaded = false;
		if (processTaskSlaNotifyVo != null) {
			ProcessTaskSlaTimeVo slaTimeVo = processTaskMapper.getProcessTaskSlaTimeBySlaId(processTaskSlaNotifyVo.getSlaId());
			if (slaTimeVo != null) {
				if (processTaskSlaNotifyVo != null && processTaskSlaNotifyVo.getConfigObj() != null) {
					JSONObject policyObj = processTaskSlaNotifyVo.getConfigObj();
					String expression = policyObj.getString("expression");
					int time = policyObj.getIntValue("time");
					String unit = policyObj.getString("unit");
					String executeType = policyObj.getString("executeType");
					int intervalTime = policyObj.getIntValue("intervalTime");
					String intervalUnit = policyObj.getString("intervalUnit");
					Integer repeatCount = null;
					if ("loop".equals(executeType) && intervalTime > 0) {//周期执行
						if (intervalUnit.equalsIgnoreCase("day")) {
							intervalTime = intervalTime * 24 * 60 * 60;
						} else {
							intervalTime = intervalTime * 60 * 60;
						}
					}else {//单次执行
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

					JobObject.Builder newJobObjectBuilder = new JobObject.Builder(processTaskSlaNotifyVo.getId().toString(), this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid())
							.withBeginTime(notifyDate.getTime())
							.withIntervalInSeconds(intervalTime)
							.withRepeatCount(repeatCount)
							.addData("slaNotifyId", processTaskSlaNotifyVo.getId());
					JobObject newJobObject = newJobObjectBuilder.build();
					Date triggerDate = schedulerManager.loadJob(newJobObject);
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
						//IProcessStepUtilHandler processStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler();
						ProcessTaskVo processTaskVo = getProcessTaskDetailById(processTaskSlaVo.getProcessTaskId());
						JSONObject conditionParamData = ProcessTaskUtil.getProcessFieldData(processTaskVo, true);
						JSONObject templateParamData = ProcessTaskUtil.getProcessFieldData(processTaskVo, false);
						Map<String, List<NotifyReceiverVo>> receiverMap = new HashMap<>();
						for(ProcessTaskStepVo processTaskStepVo : processTaskStepList) {
						    getReceiverMap(processTaskStepVo.getProcessTaskId(), processTaskStepVo.getId(), receiverMap);							
						}
						NotifyPolicyUtil.execute(policyConfig, paramMappingList, NotifyTriggerType.TIMEOUT, templateParamData, conditionParamData, receiverMap);
					}
				}
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
	
    private ProcessTaskVo getProcessTaskDetailById(Long processTaskId) {
      //获取工单基本信息(title、channel_uuid、config_hash、priority_uuid、status、start_time、end_time、expire_time、owner、ownerName、reporter、reporterName)
        ProcessTaskVo processTaskVo = processTaskMapper.getProcessTaskBaseInfoById(processTaskId);
        //获取工单流程图信息
        ProcessTaskConfigVo processTaskConfig = processTaskMapper.getProcessTaskConfigByHash(processTaskVo.getConfigHash());
        if(processTaskConfig == null) {
            throw new ProcessTaskRuntimeException("没有找到工单：'" + processTaskId + "'的流程图配置信息");
        }
        processTaskVo.setConfig(processTaskConfig.getConfig());
        
        //优先级
        PriorityVo priorityVo = priorityMapper.getPriorityByUuid(processTaskVo.getPriorityUuid());
        if(priorityVo == null) {
            priorityVo = new PriorityVo();
            priorityVo.setUuid(processTaskVo.getPriorityUuid());
        }
        processTaskVo.setPriority(priorityVo);
        //上报服务路径
        ChannelVo channelVo = channelMapper.getChannelByUuid(processTaskVo.getChannelUuid());
        if(channelVo != null) {
            CatalogVo catalogVo = catalogMapper.getCatalogByUuid(channelVo.getParentUuid());
            if(catalogVo != null) {
                List<CatalogVo> catalogList = catalogMapper.getAncestorsAndSelfByLftRht(catalogVo.getLft(), catalogVo.getRht());
                List<String> nameList = catalogList.stream().map(CatalogVo::getName).collect(Collectors.toList());
                nameList.add(channelVo.getName());
                processTaskVo.setChannelPath(String.join("/", nameList));
            }
            ChannelTypeVo channelTypeVo =  channelMapper.getChannelTypeByUuid(channelVo.getChannelTypeUuid());
            if(channelTypeVo == null) {
                channelTypeVo = new ChannelTypeVo();
                channelTypeVo.setUuid(channelVo.getChannelTypeUuid());
            }
            processTaskVo.setChannelType(channelTypeVo);
        }
        //耗时
        if(processTaskVo.getEndTime() != null) {
            long timeCost = worktimeMapper.calculateCostTime(processTaskVo.getWorktimeUuid(), processTaskVo.getStartTime().getTime(), processTaskVo.getEndTime().getTime());
            processTaskVo.setTimeCost(timeCost);
        }
        
        //获取工单表单信息
        ProcessTaskFormVo processTaskFormVo = processTaskMapper.getProcessTaskFormByProcessTaskId(processTaskId);
        if(processTaskFormVo != null && StringUtils.isNotBlank(processTaskFormVo.getFormContent())) {
            processTaskVo.setFormConfig(processTaskFormVo.getFormContent());            
            List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList = processTaskMapper.getProcessTaskStepFormAttributeDataByProcessTaskId(processTaskId);
            for(ProcessTaskFormAttributeDataVo processTaskFormAttributeDataVo : processTaskFormAttributeDataList) {
                processTaskVo.getFormAttributeDataMap().put(processTaskFormAttributeDataVo.getAttributeUuid(), processTaskFormAttributeDataVo.getDataObj());
            }
        }
        /** 上报人公司列表 **/
        List<String> teamUuidList = teamMapper.getTeamUuidListByUserUuid(processTaskVo.getOwner());
        if(CollectionUtils.isNotEmpty(teamUuidList)) {
            List<TeamVo> teamList = teamMapper.getTeamByUuidList(teamUuidList);
            for(TeamVo teamVo : teamList) {
                List<TeamVo> companyList = teamMapper.getAncestorsAndSelfByLftRht(teamVo.getLft(), teamVo.getRht(), TeamLevel.COMPANY.getValue());
                if(CollectionUtils.isNotEmpty(companyList)) {
                    processTaskVo.getOwnerCompanyList().addAll(companyList);
                }
            }
        }
        processTaskVo.setStartProcessTaskStep(getStartProcessTaskStepByProcessTaskId(processTaskId));
        return processTaskVo;
    }
    
    private ProcessTaskStepVo getStartProcessTaskStepByProcessTaskId(Long processTaskId) {
        //获取开始步骤id
        List<ProcessTaskStepVo> processTaskStepList = processTaskMapper.getProcessTaskStepByProcessTaskIdAndType(processTaskId, ProcessStepType.START.getValue());
        if(processTaskStepList.size() != 1) {
            throw new ProcessTaskRuntimeException("工单：'" + processTaskId + "'有" + processTaskStepList.size() + "个开始步骤");
        }

        ProcessTaskStepVo startProcessTaskStepVo = processTaskStepList.get(0);
        String stepConfig = processTaskMapper.getProcessTaskStepConfigByHash(startProcessTaskStepVo.getConfigHash());
        startProcessTaskStepVo.setConfig(stepConfig);
        ProcessStepHandlerVo processStepHandlerConfig = processStepHandlerMapper.getProcessStepHandlerByHandler(startProcessTaskStepVo.getHandler());
        if(processStepHandlerConfig != null) {
            startProcessTaskStepVo.setGlobalConfig(processStepHandlerConfig.getConfig());                    
        }

        ProcessTaskStepReplyVo comment = new ProcessTaskStepReplyVo();
        //获取上报描述内容
        List<Long> fileIdList = new ArrayList<>();
        List<ProcessTaskStepContentVo> processTaskStepContentList = processTaskMapper.getProcessTaskStepContentByProcessTaskStepId(startProcessTaskStepVo.getId());
        for(ProcessTaskStepContentVo processTaskStepContent : processTaskStepContentList) {
            if (ProcessTaskStepAction.STARTPROCESS.getValue().equals(processTaskStepContent.getType())) {
                fileIdList = processTaskMapper.getFileIdListByContentId(processTaskStepContent.getId());
                comment.setContent(processTaskMapper.getProcessTaskContentStringByHash(processTaskStepContent.getContentHash()));
                break;
            }
        }
        //附件       
        if(CollectionUtils.isNotEmpty(fileIdList)) {
            comment.setFileList(fileMapper.getFileListByIdList(fileIdList));
        }
        startProcessTaskStepVo.setComment(comment);
        /** 当前步骤特有步骤信息 **/
//        IProcessStepUtilHandler startProcessStepUtilHandler = ProcessStepUtilHandlerFactory.getHandler(startProcessTaskStepVo.getHandler());
//        if(startProcessStepUtilHandler == null) {
//            throw new ProcessStepHandlerNotFoundException(startProcessTaskStepVo.getHandler());
//        }
//        startProcessTaskStepVo.setHandlerStepInfo(startProcessStepUtilHandler.getHandlerStepInfo(startProcessTaskStepVo.getId()));
        return startProcessTaskStepVo;
    }
    
    private void getReceiverMap(Long processTaskId, Long processTaskStepId,
        Map<String, List<NotifyReceiverVo>> receiverMap) {
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
