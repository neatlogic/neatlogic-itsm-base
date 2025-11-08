/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
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
