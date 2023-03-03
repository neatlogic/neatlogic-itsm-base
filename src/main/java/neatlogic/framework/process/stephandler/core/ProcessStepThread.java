/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
