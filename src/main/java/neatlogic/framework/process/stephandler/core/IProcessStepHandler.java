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

import neatlogic.framework.process.constvalue.ProcessStepMode;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.dto.ProcessTaskStepWorkerVo;
import neatlogic.framework.process.dto.ProcessTaskVo;
import neatlogic.framework.process.exception.processtask.ProcessTaskException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

//需要把事务隔离级别调低，避免并发insert时因为gap lock导致deadlock
public interface IProcessStepHandler {
    /**
     * 返回控件
     *
     * @return 控件名
     */
    String getHandler();

    /**
     * 返回流程图图标配置
     *
     * @return json
     */
    JSONObject getChartConfig();

    /**
     * 返回控件类型，目前只有start,end,process和converge四种类型
     *
     * @return 类型
     */
    String getType();

    /**
     * 自动模式还是手动模式，自动模式引擎会自动触发handle动作
     *
     * @return at或mt
     */
    ProcessStepMode getMode();

    String getName();

    /**
     * 返回顺序
     *
     * @return 序号
     */
    int getSort();

    /**
     * 是否异步步骤
     *
     * @return true或false
     */
    boolean isAsync();

    /**
     * 是否允许设为开始节点
     *
     * @return true或false
     */
    Boolean isAllowStart();

    /**
     * 激活流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int active(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 分配处理人
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     * @throws ProcessTaskException 异常
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int assign(ProcessTaskStepVo currentProcessTaskStepVo) throws ProcessTaskException;

    /**
     * 挂起流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int hang(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 开始流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int start(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 处理流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int handle(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 接受流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int accept(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 转交步骤处理人
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @param workerList               处理人列表
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int transfer(ProcessTaskStepVo currentProcessTaskStepVo, List<ProcessTaskStepWorkerVo> workerList);

    /**
     * 完成流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int complete(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 自动完成流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int autoComplete(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 重审流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int reapproval(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 上一步发起的撤回动作
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int retreat(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 终止流程
     *
     * @param currentProcessTaskVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int abortProcessTask(ProcessTaskVo currentProcessTaskVo);

    /**
     * 终止流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int abort(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 恢复已终止流程
     *
     * @param currentProcessTaskVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int recoverProcessTask(ProcessTaskVo currentProcessTaskVo);

    /**
     * 恢复终止流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int recover(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 暂停流程步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int pause(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 获取当前步骤满足流转条件的后置步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    Set<Long> getNext(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 保存工单草稿，将会创建一个工单，工单状态为草稿状态
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int saveDraft(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 开始流程，将会创建一个作业
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int startProcess(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 回退步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int back(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 重新激活步骤
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int redo(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 评分
     *
     * @param currentProcessTaskVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int scoreProcessTask(ProcessTaskVo currentProcessTaskVo);

    /**
     * 失败
     *
     * @param currentProcessTaskStepVo 步骤信息
     * @return 1代表成功
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    int fail(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 获取对应步骤的minor worker
     *
     * @param taskStepVo 工单步骤
     * @return 对应步骤模块的协助处理人
     */
    List<ProcessTaskStepWorkerVo> getMinorWorkerList(ProcessTaskStepVo taskStepVo);

    /**
     * 获取对应步骤协助处理名
     * @return 协助处理名
     */
    String getMinorName();

    /**
     * 正向输入路径数量
     * -1代表不限制
     * @return
     */
    default int getForwardInputQuantity() {
        return -1;
    }
    /**
     * 正向输出路径数量
     * -1代表不限制
     * @return
     */
    default int getForwardOutputQuantity() {
        return -1;
    }
    /**
     * 回退输入路径数量
     * -1代表不限制
     * @return
     */
    default int getBackwardInputQuantity() {
        return -1;
    }
    /**
     * 回退输出路径数量
     * -1代表不限制
     * @return
     */
    default int getBackwardOutputQuantity() {
        return -1;
    }

    /**
     * 是否隐藏
     * @return
     */
    default boolean isHidden() {
        return false;
    }
}
