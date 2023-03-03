package neatlogic.framework.process.dto;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.process.dto.score.ProcessScoreTemplateVo;
import neatlogic.framework.restful.annotation.EntityField;

public class ProcessTaskScoreTemplateVo extends BaseEditorVo {

	@EntityField(name = "工单id", type = ApiParamType.INTEGER)
	private Long processTaskId;
	@EntityField(name = "评分模版ID", type = ApiParamType.LONG)
	private Long scoreTemplateId;
	@EntityField(name = "评分配置", type = ApiParamType.JSONOBJECT)
	private JSONObject config;
	@EntityField(name = "是否自动评分", type = ApiParamType.INTEGER)
	private Integer isAuto;
    @EntityField(name = "评分配置hash", type = ApiParamType.STRING)
    private String configHash;
    @JSONField(serialize = false)
    private String configStr;
    
	public ProcessTaskScoreTemplateVo() {}

	public ProcessTaskScoreTemplateVo(ProcessScoreTemplateVo processScoreTemplateVo) {
        this.scoreTemplateId = processScoreTemplateVo.getScoreTemplateId();
        this.setConfig(processScoreTemplateVo.getConfig());
        this.isAuto = processScoreTemplateVo.getIsAuto();
    }

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public Long getScoreTemplateId() {
		return scoreTemplateId;
	}

	public void setScoreTemplateId(Long scoreTemplateId) {
		this.scoreTemplateId = scoreTemplateId;
	}

	public JSONObject getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = JSON.parseObject(config);
	}

    public Integer getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(Integer isAuto) {
        this.isAuto = isAuto;
    }

    public String getConfigHash() {
        return configHash;
    }

    public void setConfigHash(String configHash) {
        this.configHash = configHash;
    }

    public String getConfigStr() {
        if(StringUtils.isBlank(configStr) && config != null) {
            configStr = config.toJSONString();
        }
        return configStr;
    }
}
