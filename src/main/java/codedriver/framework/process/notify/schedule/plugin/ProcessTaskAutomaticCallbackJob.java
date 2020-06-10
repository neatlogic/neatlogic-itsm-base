package codedriver.framework.process.notify.schedule.plugin;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.scheduler.core.JobBase;
import codedriver.framework.scheduler.dto.JobObject;

@Component
public class ProcessTaskAutomaticCallbackJob extends JobBase {
	@Override
	public String getGroupName() {
		return TenantContext.get().getTenantUuid() + "-PROCESSTASK-AUTOMATIC-CALLBACK";
	}

	@Override
	public Boolean checkCronIsExpired(JobObject jobObject) {
		return false;
	}

	@Override
	public void reloadJob(JobObject jobObject) {
		//JobObject.Builder newJobObjectBuilder = new JobObject.Builder("1111111111111111", this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid()).withBeginTime(new Date()).withIntervalInSeconds(5).addData("automaticId", "11111");
		//JobObject newJobObject = newJobObjectBuilder.build();
		//Date triggerDate = schedulerManager.loadJob(newJobObject);
		//System.out.println(triggerDate);
	}

	@Override
	public void initJob(String tenantUuid) {
		//JobObject.Builder jobObjectBuilder = new JobObject.Builder("1111111111111111", this.getGroupName(), this.getClassName(), TenantContext.get().getTenantUuid()).addData("automaticId", "11111");
		//JobObject jobObject = jobObjectBuilder.build();
		//this.reloadJob(jobObject);
		
	}

	@Override
	public void executeInternal(JobExecutionContext context, JobObject jobObject) throws JobExecutionException {
		System.out.println(jobObject.getData("automaticId"));
		
	}
}
