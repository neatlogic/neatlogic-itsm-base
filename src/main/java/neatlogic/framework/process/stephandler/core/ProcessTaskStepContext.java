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

package neatlogic.framework.process.stephandler.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.form.attribute.core.FormAttributeDataConversionHandlerFactory;
import neatlogic.framework.form.attribute.core.IFormAttributeDataConversionHandler;
import neatlogic.framework.form.dto.FormAttributeVo;
import neatlogic.framework.form.service.IFormCrossoverService;
import neatlogic.framework.process.crossover.IProcessTaskCrossoverService;
import neatlogic.framework.process.crossover.IProcessTaskStepDataCrossoverMapper;
import neatlogic.framework.process.crossover.ISelectContentByHashCrossoverMapper;
import neatlogic.framework.process.dto.ProcessTaskFormAttributeDataVo;
import neatlogic.framework.process.dto.ProcessTaskStepDataVo;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;

public class ProcessTaskStepContext {
    private final ProcessTaskStepVo processTaskStepVo;

    //以下都是缓存
    JSONObject stepConfig;
    List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList;

    public ProcessTaskStepContext(ProcessTaskStepVo processTaskStepVo) {
        this.processTaskStepVo = processTaskStepVo;
    }

    /**
     * 获取步骤配置信息
     *
     * @return JSONObject
     */
    public JSONObject getStepConfig() {
        if (stepConfig == null) {
            ISelectContentByHashCrossoverMapper selectContentByHashCrossoverMapper = CrossoverServiceFactory.getApi(ISelectContentByHashCrossoverMapper.class);
            String config = selectContentByHashCrossoverMapper.getProcessTaskStepConfigByHash(processTaskStepVo.getConfigHash());
            try {
                stepConfig = JSON.parseObject(config);
            } catch (Exception ex) {

            }
        }
        return stepConfig;
    }

    public ProcessTaskStepDataVo getStepData(String type, boolean needUser) {
        IProcessTaskStepDataCrossoverMapper processTaskStepDataCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskStepDataCrossoverMapper.class);
        ProcessTaskStepDataVo processTaskStepDataVo = new ProcessTaskStepDataVo();
        processTaskStepDataVo.setProcessTaskId(processTaskStepVo.getProcessTaskId());
        processTaskStepDataVo.setProcessTaskStepId(processTaskStepVo.getId());
        if (needUser) {
            processTaskStepDataVo.setFcu(UserContext.get().getUserUuid(true));
        }
        processTaskStepDataVo.setType(type);
        return processTaskStepDataCrossoverMapper.getProcessTaskStepData(processTaskStepDataVo);
    }

    public void saveStepData(String type, String data, boolean needUser) {
        IProcessTaskStepDataCrossoverMapper processTaskStepDataCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskStepDataCrossoverMapper.class);
        ProcessTaskStepDataVo processTaskStepDataVo = new ProcessTaskStepDataVo();
        processTaskStepDataVo.setProcessTaskId(processTaskStepVo.getProcessTaskId());
        processTaskStepDataVo.setProcessTaskStepId(processTaskStepVo.getId());
        if (needUser) {
            processTaskStepDataVo.setFcu(UserContext.get().getUserUuid(true));
        } else {
            //必须给个默认值，否则唯一索引不生效
            processTaskStepDataVo.setFcu("SYSTEM");
        }
        processTaskStepDataVo.setType(type);
        processTaskStepDataVo.setData(data);
        processTaskStepDataCrossoverMapper.replaceProcessTaskStepData(processTaskStepDataVo);
    }

    /**
     * 获取表单属性定义
     *
     * @param attributeUuid 属性uuid
     * @return FormAttributeVo
     */
    public FormAttributeVo getFormAttribute(String attributeUuid) {
        IFormCrossoverService formCrossoverService = CrossoverServiceFactory.getApi(IFormCrossoverService.class);
        return formCrossoverService.getFormAttributeByUuid(attributeUuid);
    }

    /**
     * 获取表单简单属性值
     *
     * @param attributeUuid 属性uuid
     * @return Object
     */
    public Object getFormSimpleValue(String attributeUuid) {
        if (processTaskFormAttributeDataList == null) {
            IProcessTaskCrossoverService processTaskCrossoverService = CrossoverServiceFactory.getApi(IProcessTaskCrossoverService.class);
            processTaskFormAttributeDataList = processTaskCrossoverService.getProcessTaskFormAttributeDataListByProcessTaskId(processTaskStepVo.getProcessTaskId());
        }
        if (CollectionUtils.isNotEmpty(processTaskFormAttributeDataList)) {
            Optional<ProcessTaskFormAttributeDataVo> op = processTaskFormAttributeDataList.stream().filter(d -> d.getAttributeUuid().equals(attributeUuid)).findFirst();
            if (op.isPresent()) {
                ProcessTaskFormAttributeDataVo attributeDataVo = op.get();
                if (attributeDataVo.getDataObj() != null) {
                    IFormAttributeDataConversionHandler handler = FormAttributeDataConversionHandlerFactory.getHandler(attributeDataVo.getHandler());
                    if (handler != null) {
                        return handler.getSimpleValue(attributeDataVo.getDataObj());
                    } else {
                        return attributeDataVo.getDataObj();
                    }
                }
            }

        }
        return null;
    }

    /**
     * 获取表单属性值
     *
     * @param attributeUuid 属性uuid
     * @return Object
     */
    public Object getFormValue(String attributeUuid) {
        if (processTaskFormAttributeDataList == null) {
            IProcessTaskCrossoverService processTaskCrossoverService = CrossoverServiceFactory.getApi(IProcessTaskCrossoverService.class);
            processTaskFormAttributeDataList = processTaskCrossoverService.getProcessTaskFormAttributeDataListByProcessTaskId(processTaskStepVo.getProcessTaskId());
        }
        if (CollectionUtils.isNotEmpty(processTaskFormAttributeDataList)) {
            Optional<ProcessTaskFormAttributeDataVo> op = processTaskFormAttributeDataList.stream().filter(d -> d.getAttributeUuid().equals(attributeUuid)).findFirst();
            if (op.isPresent()) {
                return op.get().getDataObj();
            }

        }
        return null;
    }

    /**
     * 获取表单组件隐藏属性值
     *
     * @param attributeUuid 属性uuid
     * @return Object
     */
    public Object getFormHiddenValue(String attributeUuid, String hiddenFieldUuid) {
        if (processTaskFormAttributeDataList == null) {
            IProcessTaskCrossoverService processTaskCrossoverService = CrossoverServiceFactory.getApi(IProcessTaskCrossoverService.class);
            processTaskFormAttributeDataList = processTaskCrossoverService.getProcessTaskFormAttributeDataListByProcessTaskId(processTaskStepVo.getProcessTaskId());
        }
        if (CollectionUtils.isNotEmpty(processTaskFormAttributeDataList)) {
            Optional<ProcessTaskFormAttributeDataVo> op = processTaskFormAttributeDataList.stream().filter(d -> d.getAttributeUuid().equals(attributeUuid)).findFirst();
            if (op.isPresent()) {
                Object dataObj = op.get().getDataObj();
                if (dataObj instanceof JSONObject) {
                    return ((JSONObject) op.get().getDataObj()).get(hiddenFieldUuid);
                } else if (dataObj instanceof JSONArray) {
                    JSONArray returnList = new JSONArray();
                    for (int i = 0; i < ((JSONArray) dataObj).size(); i++) {
                        returnList.add(((JSONArray) dataObj).getJSONObject(i).get(hiddenFieldUuid));
                    }
                    return returnList;
                }
            }

        }
        return null;
    }
}
