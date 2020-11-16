package codedriver.framework.process.dto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.util.SnowflakeUtil;

public class ProcessTaskStepDataVo {
    static Logger logger = LoggerFactory.getLogger(ProcessTaskStepDataVo.class);
    private Long id;
    private Long processTaskId;
    private Long processTaskStepId;
    private JSONObject data;
    private String type;
    private String fcu;
    @JSONField(serialize = false)
    private transient Boolean isAutoGenerateId = false;

    public ProcessTaskStepDataVo() {

    }

    public ProcessTaskStepDataVo(boolean _isAutoGenerateId) {
        this.isAutoGenerateId = _isAutoGenerateId;
    }

    public ProcessTaskStepDataVo(Long processTaskId, Long processTaskStepId, String type) {
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
        this.type = type;
        this.isAutoGenerateId = true;
    }

    public synchronized Long getId() {
        if (id == null && isAutoGenerateId) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public Long getProcessTaskStepId() {
        return processTaskStepId;
    }

    public void setProcessTaskStepId(Long processTaskStepId) {
        this.processTaskStepId = processTaskStepId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(String data) {
        try {
            this.data = JSONObject.parseObject(data);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @JSONField(serialize = false)
    public String getDataStr() {
        if (data != null) {
            return data.toJSONString();
        }
        return null;
    }

    public String getFcu() {
        if (StringUtils.isBlank(fcu)) {
            if (UserContext.get() != null) {
                fcu = UserContext.get().getUserId();
            }
        }
        return fcu;
    }

    public void setFcu(String fcu) {
        this.fcu = fcu;
    }

    public Boolean getIsAutoGenerateId() {
        return isAutoGenerateId;
    }

    public void setIsAutoGenerateId(Boolean isAutoGenerateId) {
        this.isAutoGenerateId = isAutoGenerateId;
    }

}
