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

package neatlogic.framework.process.dto;

import neatlogic.framework.common.dto.BasePageVo;

import java.util.List;

public class ProcessCommentTemplateSearchVo extends BasePageVo {

    private String userUuid;

    private List<String> uuidList;

    private int isHasModifyAuthority = 0;

    private String type;

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public List<String> getUuidList() {
        return uuidList;
    }

    public void setUuidList(List<String> uuidList) {
        this.uuidList = uuidList;
    }

    public int getIsHasModifyAuthority() {
        return isHasModifyAuthority;
    }

    public void setIsHasModifyAuthority(int isHasModifyAuthority) {
        this.isHasModifyAuthority = isHasModifyAuthority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
