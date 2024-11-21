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

import neatlogic.framework.asynchronization.thread.NeatLogicThread;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.process.operationauth.core.IOperationType;
import neatlogic.framework.process.constvalue.ProcessStepMode;
import neatlogic.framework.process.crossover.IProcessTaskCrossoverMapper;
import neatlogic.framework.process.dto.ProcessTaskStepVo;

public abstract class ProcessTaskStepThread extends NeatLogicThread {

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
        } finally {
            if (inOperationId != null) {
                IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
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
