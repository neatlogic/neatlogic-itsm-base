/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dao.mapper;

import codedriver.framework.process.dto.agent.ProcessTaskAgentTargetVo;
import codedriver.framework.process.dto.agent.ProcessTaskAgentVo;

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

    int insertProcessTaskAgent(ProcessTaskAgentVo processTaskAgentVo);

    int insertProcessTaskAgentTarget(ProcessTaskAgentTargetVo processTaskAgentTargetVo);

    int updateProcessTaskAgentIsActiveByFromUserUuid(String fromUserUuid);

    int deleteProcessTaskAgentByFromUserUuid(String fromUserUuid);

    int deleteProcessTaskAgentTargetByProcessTaskAgentIdList(List<Long> processTaskAgentIdList);
}
