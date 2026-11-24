package neatlogic.framework.process.dto;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

/** 工单详情展示的上报来源及其目标页面的定位参数。 */
public class ProcessTaskReportSourceVo {
    // 父工单和转报来源共用同一工单详情目标。
    public static final String PROCESS_TASK_TARGET = "processtask";

    @EntityField(name = "common.name", type = ApiParamType.STRING)
    private final String name;
    @EntityField(name = "common.source", type = ApiParamType.STRING)
    private String sourceText;
    @EntityField(name = "common.type", type = ApiParamType.STRING)
    private final String targetType;
    @EntityField(name = "common.config", type = ApiParamType.JSONOBJECT)
    private final JSONObject targetParams;

    /** 目标为空时，页面只显示来源文案。 */
    public ProcessTaskReportSourceVo(String name, String targetType, JSONObject targetParams) {
        this.name = name;
        this.targetType = targetType;
        this.targetParams = targetParams;
    }

    public String getName() {
        return name;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getTargetType() {
        return targetType;
    }

    public JSONObject getTargetParams() {
        return targetParams;
    }
}
