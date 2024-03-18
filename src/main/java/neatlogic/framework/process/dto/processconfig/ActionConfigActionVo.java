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

import java.util.List;

/**
 * @author linbq
 * @since 2021/5/20 18:03
 **/
public class ActionConfigActionVo {
    private String integrationUuid;
    private String trigger;
    private AssertionConfigVo successCondition;
    private List<IntegrationParamMappingVo> paramMappingList;

    public String getIntegrationUuid() {
        return integrationUuid;
    }

    public void setIntegrationUuid(String integrationUuid) {
        this.integrationUuid = integrationUuid;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public AssertionConfigVo getSuccessCondition() {
        return successCondition;
    }

    public void setSuccessCondition(AssertionConfigVo successCondition) {
        this.successCondition = successCondition;
    }

    public List<IntegrationParamMappingVo> getParamMappingList() {
        return paramMappingList;
    }

    public void setParamMappingList(List<IntegrationParamMappingVo> paramMappingList) {
        this.paramMappingList = paramMappingList;
    }
}
