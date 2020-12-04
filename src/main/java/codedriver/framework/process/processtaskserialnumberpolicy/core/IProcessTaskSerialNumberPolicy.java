package codedriver.framework.process.processtaskserialnumberpolicy.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface IProcessTaskSerialNumberPolicy {

    public String getName();
    
    public JSONArray makeupFormAttributeList();
    
    public JSONObject makeupConfig(JSONObject jsonObj);
    
    @Transactional
    public String genarate(String channelTypeUuid, JSONObject config);
    
    public default String getHandler() {
        return ClassUtils.getUserClass(this.getClass()).getName();
    }
}
