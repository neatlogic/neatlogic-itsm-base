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

package neatlogic.framework.process.processtaskserialnumberpolicy.core;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import neatlogic.framework.process.dto.ProcessTaskSerialNumberPolicyVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RootComponent
public class ProcessTaskSerialNumberPolicyHandlerFactory extends ModuleInitializedListenerBase {

    private static final Map<String, IProcessTaskSerialNumberPolicyHandler> policyMap = new HashMap<>();
    private static final List<ProcessTaskSerialNumberPolicyVo> policyList = new ArrayList<>();

    public static IProcessTaskSerialNumberPolicyHandler getHandler(String handler) {
        return policyMap.get(handler);
    }

    public static List<ProcessTaskSerialNumberPolicyVo> getPolicyHandlerList() {
        return policyList;
    }

    @Override
    public void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IProcessTaskSerialNumberPolicyHandler> map =
                context.getBeansOfType(IProcessTaskSerialNumberPolicyHandler.class);
        for (Map.Entry<String, IProcessTaskSerialNumberPolicyHandler> entry : map.entrySet()) {
            IProcessTaskSerialNumberPolicyHandler numberPolicy = entry.getValue();
            policyMap.put(numberPolicy.getHandler(), numberPolicy);
            ProcessTaskSerialNumberPolicyVo policy = new ProcessTaskSerialNumberPolicyVo();
            policy.setHandler(numberPolicy.getHandler());
            policy.setName(numberPolicy.getName());
            policy.setFormAttributeList(numberPolicy.makeupFormAttributeList());
            policyList.add(policy);
        }
    }

    @Override
    protected void myInit() {

    }

}
