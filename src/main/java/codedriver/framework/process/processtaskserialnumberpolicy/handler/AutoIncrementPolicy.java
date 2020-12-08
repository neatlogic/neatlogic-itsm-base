package codedriver.framework.process.processtaskserialnumberpolicy.handler;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.util.PageUtil;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskSerialNumberMapper;
import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.processtaskserialnumberpolicy.core.IProcessTaskSerialNumberPolicyHandler;

@Service
public class AutoIncrementPolicy implements IProcessTaskSerialNumberPolicyHandler {

    @Autowired
    private ProcessTaskSerialNumberMapper processTaskSerialNumberMapper;

    @Autowired
    private ProcessTaskMapper processTaskMapper;

    @Override
    public String getName() {
        return "自增序列";
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
        long max = (long)Math.pow(10, digits) - 1;
        long serialNumberSeed = processTaskSerialNumberPolicyVo.getSerialNumberSeed();
        if(serialNumberSeed > max) {
            serialNumberSeed -= max;
        }
        processTaskSerialNumberMapper.updateProcessTaskSerialNumberPolicySerialNumberSeedByChannelTypeUuid(
            processTaskSerialNumberPolicyVo.getChannelTypeUuid(), serialNumberSeed + 1);
        return String.format("%0" + digits + "d", serialNumberSeed);
    }

    @Override
    public int batchUpdateHistoryProcessTask(String channelTypeUuid) {
        int rowNum = processTaskMapper.getProcessTaskCountByChannelTypeUuid(channelTypeUuid);
        if (rowNum > 0) {
            /** 加锁 **/
            ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo =
                processTaskSerialNumberMapper.getProcessTaskSerialNumberPolicyLockByChannelTypeUuid(channelTypeUuid);
            int digits = processTaskSerialNumberPolicyVo.getConfig().getIntValue("digits");
            long max = (long)Math.pow(10, digits) - 1;
            long startValue = processTaskSerialNumberPolicyVo.getConfig().getLongValue("startValue");
            int pageSize = 1000;
            int pageCount = PageUtil.getPageCount(rowNum, pageSize);
            ProcessTaskVo processTaskVo = new ProcessTaskVo();
            processTaskVo.setChannelTypeUuid(channelTypeUuid);
            for (int currentPage = 1; currentPage <= pageCount; currentPage++) {
                processTaskVo.setCurrentPage(currentPage);
                List<ProcessTaskVo> processTaskList =
                    processTaskMapper.getProcessTaskListByChannelTypeUuid(processTaskVo);
                for (ProcessTaskVo processTask : processTaskList) {
                    String serialNumber = String.format("%0" + digits + "d", startValue);
                    processTaskMapper.updateProcessTaskSerialNumberById(processTask.getId(), serialNumber);
                    processTaskSerialNumberMapper.insertProcessTaskSerialNumber(processTask.getId(), serialNumber);
                    startValue++;
                    if(startValue > max) {
                        startValue -= max;
                    }
                }
            }
            processTaskSerialNumberPolicyVo.setSerialNumberSeed(startValue);
            processTaskSerialNumberMapper.updateProcessTaskSerialNumberPolicySerialNumberSeedByChannelTypeUuid(channelTypeUuid, startValue);
        }
        return rowNum;
    }
}
