/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.workcenter.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.process.exception.workcenter.AuthStringIrregularException;
import neatlogic.framework.restful.annotation.EntityField;
import org.apache.commons.lang3.StringUtils;

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

    public WorkcenterAuthorityVo(String authString) {
        if (authString.startsWith(GroupSearch.ROLE.getValuePlugin())) {
            this.setType(GroupSearch.ROLE.getValue());
            this.setUuid(authString.replaceAll(GroupSearch.ROLE.getValuePlugin(), StringUtils.EMPTY));
        } else if (authString.startsWith(GroupSearch.USER.getValuePlugin())) {
            this.setType(GroupSearch.USER.getValue());
            this.setUuid(authString.replaceAll(GroupSearch.USER.getValuePlugin(), StringUtils.EMPTY));
        } else if (authString.startsWith(GroupSearch.COMMON.getValuePlugin())) {
            this.setType(GroupSearch.COMMON.getValue());
            this.setUuid(authString.replaceAll(GroupSearch.COMMON.getValuePlugin(), StringUtils.EMPTY));
        } else {
            throw new AuthStringIrregularException(authString);
        }
    }

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
