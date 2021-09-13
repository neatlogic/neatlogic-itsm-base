/*
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.stephandler.core;

import codedriver.framework.process.constvalue.ProcessStepMode;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import codedriver.framework.process.dto.ProcessTaskStepWorkerVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.exception.core.ProcessTaskException;
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
    Set<ProcessTaskStepVo> getNext(ProcessTaskStepVo currentProcessTaskStepVo);

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
     * 获取对应步骤的minor worker
     *
     * @param taskStepVo 工单步骤
     * @return 对应步骤模块的协助处理人
     */
    List<ProcessTaskStepWorkerVo> getMinorWorkerList(ProcessTaskStepVo taskStepVo);


    /**
     * 更新对应步骤模块的协助处理人
     * @param taskStepVo 工单步骤
     * @return 1代表成功
     */
    int insertMinorWorkerList(ProcessTaskStepVo taskStepVo);
}
