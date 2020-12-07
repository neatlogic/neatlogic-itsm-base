package codedriver.framework.process.processtaskserialnumberpolicy.handler;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import codedriver.framework.process.dao.mapper.ChannelMapper;
import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.processtaskserialnumberpolicy.core.IProcessTaskSerialNumberPolicyHandler;
@Service
public class DateTimeAndAutoIncrementPolicy implements IProcessTaskSerialNumberPolicyHandler {

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public String getName() {
        return "日期 + 自增序列";
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
        if(startValue == null) {
            startValue = 0L;
        }
        resultObj.put("startValue", startValue);
        resultObj.put("digits", jsonObj.getInteger("digits"));
        return resultObj;
    }

    @Override
    public String genarate(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo,
        ProcessTaskVo processTaskVo) {
        channelMapper.updateProcessTaskSerialNumberPolicySerialNumberSeedByChannelTypeUuid(processTaskSerialNumberPolicyVo.getChannelTypeUuid());
        Integer digits = (Integer)JSONPath.read(processTaskSerialNumberPolicyVo.getConfig(), "digits");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date startTime = new Date();
        if(processTaskVo != null) {
            startTime = processTaskVo.getStartTime();
        }
        return sdf.format(startTime) + String.format("%0" + digits + "d", processTaskSerialNumberPolicyVo.getSerialNumberSeed());
    }
    
}
