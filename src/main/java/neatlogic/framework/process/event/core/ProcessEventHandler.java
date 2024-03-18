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
