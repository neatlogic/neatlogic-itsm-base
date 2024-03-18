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
 * @since 2021/5/20 10:40
 **/
public class AutomaticIntervalCallbackConfigVo {
    private Integer interval;
    private String integrationUuid;
    private String resultTemplate;
    private List<IntegrationParamMappingVo> paramList = new ArrayList<>();
    private AssertionConfigVo successConfig;
    private AssertionConfigVo failConfig;

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getIntegrationUuid() {
        return integrationUuid;
    }

    public void setIntegrationUuid(String integrationUuid) {
        this.integrationUuid = integrationUuid;
    }

    public String getResultTemplate() {
        return resultTemplate;
    }

    public void setResultTemplate(String resultTemplate) {
        this.resultTemplate = resultTemplate;
    }

    public List<IntegrationParamMappingVo> getParamList() {
        return paramList;
    }

    public void setParamList(List<IntegrationParamMappingVo> paramList) {
        this.paramList = paramList;
    }

    public AssertionConfigVo getSuccessConfig() {
        return successConfig;
    }

    public void setSuccessConfig(AssertionConfigVo successConfig) {
        this.successConfig = successConfig;
    }

    public AssertionConfigVo getFailConfig() {
        return failConfig;
    }

    public void setFailConfig(AssertionConfigVo failConfig) {
        this.failConfig = failConfig;
    }
}
