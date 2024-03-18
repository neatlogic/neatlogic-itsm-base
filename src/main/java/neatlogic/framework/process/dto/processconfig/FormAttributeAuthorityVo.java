/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

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

package neatlogic.framework.process.dto.processconfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linbq
 * @since 2021/5/19 17:55
 **/
public class FormAttributeAuthorityVo {
    private String action = "";
    private String type = "";
    private List<String> attributeUuidList = new ArrayList<>();
    private List<String> processStepUuidList = new ArrayList<>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getAttributeUuidList() {
        return attributeUuidList;
    }

    public void setAttributeUuidList(List<String> attributeUuidList) {
        this.attributeUuidList = attributeUuidList;
    }

    public List<String> getProcessStepUuidList() {
        return processStepUuidList;
    }

    public void setProcessStepUuidList(List<String> processStepUuidList) {
        this.processStepUuidList = processStepUuidList;
    }
}
