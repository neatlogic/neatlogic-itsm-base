package codedriver.framework.process.processtaskserialnumberpolicy.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;

public interface IProcessTaskSerialNumberPolicyHandler {

    public String getName();

    public JSONArray makeupFormAttributeList();

    public JSONObject makeupConfig(JSONObject jsonObj);

    public String genarate(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo);

    @Transactional
    public int batchUpdateHistoryProcessTask(String channelTypeUuid);

    public default String getHandler() {
        return ClassUtils.getUserClass(this.getClass()).getName();
    }
}
