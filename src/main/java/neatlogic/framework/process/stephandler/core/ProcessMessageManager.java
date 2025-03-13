/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.process.stephandler.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.process.constvalue.ProcessStepHandlerType;
import neatlogic.framework.process.dto.ProcessStepRelVo;
import neatlogic.framework.process.util.ProcessTaskUtil;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProcessMessageManager {
    private static final ThreadLocal<ProcessMessageContext> context = new ThreadLocal<>();

    private static ProcessMessageContext getProcessMessageContext() {
        ProcessMessageContext processMessageContext = context.get();
        if (processMessageContext == null) {
            processMessageContext = new ProcessMessageContext();
            context.set(processMessageContext);
        }
        return context.get();
    }
    public static void setConfig(JSONObject config) {
        getProcessMessageContext().setConfig(config);
    }

    public static void setStepName(String stepName) {
        getProcessMessageContext().setStepName(stepName);
    }

    public static String getStepName() {
        return getProcessMessageContext().getStepName();
    }

    public static OperationTypeEnum getOperationType() {
        return getProcessMessageContext().getOperationType();
    }

    public static void setOperationType(OperationTypeEnum operationType) {
        getProcessMessageContext().setOperationType(operationType);
    }

    public static void release() {
        context.remove();
    }

    public static List<String> getEffectiveStepUuidList() {
        ProcessMessageContext processMessageContext = getProcessMessageContext();
        List<String> effectiveStepUuidList = processMessageContext.getEffectiveStepUuidList();
        if (effectiveStepUuidList == null) {
            List<ProcessStepRelVo> allProcessStepRelList = getProcessStepRelList();
            String startStepUuid = null;
            String endStepUuid = null;
            JSONObject process = processMessageContext.getConfig();
            if (MapUtils.isEmpty(process)) {
                return new ArrayList<>();
            }
            JSONArray stepList = process.getJSONArray("stepList");
            for (int i = 0; i < stepList.size(); i++) {
                JSONObject step = stepList.getJSONObject(i);
                if (MapUtils.isEmpty(step)) {
                    continue;
                }
                String handler = step.getString("handler");
                if (Objects.equals(handler, ProcessStepHandlerType.START.getHandler())) {
                    startStepUuid = step.getString("uuid");
                } else if (Objects.equals(handler, ProcessStepHandlerType.END.getHandler())) {
                    endStepUuid = step.getString("uuid");
                }
            }
            effectiveStepUuidList = ProcessTaskUtil.getEffectivePostStepUuidList(startStepUuid, endStepUuid, allProcessStepRelList);
            List<ProcessStepRelVo> connectionList = new ArrayList<>();
            for (ProcessStepRelVo processStepRelVo : allProcessStepRelList) {
                if (effectiveStepUuidList.contains(processStepRelVo.getFromStepUuid()) && effectiveStepUuidList.contains(processStepRelVo.getToStepUuid())) {
                    connectionList.add(processStepRelVo);
                }
            }
            processMessageContext.setConnectionList(connectionList);
            processMessageContext.setEffectiveStepUuidList(effectiveStepUuidList);
        }
        return effectiveStepUuidList;
    }

    public static List<ProcessStepRelVo> getProcessStepRelList() {
        ProcessMessageContext processMessageContext = getProcessMessageContext();
        List<ProcessStepRelVo> processStepRelList = processMessageContext.getConnectionList();
        if (processStepRelList == null) {
            JSONObject process = processMessageContext.getConfig();
            if (MapUtils.isEmpty(process)) {
                return new ArrayList<>();
            }
            JSONArray connectionList = process.getJSONArray("connectionList");
            if (connectionList == null) {
                connectionList = new JSONArray();
            }
            for (int i = connectionList.size() - 1; i >= 0; i--) {
                JSONObject connectionObj = connectionList.getJSONObject(i);
                if (MapUtils.isEmpty(connectionObj)) {
                    connectionList.remove(i);
                }
                if (StringUtils.isBlank(connectionObj.getString("uuid"))) {
                    connectionList.remove(i);
                }
                if (StringUtils.isBlank(connectionObj.getString("fromStepUuid"))) {
                    connectionList.remove(i);
                }
                if (StringUtils.isBlank(connectionObj.getString("toStepUuid"))) {
                    connectionList.remove(i);
                }
                if (StringUtils.isBlank(connectionObj.getString("type"))) {
                    connectionList.remove(i);
                }
            }
            processStepRelList = connectionList.toJavaList(ProcessStepRelVo.class);
            processMessageContext.setConnectionList(processStepRelList);
        }
        return processStepRelList;
    }

}
