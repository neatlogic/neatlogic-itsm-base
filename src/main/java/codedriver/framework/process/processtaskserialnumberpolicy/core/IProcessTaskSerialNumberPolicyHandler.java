package codedriver.framework.process.processtaskserialnumberpolicy.core;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.process.dto.ProcessTaskSerialNumberPolicyVo;

public interface IProcessTaskSerialNumberPolicyHandler {

    String getName();

    JSONArray makeupFormAttributeList();

    JSONObject makeupConfig(JSONObject jsonObj);

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    String genarate(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo);

    int batchUpdateHistoryProcessTask(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo);

    Long calculateSerialNumberSeedAfterBatchUpdateHistoryProcessTask(ProcessTaskSerialNumberPolicyVo processTaskSerialNumberPolicyVo);

    default String getHandler() {
        return ClassUtils.getUserClass(this.getClass()).getName();
    }
}
