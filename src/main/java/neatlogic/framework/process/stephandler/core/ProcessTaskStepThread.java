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

import neatlogic.framework.asynchronization.thread.NeatLogicThread;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.process.constvalue.ProcessStepMode;
import neatlogic.framework.process.crossover.IProcessTaskCrossoverMapper;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.operationauth.core.IOperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProcessTaskStepThread extends NeatLogicThread {

    private final static Logger logger = LoggerFactory.getLogger(ProcessTaskStepThread.class);

    private final ProcessTaskStepVo processTaskStepVo;

    private final Long processTaskId;

    private final Long processTaskStepId;

    private final ProcessStepMode mode;

    private final IOperationType operationType;

    private Long inOperationId;

    public ProcessTaskStepThread(IOperationType operationType, ProcessTaskStepVo processTaskStepVo, ProcessStepMode mode) {
        super("PROCESSTASK-STEP-" + (operationType != null ? operationType.getValue() : "空") + (processTaskStepVo != null ? "-" + processTaskStepVo.getName() + "_" +processTaskStepVo.getId() : ""));
        this.processTaskStepVo = processTaskStepVo;
        this.processTaskId = processTaskStepVo.getProcessTaskId();
        this.processTaskStepId = processTaskStepVo.getId();
        this.mode = mode;
        this.operationType = operationType;
    }
    @Override
    protected void execute() {
        try {
            myExecute(processTaskStepVo);
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
            processTaskCrossoverMapper.deleteProcessTaskStepInOperationByProcessTaskIdAndProcessTaskStepIdAndOperationType(processTaskId, processTaskStepId, operationType.getValue());
            if (inOperationId != null) {
                processTaskCrossoverMapper.deleteProcessTaskStepInOperationById(inOperationId);
            }
        }
    }

    protected abstract void myExecute(ProcessTaskStepVo processTaskStepVo);

    public ProcessTaskStepVo getProcessTaskStepVo() {
        return processTaskStepVo;
    }

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setInOperationId(Long inOperationId) {
        this.inOperationId = inOperationId;
    }

    public Long getProcessTaskStepId() {
        return processTaskStepId;
    }

    public ProcessStepMode getMode() {
        return mode;
    }

    public IOperationType getOperationType() {
        return operationType;
    }
}
