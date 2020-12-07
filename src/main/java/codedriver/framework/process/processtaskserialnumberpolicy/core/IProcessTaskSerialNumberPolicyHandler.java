package codedriver.framework.process.processtaskserialnumberpolicy.core;

import org.springframework.util.ClassUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;
import codedriver.framework.process.dto.ProcessTaskVo;

public interface IProcessTaskSerialNumberPolicyHandler {

    public String getName();
    
    public JSONArray makeupFormAttributeList();
    
    public JSONObject makeupConfig(JSONObject jsonObj);
    
    public default String genarate(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo) {
        return genarate(processTaskSerialNumberPolicyVo, null);
    }
    
    public String genarate(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo, ProcessTaskVo processTaskVo);
    
    public default String getHandler() {
        return ClassUtils.getUserClass(this.getClass()).getName();
    }
}
