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
