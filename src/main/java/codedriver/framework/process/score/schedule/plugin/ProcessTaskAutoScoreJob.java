package codedriver.framework.process.score.schedule.plugin;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.common.constvalue.SystemUser;
import codedriver.framework.process.constvalue.ProcessTaskAuditType;
import codedriver.framework.process.dao.mapper.ChannelMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.SelectContentByHashMapper;
import codedriver.framework.process.dao.mapper.WorktimeMapper;
import codedriver.framework.process.dao.mapper.score.ProcesstaskScoreMapper;
import codedriver.framework.process.dao.mapper.score.ScoreTemplateMapper;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.dto.score.ProcesstaskScoreVo;
import codedriver.framework.process.dto.score.ScoreTemplateDimensionVo;
import codedriver.framework.process.dto.score.ScoreTemplateVo;
import codedriver.framework.process.stephandler.core.IProcessStepUtilHandler;
import codedriver.framework.process.stephandler.core.ProcessStepUtilHandlerFactory;
import codedriver.framework.process.util.WorkTimeUtil;
import codedriver.framework.scheduler.core.JobBase;
import codedriver.framework.scheduler.dto.JobObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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
	private ProcesstaskScoreMapper processtaskScoreMapper;

	@Autowired
	private ChannelMapper channelMapper;

	@Autowired
	private SelectContentByHashMapper selectContentByHashMapper;

	@Autowired
	WorktimeMapper worktimeMapper;

	@Override
	public Boolean checkCronIsExpired(JobObject jobObject) {
		Long processTaskId = (Long) jobObject.getData("processTaskId");
		List<ProcesstaskScoreVo> processtaskScoreVos = processtaskScoreMapper.searchProcesstaskScoreByProcesstaskId(processTaskId);
		if (CollectionUtils.isEmpty(processtaskScoreVos)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void reloadJob(JobObject jobObject) {
		String tenantUuid = jobObject.getTenantUuid();
		TenantContext.get().switchTenant(tenantUuid);
		Long processTaskId = (Long) jobObject.getData("processTaskId");
		ProcessTaskVo task = processTaskMapper.getProcessTaskById(processTaskId);
		String worktimeUuid = null;
		Integer isAuto = null;
		Integer autoTime = null;
		String autoTimeType = null;
		if(task != null){
			List<ProcesstaskScoreVo> processtaskScoreVos = processtaskScoreMapper.searchProcesstaskScoreByProcesstaskId(processTaskId);
			ChannelVo channel = channelMapper.getChannelByUuid(task.getChannelUuid());
			if(channel != null){
				worktimeUuid = channel.getWorktimeUuid();
			}
			/** 如果没有评分记录，那么读取评分配置 */
			if(CollectionUtils.isEmpty(processtaskScoreVos)){
				String configHash = task.getConfigHash();
				ProcessTaskConfigVo taskConfigVo = null;
				if(StringUtils.isNotBlank(configHash) && (taskConfigVo = selectContentByHashMapper.getProcessTaskConfigByHash(configHash)) != null){
					JSONObject taskConfigObj = JSONObject.parseObject(taskConfigVo.getConfig());
					if(MapUtils.isNotEmpty(taskConfigObj) && MapUtils.isNotEmpty(taskConfigObj.getJSONObject("process"))){
						JSONObject scoreConfig = taskConfigObj.getJSONObject("process").getJSONObject("scoreConfig");
						JSONObject config = null;
						if(MapUtils.isNotEmpty(scoreConfig) && MapUtils.isNotEmpty(config = scoreConfig.getJSONObject("config"))){
							isAuto = config.getInteger("isAuto");
							autoTime = config.getInteger("autoTime");
							autoTimeType = config.getString("autoTimeType");
						}
					}
				}
			}
		}
		/** 如果设置了自动评分，则启动定时任务 */
		if(isAuto != null && Integer.parseInt(isAuto.toString()) == 1 && autoTime != null){
			/**
			 * 如果没有设置评分时限类型是自然日还是工作日，默认按自然日顺延
			 * 如果设置为工作日，那么获取当前时间以后的工作日历，按工作日历顺延
			 */
			Date autoScoreDate = DateUtils.addDays(task.getEndTime(), Integer.parseInt(autoTime.toString()));
			if(StringUtils.isNotBlank(autoTimeType) && "workDay".equals(autoTimeType) && StringUtils.isNotBlank(worktimeUuid) && worktimeMapper.checkWorktimeIsExists(worktimeUuid) > 0){
				long timeLimit = DateUtils.addDays(task.getEndTime(), Integer.parseInt(autoTime.toString())).getTime() - task.getEndTime().getTime();
				long expireTime = WorkTimeUtil.calculateExpireTime(task.getEndTime().getTime(), timeLimit, worktimeUuid);
				autoScoreDate = new Date(expireTime);
			}
			JobObject.Builder newJobObjectBuilder = new JobObject.Builder(processTaskId.toString(), this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid())
					.withBeginTime(autoScoreDate)
					.withIntervalInSeconds(60 * 60)
					.withRepeatCount(0)
					.addData("processTaskId", processTaskId);
			JobObject newJobObject = newJobObjectBuilder.build();
			schedulerManager.loadJob(newJobObject);
		}
	}

	@Override
	public void initJob(String tenantUuid) {
	}

	@Override
	public void executeInternal(JobExecutionContext context, JobObject jobObject) throws JobExecutionException {
		Long processTaskId = (Long) jobObject.getData("processTaskId");
		ProcessTaskVo task = processTaskMapper.getProcessTaskById(processTaskId);
		List<ProcesstaskScoreVo> processtaskScoreVos = processtaskScoreMapper.searchProcesstaskScoreByProcesstaskId(processTaskId);
		ScoreTemplateVo template = null;
		// 从processtask_config里查评分模版Id，获取评分模版
		if(task != null && CollectionUtils.isEmpty(processtaskScoreVos)){
			String configHash = task.getConfigHash();
			ProcessTaskConfigVo taskConfigVo = null;
			if(StringUtils.isNotBlank(configHash) && (taskConfigVo = selectContentByHashMapper.getProcessTaskConfigByHash(configHash)) != null){
				JSONObject taskConfigObj = JSONObject.parseObject(taskConfigVo.getConfig());
				if(MapUtils.isNotEmpty(taskConfigObj) && MapUtils.isNotEmpty(taskConfigObj.getJSONObject("process"))){
					JSONObject scoreConfig = taskConfigObj.getJSONObject("process").getJSONObject("scoreConfig");
					Long scoreTemplateId = null;
					if(MapUtils.isNotEmpty(scoreConfig) && (scoreTemplateId = scoreConfig.getLong("scoreTemplateId")) != null){
						template = scoreTemplateMapper.getScoreTemplateById(scoreTemplateId);
					}
				}
			}
		}
		if(template != null){
			IProcessStepUtilHandler handler = ProcessStepUtilHandlerFactory.getHandler();
			List<ScoreTemplateDimensionVo> dimensionList = template.getDimensionList();
			ProcesstaskScoreVo processtaskScoreVo = new ProcesstaskScoreVo();
			processtaskScoreVo.setProcesstaskId(processTaskId);
			processtaskScoreVo.setScoreTemplateId(template.getId());
			processtaskScoreVo.setFcu(SystemUser.SYSTEM.getUserId());
			processtaskScoreVo.setIsAuto(1);
			JSONArray dimensionArray = new JSONArray();
			if(CollectionUtils.isNotEmpty(dimensionList)){
				for(ScoreTemplateDimensionVo vo : dimensionList){
					processtaskScoreVo.setScoreDimensionId(vo.getId());
					processtaskScoreVo.setScore(5);
					processtaskScoreMapper.insertProcesstaskScore(processtaskScoreVo);
					JSONObject dimensionObj = new JSONObject();
					dimensionObj.put("dimensionName",vo.getName());
					dimensionObj.put("score",5);
					dimensionObj.put("description",vo.getDescription());
					dimensionArray.add(dimensionObj);
				}
			}
			JSONObject contentObj = new JSONObject();
			contentObj.put("scoreTemplateId",template.getId());
			contentObj.put("dimensionList",dimensionArray);
			JSONObject paramObj = new JSONObject();
			paramObj.put("score",contentObj);
			ProcessTaskStepVo processTaskStepVo = new ProcessTaskStepVo();
			processTaskStepVo.setProcessTaskId(processTaskId);
			processTaskStepVo.setParamObj(paramObj);
			/** 生成活动 */
			handler.activityAudit(processTaskStepVo, ProcessTaskAuditType.SCORE);
		}else{
			schedulerManager.unloadJob(jobObject);
		}
	}

	@Override
	public String getGroupName() {
		return TenantContext.get().getTenantUuid() + "-PROCESSTASK-AUTOSCORE";
	}

}
