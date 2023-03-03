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

package neatlogic.framework.process.event.core;

import neatlogic.framework.asynchronization.thread.NeatLogicThread;
import neatlogic.framework.asynchronization.threadpool.CachedThreadPool;
import neatlogic.framework.process.constvalue.ProcessTaskEvent;
import neatlogic.framework.process.dao.mapper.ProcessEventMapper;
import neatlogic.framework.process.dao.mapper.ProcessTaskMapper;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.exception.processtask.ProcessTaskStepNotFoundException;
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

    private static final ThreadLocal<List<NeatLogicThread>> RUNNABLES = new ThreadLocal<>();

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
        List<NeatLogicThread> runnableActionList = RUNNABLES.get();
        if (runnableActionList == null) {
            runnableActionList = new ArrayList<>();
            RUNNABLES.set(runnableActionList);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    List<NeatLogicThread> runnableActionList = RUNNABLES.get();
                    for (NeatLogicThread runnable : runnableActionList) {
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

    static class EventRunner extends NeatLogicThread {
        private final Long processTaskStepId;
        private final ProcessTaskEvent event;

        public EventRunner(ProcessTaskEvent _event, Long _processTaskStepId) {
            super("PROCESSTASK-EVENTHANDLER-" + _processTaskStepId);
            event = _event;
            processTaskStepId = _processTaskStepId;
        }

        @Override
        public void execute() {
            ProcessTaskStepVo processTaskStepVo = processTaskMapper.getProcessTaskStepBaseInfoById(processTaskStepId);
            if (processTaskStepVo == null) {
                throw new ProcessTaskStepNotFoundException(processTaskStepId);
            }
        }

    }
}
