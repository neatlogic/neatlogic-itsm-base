/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.stephandler.core;

import codedriver.framework.asynchronization.thread.CodeDriverThread;
import codedriver.framework.dto.UserVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public abstract class ProcessStepThread extends CodeDriverThread {
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
