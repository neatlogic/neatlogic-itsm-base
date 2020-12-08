package codedriver.framework.process.processtaskserialnumberpolicy.handler;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.quartz.CronExpression;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.common.util.PageUtil;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskSerialNumberMapper;
import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.processtaskserialnumberpolicy.core.IProcessTaskSerialNumberPolicyHandler;
import codedriver.framework.scheduler.core.IJob;
import codedriver.framework.scheduler.core.JobBase;
import codedriver.framework.scheduler.core.SchedulerManager;
import codedriver.framework.scheduler.dto.JobObject;

@Service
public class DateTimeAndAutoIncrementPolicy implements IProcessTaskSerialNumberPolicyHandler {

    @Autowired
    private ProcessTaskSerialNumberMapper processTaskSerialNumberMapper;

    @Autowired
    private ProcessTaskMapper processTaskMapper;

    @Override
    public String getName() {
        return "日期 + 自增序列";
    }
    
    private static String cron = "0 0 0 * * ?";
    
    @PostConstruct
    public void init() {
        System.out.println("111111111111111111");
        List<ProcessTaskSerialNumberPolicyVo> processTaskSerialNumberPolicyList =
            processTaskSerialNumberMapper.getProcessTaskSerialNumberPolicyListByHandler(this.getHandler());
        IJob job = SchedulerManager.getHandler(codedriver.framework.process.processtaskserialnumberpolicy.handler.DateTimeAndAutoIncrementPolicy.ProcessTaskSerialNumberSeedResetJob.class.getName());
        for (ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo : processTaskSerialNumberPolicyList) {
                if (CronExpression.isValidExpression(cron)) {
                    JobObject.Builder jobObjectBuilder =
                        new JobObject.Builder(processTaskSerialNumberPolicyVo.getChannelTypeUuid(), job.getGroupName(),
                            job.getClassName(), TenantContext.get().getTenantUuid()).addData("channelTypeUuid",
                                processTaskSerialNumberPolicyVo.getChannelTypeUuid());
                    JobObject jobObject = jobObjectBuilder.build();
                    job.reloadJob(jobObject);
                }
        }
    }

    @Override
    public JSONArray makeupFormAttributeList() {
        JSONArray resultArray = new JSONArray();
        {
            /** 起始值 **/
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("type", "text");
            jsonObj.put("name", "startValue");
            jsonObj.put("label", "起始值");
            jsonObj.put("validateList", Arrays.asList("required"));
            jsonObj.put("value", "");
            jsonObj.put("defaultValue", "");
            resultArray.add(jsonObj);
        }
        {
            /** 位数 **/
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("type", "text");
            jsonObj.put("name", "digits");
            jsonObj.put("label", "位数");
            jsonObj.put("validateList", Arrays.asList("required"));
            jsonObj.put("value", "");
            jsonObj.put("defaultValue", "");
            resultArray.add(jsonObj);
        }
        return resultArray;
    }

    @Override
    public JSONObject makeupConfig(JSONObject jsonObj) {
        JSONObject resultObj = new JSONObject();
        Long startValue = jsonObj.getLong("startValue");
        if (startValue == null) {
            startValue = 0L;
        }
        resultObj.put("startValue", startValue);
        resultObj.put("digits", jsonObj.getInteger("digits"));
        return resultObj;
    }

    @Override
    public String genarate(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo) {
        int digits = processTaskSerialNumberPolicyVo.getConfig().getIntValue("digits");
        long max = (long)Math.pow(10, digits);
        long serialNumberSeed = processTaskSerialNumberPolicyVo.getSerialNumberSeed();
        if(serialNumberSeed > max) {
            serialNumberSeed -= max;
        }
        processTaskSerialNumberMapper.updateProcessTaskSerialNumberPolicySerialNumberSeedByChannelTypeUuid(
            processTaskSerialNumberPolicyVo.getChannelTypeUuid());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date())
            + String.format("%0" + digits + "d", serialNumberSeed);
    }

    @Override
    public int batchUpdateHistoryProcessTask(String channelTypeUuid) {
        int rowNum = processTaskMapper.getProcessTaskCountByChannelTypeUuid(channelTypeUuid);
        if (rowNum > 0) {
            /** 加锁 **/
            ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo =
                processTaskSerialNumberMapper.getProcessTaskSerialNumberPolicyLockByChannelTypeUuid(channelTypeUuid);
            Integer digits = processTaskSerialNumberPolicyVo.getConfig().getInteger("digits");
            long startValue = processTaskSerialNumberPolicyVo.getConfig().getLongValue("startValue");
            long serialNumberSeed = startValue;
            String timeFormat = null;
            int pageSize = 1000;
            int pageCount = PageUtil.getPageCount(rowNum, pageSize);
            ProcessTaskVo processTaskVo = new ProcessTaskVo();
            processTaskVo.setChannelTypeUuid(channelTypeUuid);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            for (int currentPage = 1; currentPage <= pageCount; currentPage++) {
                processTaskVo.setCurrentPage(currentPage);
                List<ProcessTaskVo> processTaskList =
                    processTaskMapper.getProcessTaskListByChannelTypeUuid(processTaskVo);
                for (ProcessTaskVo processTask : processTaskList) {
                    String startTimeFormat = sdf.format(processTask.getStartTime());
                    if (!Objects.equals(timeFormat, startTimeFormat)) {
                        serialNumberSeed = startValue;
                        timeFormat = startTimeFormat;
                    }
                    String serialNumber = startTimeFormat + String.format("%0" + digits + "d", serialNumberSeed);
                    processTaskMapper.updateProcessTaskSerialNumberById(processTask.getId(), serialNumber);
                    processTaskSerialNumberMapper.insertProcessTaskSerialNumber(processTask.getId(), serialNumber);
                    serialNumberSeed++;
                }
            }
            processTaskSerialNumberPolicyVo.setSerialNumberSeed(startValue);
            processTaskSerialNumberMapper.updateProcessTaskSerialNumberPolicyByChannelTypeUuid(processTaskSerialNumberPolicyVo);
        }
        return rowNum;
    }

    @Component
    private static class ProcessTaskSerialNumberSeedResetJob extends JobBase {

        @Autowired
        private ProcessTaskSerialNumberMapper processTaskSerialNumberMapper;

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
            ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo =
                processTaskSerialNumberMapper.getProcessTaskSerialNumberPolicyLockByChannelTypeUuid(channelTypeUuid);
            if (processTaskSerialNumberPolicyVo != null) {
                if (CronExpression.isValidExpression(cron)) {
                    JobObject.Builder newJobObjectBuilder = new JobObject.Builder(channelTypeUuid, this.getGroupName(),
                        this.getClassName(), TenantContext.get().getTenantUuid()).withCron(cron)
                            .addData("channelTypeUuid", channelTypeUuid);
                    JobObject newJobObject = newJobObjectBuilder.build();
                    schedulerManager.loadJob(newJobObject);
                }
            }
        }

        @Override
        public void initJob(String tenantUuid) {
            
        }

        @Override
        public void executeInternal(JobExecutionContext context, JobObject jobObject) throws JobExecutionException {
            String channelTypeUuid = (String)jobObject.getData("channelTypeUuid");
            ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo =
                processTaskSerialNumberMapper.getProcessTaskSerialNumberPolicyLockByChannelTypeUuid(channelTypeUuid);
            if (processTaskSerialNumberPolicyVo != null) {
                Long startValue = 1L;
                Long value = processTaskSerialNumberPolicyVo.getConfig().getLong("startValue");
                if (value != null) {
                    startValue = value;
                }
                processTaskSerialNumberPolicyVo.setSerialNumberSeed(startValue);
                processTaskSerialNumberMapper.updateProcessTaskSerialNumberPolicyByChannelTypeUuid(processTaskSerialNumberPolicyVo);
            }
        }
    }
}
