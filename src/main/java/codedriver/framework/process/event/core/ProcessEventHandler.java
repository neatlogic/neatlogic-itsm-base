/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.event.core;

import codedriver.framework.asynchronization.thread.CodeDriverThread;
import codedriver.framework.asynchronization.threadpool.CachedThreadPool;
import codedriver.framework.process.constvalue.ProcessTaskEvent;
import codedriver.framework.process.dao.mapper.ProcessEventMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessEventHandler {
    static Logger logger = LoggerFactory.getLogger(ProcessEventHandler.class);

    private static final ThreadLocal<List<CodeDriverThread>> RUNNABLES = new ThreadLocal<>();

    private static ProcessEventMapper processEventMapper;

    private static ProcessTaskMapper processTaskMapper;

    @Autowired
    public void setProcessEventMapper(ProcessEventMapper _processEventMapper) {
        processEventMapper = _processEventMapper;
    }

    @Autowired
    public void setProcessTaskMapper(ProcessTaskMapper _processTaskMapper) {
        processTaskMapper = _processTaskMapper;
    }

    public synchronized static void doEvent(ProcessTaskEvent event, Long flowJobStepId) {
        ProcessEventHandler.EventRunner runner = new ProcessEventHandler.EventRunner(event, flowJobStepId);
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            CachedThreadPool.execute(runner);
            return;
        }
        List<CodeDriverThread> runnableActionList = RUNNABLES.get();
        if (runnableActionList == null) {
            runnableActionList = new ArrayList<>();
            RUNNABLES.set(runnableActionList);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    List<CodeDriverThread> runnableActionList = RUNNABLES.get();
                    for (CodeDriverThread runnable : runnableActionList) {
                        CachedThreadPool.execute(runnable);
                    }
                }

                @Override
                public void afterCompletion(int status) {
                    RUNNABLES.remove();
                }
            });
        }
        runnableActionList.add(runner);

    }

    static class EventRunner extends CodeDriverThread {
        private final Long processTaskStepId;
        private final ProcessTaskEvent event;

        public EventRunner(ProcessTaskEvent _event, Long _processTaskStepId) {
            event = _event;
            processTaskStepId = _processTaskStepId;
        }

        @Override
        public void execute() {
            String oldName = Thread.currentThread().getName();
            Thread.currentThread().setName("PROCESSTASK-EVENTHANDLER-" + processTaskStepId);
            try {
                ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepId);

                if (processTaskStepVo == null) {
                    throw new ProcessTaskRuntimeException("找不到步骤信息，processTaskStepId：" + processTaskStepId);
                }

            } finally {
                Thread.currentThread().setName(oldName);
            }
        }

    }
}
