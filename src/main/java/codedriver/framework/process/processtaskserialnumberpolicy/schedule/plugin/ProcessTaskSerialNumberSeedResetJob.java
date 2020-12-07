package codedriver.framework.process.processtaskserialnumberpolicy.schedule.plugin;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.process.dao.mapper.ChannelMapper;
import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;
import codedriver.framework.process.processtaskserialnumberpolicy.core.IProcessTaskSerialNumberPolicyHandler;
import codedriver.framework.process.processtaskserialnumberpolicy.core.ProcessTaskSerialNumberPolicyHandlerFactory;
import codedriver.framework.scheduler.core.JobBase;
import codedriver.framework.scheduler.dto.JobObject;

@Component
public class ProcessTaskSerialNumberSeedResetJob extends JobBase {

    @Autowired
    private ChannelMapper channelMapper;
    
    @Override
    public String getGroupName() {
        return TenantContext.get().getTenantUuid() + "-PROCESSTASK-SERIALNUMBERSEED-RESET";
    }

    @Override
    public Boolean checkCronIsExpired(JobObject jobObject) {
        return true;
    }

    @Override
    public void reloadJob(JobObject jobObject) {
        String tenantUuid = jobObject.getTenantUuid();
        TenantContext.get().switchTenant(tenantUuid);
        String channelTypeUuid = (String)jobObject.getData("channelTypeUuid");
        ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo = channelMapper.getProcessTaskSerialNumberPolicyLockByChannelTypeUuid(channelTypeUuid);
        if(processTaskSerialNumberPolicyVo != null) {
            IProcessTaskSerialNumberPolicyHandler handler = ProcessTaskSerialNumberPolicyHandlerFactory.getHandler(processTaskSerialNumberPolicyVo.getHandler());
            if(handler != null) {
                String cron = handler.getSerialNumberSeedResetCron();
                if(StringUtils.isNotBlank(cron) && CronExpression.isValidExpression(cron)) {
                    JobObject.Builder newJobObjectBuilder = new JobObject.Builder(channelTypeUuid, this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid())
                        .withCron(cron)
                        .addData("channelTypeUuid", channelTypeUuid);
                    JobObject newJobObject = newJobObjectBuilder.build();
                    schedulerManager.loadJob(newJobObject);
                }
            }
        }
    }

    @Override
    public void initJob(String tenantUuid) {
        List<ProcessTaskSerialNumberPolicyVo> processTaskSerialNumberPolicyList = channelMapper.getAllProcessTaskSerialNumberPolicyList();
        for(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo : processTaskSerialNumberPolicyList) {
            IProcessTaskSerialNumberPolicyHandler handler = ProcessTaskSerialNumberPolicyHandlerFactory.getHandler(processTaskSerialNumberPolicyVo.getHandler());
            if(handler != null) {
                String cron = handler.getSerialNumberSeedResetCron();
                if(StringUtils.isNotBlank(cron) && CronExpression.isValidExpression(cron)) {
                    JobObject.Builder jobObjectBuilder = new JobObject.Builder(processTaskSerialNumberPolicyVo.getChannelTypeUuid(), this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid())
                        .addData("channelTypeUuid", processTaskSerialNumberPolicyVo.getChannelTypeUuid());
                    JobObject jobObject = jobObjectBuilder.build();
                    this.reloadJob(jobObject);
                }
            }
        }
    }

    @Override
    public void executeInternal(JobExecutionContext context, JobObject jobObject) throws JobExecutionException {
        String channelTypeUuid = (String)jobObject.getData("channelTypeUuid");
        ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo = channelMapper.getProcessTaskSerialNumberPolicyLockByChannelTypeUuid(channelTypeUuid);
        if(processTaskSerialNumberPolicyVo != null) {
            Long startValue = 1L;
            Long value = processTaskSerialNumberPolicyVo.getConfig().getLong("startValue");
            if(value != null) {
                startValue = value;
            }
            processTaskSerialNumberPolicyVo.setSerialNumberSeed(startValue);
            channelMapper.updateProcessTaskSerialNumberPolicyByChannelTypeUuid(processTaskSerialNumberPolicyVo);
        }
    }

}
