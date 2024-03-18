/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
