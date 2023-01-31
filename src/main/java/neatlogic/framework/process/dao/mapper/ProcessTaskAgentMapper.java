/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dao.mapper;

import neatlogic.framework.process.dto.agent.ProcessTaskAgentTargetVo;
import neatlogic.framework.process.dto.agent.ProcessTaskAgentVo;

import java.util.List;

/**
 * @author linbq
 * @since 2021/10/9 20:01
 **/
public interface ProcessTaskAgentMapper {

    List<Long> getProcessTaskAgentIdListByFromUserUuid(String fromUserUuid);

    List<ProcessTaskAgentVo> getProcessTaskAgentListByFromUserUuid(String fromUserUuid);

    List<ProcessTaskAgentVo> getProcessTaskAgentListByToUserUuid(String toUserUuid);

    List<ProcessTaskAgentTargetVo> getProcessTaskAgentTargetListByProcessTaskAgentId(Long processTaskAgentId);

    List<ProcessTaskAgentVo> getProcessTaskAgentDetailListByToUserUuid(String toUserUuid);

    List<ProcessTaskAgentVo> getProcessTaskAgentDetailListByFromUserUuidList(List<String> fromUserUuidList);

    int insertProcessTaskAgent(ProcessTaskAgentVo processTaskAgentVo);

    int insertIgnoreProcessTaskAgentTarget(ProcessTaskAgentTargetVo processTaskAgentTargetVo);

    int updateProcessTaskAgentIsActiveByFromUserUuid(String fromUserUuid);

    int deleteProcessTaskAgentByFromUserUuid(String fromUserUuid);

    int deleteProcessTaskAgentTargetByProcessTaskAgentIdList(List<Long> processTaskAgentIdList);
}
