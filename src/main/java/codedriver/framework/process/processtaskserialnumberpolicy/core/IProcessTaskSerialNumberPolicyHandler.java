package codedriver.framework.process.processtaskserialnumberpolicy.core;

import org.springframework.util.ClassUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;

public interface IProcessTaskSerialNumberPolicyHandler {

    public String getName();

    public JSONArray makeupFormAttributeList();

    public JSONObject makeupConfig(JSONObject jsonObj);

    public String genarate(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo);
    
    public int batchUpdateHistoryProcessTask(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo);

    public Long calculateSerialNumberSeedAfterBatchUpdateHistoryProcessTask(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo);
    
    public default String getHandler() {
        return ClassUtils.getUserClass(this.getClass()).getName();
    }
}
