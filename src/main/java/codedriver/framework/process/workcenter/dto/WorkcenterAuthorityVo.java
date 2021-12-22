package codedriver.framework.process.workcenter.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

import java.io.Serializable;

/**
 * @author longrf
 * @date 2021/12/21 3:30 下午
 */
public class WorkcenterAuthorityVo implements Serializable {

    private static final long serialVersionUID = 1152066708547908924L;

    @EntityField(name = "工单中心分类uuid", type = ApiParamType.STRING)
    private String workcenterUuid;
    @EntityField(name = "类型", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "uuid", type = ApiParamType.STRING)
    private String uuid;


    public WorkcenterAuthorityVo(String workcenterUuid, String type, String uuid) {
        this.workcenterUuid = workcenterUuid;
        this.type = type;
        this.uuid = uuid;
    }

    public WorkcenterAuthorityVo() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWorkcenterUuid() {
        return workcenterUuid;
    }

    public void setWorkcenterUuid(String workcenterUuid) {
        this.workcenterUuid = workcenterUuid;
    }
}
