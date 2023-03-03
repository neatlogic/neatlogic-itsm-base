package neatlogic.framework.process.workerpolicy.core;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.dto.ProcessTaskStepWorkerPolicyVo;
import neatlogic.framework.process.dto.ProcessTaskStepWorkerVo;

public interface IWorkerPolicyHandler {
    String getType();

    String getName();

    /**
     * 是否只分配一次处理人
     *
     * @return
     */
    int isOnlyOnceExecute();

    /**
     * @param @param  workerPolicyVo
     * @param @param  currentProcessTaskStepVo
     * @param @return
     * @return Boolean 成功分配到处理人则返回true，分配不到则返回false
     * @Author: chenqiwei
     * @Time:Sep 18, 2019
     * @Description: TODO
     */
    @Transactional
    List<ProcessTaskStepWorkerVo> execute(ProcessTaskStepWorkerPolicyVo workerPolicyVo, ProcessTaskStepVo currentProcessTaskStepVo);
}
