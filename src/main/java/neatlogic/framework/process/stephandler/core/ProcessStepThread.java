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
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public abstract class ProcessStepThread extends NeatLogicThread {
    Logger logger = LoggerFactory.getLogger(ProcessStepThread.class);
    private final ProcessTaskStepVo processTaskStepVo;
    private Supplier<Integer> supplier;
    private UserVo currentUserVo;
    public ProcessTaskStepVo getProcessTaskStepVo() {
        return processTaskStepVo;
    }

    public void setSupplier(Supplier<Integer> supplier) {
        this.supplier = supplier;
    }

    public ProcessStepThread(ProcessTaskStepVo _processTaskStepVo) {
        super("PROCESSTASK-STEP-HANDLER" + (_processTaskStepVo != null ? "-" + _processTaskStepVo.getId() : ""));
        this.processTaskStepVo = _processTaskStepVo;
    }

    public ProcessStepThread(ProcessTaskStepVo _processTaskStepVo, UserVo _currentUserVo) {
        this(_processTaskStepVo);
        this.currentUserVo = _currentUserVo;
    }
    @Override
    public final void execute() {
        try {
            myExecute();
        } finally {
            if (supplier != null) {
                supplier.get();
            }
        }
    }

    protected abstract void myExecute();
}
