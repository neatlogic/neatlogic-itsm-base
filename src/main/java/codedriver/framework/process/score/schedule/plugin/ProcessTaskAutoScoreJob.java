package codedriver.framework.process.score.schedule.plugin;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.common.constvalue.SystemUser;
import codedriver.framework.process.constvalue.ProcessTaskAuditType;
import codedriver.framework.process.dao.mapper.ChannelMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.score.ProcesstaskScoreMapper;
import codedriver.framework.process.dao.mapper.score.ScoreTemplateMapper;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.dto.score.ProcessScoreTemplateVo;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 工单自动评分定时类
 */
@Component
public class ProcessTaskAutoScoreJob extends JobBase {
	static Logger logger = LoggerFactory.getLogger(ProcessTaskAutoScoreJob.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private ProcessTaskMapper processTaskMapper;

	@Autowired
	private ScoreTemplateMapper scoreTemplateMapper;

	@Autowired
	private ProcesstaskScoreMapper processtaskScoreMapper;

	@Autowired
	private ChannelMapper channelMapper;

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
		List<ProcesstaskScoreVo> processtaskScoreVos = processtaskScoreMapper.searchProcesstaskScoreByProcesstaskId(processTaskId);
		ProcessScoreTemplateVo processScoreTemplate = null;
		if(task != null && CollectionUtils.isEmpty(processtaskScoreVos)){
			processScoreTemplate = scoreTemplateMapper.getProcessScoreTemplateByProcessUuid(task.getProcessUuid());
		}
		String config = null;
		Integer isAuto = null;
		Integer autoTime = null;
		String autoTimeType = null;
		if(processScoreTemplate != null && StringUtils.isNotBlank(config = processScoreTemplate.getConfig())) {
			JSONObject configObj = JSONObject.parseObject(config);
			isAuto = configObj.getInteger("isAuto");
			autoTime = configObj.getInteger("autoTime");
			autoTimeType = configObj.getString("autoTimeType");
		}
		if(isAuto != null && Integer.parseInt(isAuto.toString()) == 1 && autoTime != null){
			/**
			 * 如果没有设置评分时限类型是自然日还是工作日，默认按自然日顺延
			 * 如果设置为工作日，那么获取当前时间以后的工作日历，按工作日历顺延
			 */
			Date autoScoreDate = DateUtils.addDays(task.getEndTime(), Integer.parseInt(autoTime.toString()));
			if(StringUtils.isNotBlank(autoTimeType) && "workDay".equals(autoTimeType)){
				String channelUuid = task.getChannelUuid();
				ChannelVo channel = channelMapper.getChannelByUuid(channelUuid);
				String worktimeUuid = channel.getWorktimeUuid();
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
		ProcessScoreTemplateVo processScoreTemplate = null;
		if(task != null && CollectionUtils.isEmpty(processtaskScoreVos)){
			processScoreTemplate = scoreTemplateMapper.getProcessScoreTemplateByProcessUuid(task.getProcessUuid());
		}
		ScoreTemplateVo template = null;
		if(processScoreTemplate != null){
			template = scoreTemplateMapper.getScoreTemplateById(processScoreTemplate.getScoreTemplateId());
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
