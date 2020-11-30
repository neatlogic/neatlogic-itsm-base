package codedriver.framework.process.score.schedule.plugin;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.SystemUser;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.WorktimeMapper;
import codedriver.framework.process.dao.mapper.score.ProcessTaskScoreMapper;
import codedriver.framework.process.dao.mapper.score.ScoreTemplateMapper;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.dto.score.ProcessTaskAutoScoreVo;
import codedriver.framework.process.dto.score.ProcessTaskScoreVo;
import codedriver.framework.process.dto.score.ScoreTemplateDimensionVo;
import codedriver.framework.process.dto.score.ScoreTemplateVo;
import codedriver.framework.process.stephandler.core.IProcessStepHandler;
import codedriver.framework.process.stephandler.core.ProcessStepHandlerFactory;
import codedriver.framework.process.util.WorkTimeUtil;
import codedriver.framework.scheduler.core.JobBase;
import codedriver.framework.scheduler.dto.JobObject;

/**
 * 工单自动评分定时类
 */
@Component
public class ProcessTaskAutoScoreJob extends JobBase {
	static Logger logger = LoggerFactory.getLogger(ProcessTaskAutoScoreJob.class);

	@Autowired
	private ProcessTaskMapper processTaskMapper;

	@Autowired
	private ScoreTemplateMapper scoreTemplateMapper;

	@Autowired
	private ProcessTaskScoreMapper processTaskScoreMapper;

	@Autowired
	private WorktimeMapper worktimeMapper;

	@Override
	public Boolean checkCronIsExpired(JobObject jobObject) {
		Long processTaskId = (Long) jobObject.getData("processTaskId");
		List<ProcessTaskScoreVo> processtaskScoreVos = processTaskScoreMapper.searchProcessTaskScoreByProcesstaskId(processTaskId);
		if (CollectionUtils.isEmpty(processtaskScoreVos)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void reloadJob(JobObject jobObject) {
	    System.out.println("2");
		String tenantUuid = jobObject.getTenantUuid();
		TenantContext.get().switchTenant(tenantUuid);
		Long processTaskId = (Long) jobObject.getData("processTaskId");
		{
		List<ProcessTaskScoreVo> processtaskScoreVos = processTaskScoreMapper.searchProcessTaskScoreByProcesstaskId(processTaskId);
		if(CollectionUtils.isEmpty(processtaskScoreVos)){
		    /** 如果没有评分记录，那么读取评分配置 */
		    ProcessTaskVo task = processTaskMapper.getProcessTaskById(processTaskId);
	        if(task != null) {
	            String config = processTaskScoreMapper.getProcessTaskAutoScoreConfigByProcessTaskId(processTaskId);
	            Integer autoTime = (Integer)JSONPath.read(config, "config.autoTime");
	            if(autoTime != null) {
	                String autoTimeType = (String)JSONPath.read(config, "config.autoTimeType");
	                /**
	                 * 如果没有设置评分时限类型是自然日还是工作日，默认按自然日顺延
	                 * 如果设置为工作日，那么获取当前时间以后的工作日历，按工作日历顺延
	                 */
	                Date autoScoreDate = null;
	                if("workDay".equals(autoTimeType) && worktimeMapper.checkWorktimeIsExists(task.getWorktimeUuid()) > 0) {
//	                    long timeLimit = DateUtils.addDays(task.getEndTime(), autoTime).getTime() - task.getEndTime().getTime();
//	                    long expireTime = WorkTimeUtil.calculateExpireTime(task.getEndTime().getTime(), timeLimit, task.getWorktimeUuid());
	                    long expireTime = WorkTimeUtil.calculateExpireTime(task.getEndTime().getTime(), TimeUnit.DAYS.toMillis(autoTime), task.getWorktimeUuid());
	                    autoScoreDate = new Date(expireTime);
	                }else {
//	                    autoScoreDate = DateUtils.addDays(task.getEndTime(), Integer.parseInt(autoTime.toString()));
	                    //autoScoreDate = new Date(task.getEndTime().getTime() + TimeUnit.DAYS.toMillis(autoTime));
	                    autoScoreDate = new Date(task.getEndTime().getTime() + TimeUnit.MINUTES.toMillis(1));
	                }
	                ProcessTaskAutoScoreVo processTaskAutoScoreVo = new ProcessTaskAutoScoreVo();
	                processTaskAutoScoreVo.setProcessTaskId(processTaskId);
	                processTaskAutoScoreVo.setTriggerTime(autoScoreDate);
	                processTaskScoreMapper.updateProcessTaskAutoScoreByProcessTaskId(processTaskAutoScoreVo);
	                JobObject.Builder newJobObjectBuilder = new JobObject.Builder(processTaskId.toString(), this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid())
	                    .withBeginTime(autoScoreDate)
	                    //.withBeginTime(new Date(System.currentTimeMillis() + 10 * 1000))
	                    .withIntervalInSeconds(60 * 60)
	                    .withRepeatCount(0)
	                    .addData("processTaskId", processTaskId);
	                JobObject newJobObject = newJobObjectBuilder.build();
	                schedulerManager.loadJob(newJobObject);
	            }
	            
	        }
		}
		}
//		String worktimeUuid = null;
//		Integer isAuto = null;
//		Integer autoTime = null;
//		String autoTimeType = null;
//		if(task != null){
//			List<ProcessTaskScoreVo> processtaskScoreVos = processTaskScoreMapper.searchProcessTaskScoreByProcesstaskId(processTaskId);
//			ChannelVo channel = channelMapper.getChannelByUuid(task.getChannelUuid());
//			if(channel != null){
//				worktimeUuid = channel.getWorktimeUuid();
//			}
//			/** 如果没有评分记录，那么读取评分配置 */
//			if(CollectionUtils.isEmpty(processtaskScoreVos)){
//				String configHash = task.getConfigHash();
//				ProcessTaskConfigVo taskConfigVo = null;
//				if(StringUtils.isNotBlank(configHash) && (taskConfigVo = selectContentByHashMapper.getProcessTaskConfigByHash(configHash)) != null){
//					JSONObject taskConfigObj = JSONObject.parseObject(taskConfigVo.getConfig());
//					if(MapUtils.isNotEmpty(taskConfigObj) && MapUtils.isNotEmpty(taskConfigObj.getJSONObject("process"))){
//						JSONObject scoreConfig = taskConfigObj.getJSONObject("process").getJSONObject("scoreConfig");
//						JSONObject config = null;
//						if(MapUtils.isNotEmpty(scoreConfig) && MapUtils.isNotEmpty(config = scoreConfig.getJSONObject("config"))){
//							isAuto = scoreConfig.getInteger("isAuto");
//							autoTime = config.getInteger("autoTime");
//							autoTimeType = config.getString("autoTimeType");
//						}
//					}
//				}
//			}
//		}
//		/** 如果设置了自动评分，则启动定时任务 */
//		if(isAuto != null && Integer.parseInt(isAuto.toString()) == 1 && autoTime != null){
//			/**
//			 * 如果没有设置评分时限类型是自然日还是工作日，默认按自然日顺延
//			 * 如果设置为工作日，那么获取当前时间以后的工作日历，按工作日历顺延
//			 */
//			Date autoScoreDate = DateUtils.addDays(task.getEndTime(), Integer.parseInt(autoTime.toString()));
//			if(StringUtils.isNotBlank(autoTimeType) && "workDay".equals(autoTimeType) && StringUtils.isNotBlank(worktimeUuid) && worktimeMapper.checkWorktimeIsExists(worktimeUuid) > 0){
//				long timeLimit = DateUtils.addDays(task.getEndTime(), Integer.parseInt(autoTime.toString())).getTime() - task.getEndTime().getTime();
//				long expireTime = WorkTimeUtil.calculateExpireTime(task.getEndTime().getTime(), timeLimit, worktimeUuid);
//				autoScoreDate = new Date(expireTime);
//			}
//			JobObject.Builder newJobObjectBuilder = new JobObject.Builder(processTaskId.toString(), this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid())
//					//.withBeginTime(autoScoreDate)
//			        .withBeginTime(new Date(System.currentTimeMillis() + 10 * 1000))
//					.withIntervalInSeconds(60 * 60)
//					.withRepeatCount(0)
//					.addData("processTaskId", processTaskId);
//			JobObject newJobObject = newJobObjectBuilder.build();
//			schedulerManager.loadJob(newJobObject);
//		}
	}

	@Override
	public void initJob(String tenantUuid) {
	    System.out.println("1");
	    List<Long> processTaskIdList = processTaskScoreMapper.getAllProcessTaskAutoScoreProcessTaskIdList();
	    for(Long processTaskId : processTaskIdList) {
	        JobObject.Builder jobObjectBuilder = new JobObject.Builder(processTaskId.toString(), this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid()).addData("processTaskId", processTaskId);
            JobObject jobObject = jobObjectBuilder.build();
            this.reloadJob(jobObject);
	    }
	}

	@Override
	public void executeInternal(JobExecutionContext context, JobObject jobObject) throws JobExecutionException {
	    System.out.println("3");
        Long processTaskId = (Long) jobObject.getData("processTaskId");
        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeee");
	    List<ProcessTaskScoreVo> processTaskScoreVos = processTaskScoreMapper.searchProcessTaskScoreByProcesstaskId(processTaskId);
	    if(CollectionUtils.isEmpty(processTaskScoreVos)) {
	        System.out.println("4");
	        ProcessTaskVo task = processTaskMapper.getProcessTaskById(processTaskId);
	        if(task != null) {
	            System.out.println("5");
	            String config = processTaskScoreMapper.getProcessTaskAutoScoreConfigByProcessTaskId(processTaskId);
	            Long scoreTemplateId = (Long)JSONPath.read(config, "scoreTemplateId");
	            //String config = selectContentByHashMapper.getProcessTaskConfigStringByHash(task.getConfigHash());
	            //Long scoreTemplateId = (Long)JSONPath.read(config, "process.scoreConfig.scoreTemplateId");
	            ScoreTemplateVo template = scoreTemplateMapper.getScoreTemplateById(scoreTemplateId);
	            if(template != null) {
	                System.out.println("6");
	                List<ScoreTemplateDimensionVo> dimensionList = template.getDimensionList();
                    System.out.println("dimensionList:" + dimensionList);
	                if(CollectionUtils.isNotEmpty(dimensionList)){
                        System.out.println("7");
	                    for(ScoreTemplateDimensionVo vo : dimensionList){
	                        vo.setScore(5);
	                    }
	                    JSONObject paramObj = new JSONObject();
	                    paramObj.put("scoreTemplateId", scoreTemplateId);
	                    paramObj.put("scoreDimensionList", dimensionList);
	                    //paramObj.put("content", "系统自动评价");
	                    task.setParamObj(paramObj);
	                    System.out.println("8");
	                    IProcessStepHandler stepHandler = ProcessStepHandlerFactory.getHandler();
	                    System.out.println(stepHandler);
	                    System.out.println("9");
    	                    while(stepHandler == null) {
    	                        try {
                                    TimeUnit.SECONDS.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
    	                        stepHandler = ProcessStepHandlerFactory.getHandler();
    	                        System.out.println("####################################################");
    	                    }
    	                    System.out.println(stepHandler);
                        /** 执行转交前，设置当前用户为system,用于权限校验 **/
                        UserContext.init(SystemUser.SYSTEM.getConfig(), null, SystemUser.SYSTEM.getTimezone(), null, null);
                        System.out.println("自动评分：" + processTaskId);
                        stepHandler.scoreProcessTask(task);
                        System.out.println("10");
	                }
	            }
	        }
	    }
	    schedulerManager.unloadJob(jobObject);
        processTaskScoreMapper.deleteProcessTaskAutoScoreByProcessTaskId(processTaskId);
        System.out.println("####################################################");
//		Long processTaskId = (Long) jobObject.getData("processTaskId");
//		ProcessTaskVo task = processTaskMapper.getProcessTaskById(processTaskId);
//		List<ProcessTaskScoreVo> processTaskScoreVos = processTaskScoreMapper.searchProcessTaskScoreByProcesstaskId(processTaskId);
//		ScoreTemplateVo template = null;
//		// 从processtask_config里查评分模版Id，获取评分模版
//		if(task != null && CollectionUtils.isEmpty(processTaskScoreVos)){
//			String configHash = task.getConfigHash();
//			ProcessTaskConfigVo taskConfigVo = null;
//			if(StringUtils.isNotBlank(configHash) && (taskConfigVo = selectContentByHashMapper.getProcessTaskConfigByHash(configHash)) != null){
//				JSONObject taskConfigObj = JSONObject.parseObject(taskConfigVo.getConfig());
//				if(MapUtils.isNotEmpty(taskConfigObj) && MapUtils.isNotEmpty(taskConfigObj.getJSONObject("process"))){
//					JSONObject scoreConfig = taskConfigObj.getJSONObject("process").getJSONObject("scoreConfig");
//					Long scoreTemplateId = null;
//					if(MapUtils.isNotEmpty(scoreConfig) && (scoreTemplateId = scoreConfig.getLong("scoreTemplateId")) != null){
//						template = scoreTemplateMapper.getScoreTemplateById(scoreTemplateId);
//					}
//				}
//			}
//		}
//		if(template != null){
//			IProcessStepUtilHandler handler = ProcessStepUtilHandlerFactory.getHandler();
//			List<ScoreTemplateDimensionVo> dimensionList = template.getDimensionList();
//			ProcessTaskScoreVo processtaskScoreVo = new ProcessTaskScoreVo();
//			processtaskScoreVo.setProcessTaskId(processTaskId);
//			processtaskScoreVo.setScoreTemplateId(template.getId());
//			processtaskScoreVo.setFcu(SystemUser.SYSTEM.getUserId());
//			processtaskScoreVo.setIsAuto(1);
//			JSONArray dimensionArray = new JSONArray();
//			if(CollectionUtils.isNotEmpty(dimensionList)){
//				for(ScoreTemplateDimensionVo vo : dimensionList){
//					processtaskScoreVo.setScoreDimensionId(vo.getId());
//					processtaskScoreVo.setScore(5);
//					processTaskScoreMapper.insertProcessTaskScore(processtaskScoreVo);
//					JSONObject dimensionObj = new JSONObject();
//					dimensionObj.put("name",vo.getName());
//					dimensionObj.put("score",5);
//					dimensionObj.put("description",vo.getDescription());
//					dimensionArray.add(dimensionObj);
//				}
//			}
//			JSONObject contentObj = new JSONObject();
//			contentObj.put("scoreTemplateId",template.getId());
//			contentObj.put("dimensionList",dimensionArray);
//			JSONObject paramObj = new JSONObject();
//			paramObj.put("score",contentObj);
//			ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
//			processTaskStepVo.setProcessTaskId(processTaskId);
//			processTaskStepVo.setParamObj(paramObj);
//			/** 生成活动 */
//			handler.activityAudit(processTaskStepVo, ProcessTaskAuditType.SCORE);
//		}else{
//			schedulerManager.unloadJob(jobObject);
//		}
	}

	@Override
	public String getGroupName() {
		return TenantContext.get().getTenantUuid() + "-PROCESSTASK-AUTOSCORE";
	}

}
