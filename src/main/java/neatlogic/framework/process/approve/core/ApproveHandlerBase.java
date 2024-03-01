package neatlogic.framework.process.approve.core;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.exception.type.PermissionDeniedException;
import neatlogic.framework.process.approve.dto.ApproveEntityVo;
import neatlogic.framework.process.constvalue.ProcessFlowDirection;
import neatlogic.framework.process.crossover.IProcessTaskCrossoverService;
import neatlogic.framework.process.dao.mapper.ChannelMapper;
import neatlogic.framework.process.dao.mapper.ProcessTaskMapper;
import neatlogic.framework.process.dto.ChannelPriorityVo;
import neatlogic.framework.process.exception.processtask.ProcessTaskNextStepIllegalException;
import neatlogic.framework.process.exception.processtask.ProcessTaskNextStepOverOneException;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

public abstract class ApproveHandlerBase implements IApproveHandler {

    @Resource
    private ProcessTaskMapper processTaskMapper;

    @Resource
    private ChannelMapper channelMapper;

    @Override
    public Long startProcess(String channelUuid, Long id) throws Exception {
        JSONObject paramObj = new JSONObject();
//        paramObj.put("content", "xxx");
        paramObj.put("channelUuid", channelUuid);
        paramObj.put("owner", UserContext.get().getUserUuid());
        ApproveEntityVo approveEntity = myStartProcess(id);
        paramObj.put("source", approveEntity.getSource());
        paramObj.put("title", approveEntity.getTitle());
        paramObj.put("approveEntity", approveEntity);
        List<ChannelPriorityVo> channelPriorityList = channelMapper.getChannelPriorityListByChannelUuid(channelUuid);
        if (CollectionUtils.isNotEmpty(channelPriorityList)) {
            for (ChannelPriorityVo channelPriorityVo : channelPriorityList) {
                if (Objects.equals(channelPriorityVo.getIsDefault(), 1)) {
                    paramObj.put("priorityUuid", channelPriorityVo.getPriorityUuid());
                }
            }
        }
        IProcessTaskCrossoverService processTaskCrossoverService = CrossoverServiceFactory.getApi(IProcessTaskCrossoverService.class);
        JSONObject saveResultObj = processTaskCrossoverService.saveProcessTaskDraft(paramObj);

        //查询可执行下一 步骤
        Long processTaskId = saveResultObj.getLong("processTaskId");
        List<Long> nextStepIdList = processTaskMapper.getToProcessTaskStepIdListByFromIdAndType(saveResultObj.getLong("processTaskStepId"), ProcessFlowDirection.FORWARD.getValue());
        if (nextStepIdList.isEmpty()) {
            throw new ProcessTaskNextStepIllegalException(processTaskId);
        }
        if (nextStepIdList.size() != 1) {
            throw new ProcessTaskNextStepOverOneException(processTaskId);
        }
        saveResultObj.put("nextStepId", nextStepIdList.get(0));

        //流转
        saveResultObj.put("source", paramObj.getString("source"));
        processTaskCrossoverService.startProcessProcessTask(saveResultObj);
        return processTaskId;
    }

    protected abstract ApproveEntityVo myStartProcess(Long id) throws PermissionDeniedException;
}
